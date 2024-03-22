package com.saad.invitationmaker.core.repo

import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.saad.invitationmaker.app.utils.isInternetAvailable
import com.saad.invitationmaker.core.di.PixabayApiService
import com.saad.invitationmaker.core.di.StickerApiService
import com.saad.invitationmaker.core.local.AppDataBase
import com.saad.invitationmaker.core.network.apiService.ApiService
import com.saad.invitationmaker.core.network.models.Hit
import com.saad.invitationmaker.core.network.models.ImageList
import com.saad.invitationmaker.features.backgrounds.models.CategoryModel
import com.saad.invitationmaker.features.editor.bottomSheets.fragment.TempModel
import com.saad.invitationmaker.features.editor.models.CategoryModelSticker
import com.saad.invitationmaker.features.home.models.AllCardsDesigns
import com.saad.invitationmaker.features.home.models.AllViews
import com.saad.invitationmaker.features.home.models.LocalCardDetailsModel
import com.sofit.app.utils.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepoImpl @Inject constructor(
    private val context: Context,
    @StickerApiService private val stickerApi: ApiService,
    @PixabayApiService private val pixaBayApi: ApiService,
    private val firestore: FirebaseFirestore,
    private val local: AppDataBase,
) : Repo {

    private val getAllInvitation = firestore.collection("categories").document("invitation")

    override val designsLiveData = SingleLiveEvent<List<Hit>>()
    override suspend fun storeImage(img: String) {
        try {

            val imgBitmap = downloadBitmap(img)
            if (imgBitmap != null) {
//                saveImageToHiddenDirectory(imgBitmap)

            }

        } catch (e: Exception) {
            Log.e("ImageStorage", "Error storing image: ${e.message}")
        }
    }


    override suspend fun saveTempStickerToDB(sticker: TempModel) {
        local.tempStickerDao().insertData(sticker)
    }

    override suspend fun getTempStickerFromRoom(): TempModel {
        val data = local.tempStickerDao().getStickers()
        return data
    }

    fun bitmapToString(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    override suspend fun saveSingleCardLocally(category: String, docId: String) {
        val isAvailable = local.cardsDao().isCardDetailAvailable(docId)
        if (isAvailable == null) {
            singleDesignCall(category, docId)
        }
    }

    override fun getSingleCardDetailsLocal(category: String, docId: String) =
        flow {
            val data = local.cardsDao().getSingleCardDetails(docId)
            emit(data)
            /*      val isAvailable = local.cardsDao().isCardDetailAvailable(docId)
                  if (isAvailable != null) {
                      val data: LocalCardDetailsModel = local.cardsDao().getSingleCardDetails(docId)
                      emit(data)
                  } else {
                      val job = CoroutineScope(Dispatchers.IO).launch {
                          singleDesignCall(category, docId)
                      }
                      if (job.isCompleted) {
                          val data = local.cardsDao().getSingleCardDetails(docId)
                          emit(data)
                      }
                  }*/
        }

    override suspend fun isDataBaseEmpty(): Boolean {
        val cardDetail = local.cardsDao().getAnyCardDetail()
        return cardDetail == null
    }

    override suspend fun isStickerDataBaseEmpty(): Boolean {
        val background = local.stickerDao().isStickerAvailable()
        return background == null
    }

    override suspend fun isBackgroundDataBaseEmpty(): Boolean {
        val background = local.backgroundDao().isBackgroundAvailable()
        return background == null
    }

    override suspend fun isCardDetailsAvailable(docId: String): Boolean {
        val available = local.cardsDao().isCardDetailAvailable(docId)
        return available == null
    }

    override val getAllBackgrounds: Flow<List<CategoryModel>> = flow {
        val data = local.backgroundDao().getBackgrounds()
        emit(data)
    }

    override val getAllStickerss: Flow<List<CategoryModelSticker>> = flow {
        val data = local.stickerDao().getStickers()
        emit(data)
    }


    override suspend fun getAllStickersRemote() {
        try {
            val natureResponse = fetchBackgrounds("backgrounds")
            val backgroundsResponse = fetchBackgrounds("animals")
            val scienceResponse = fetchBackgrounds("buildings")
            val computerResponse = fetchBackgrounds("food")

            if (backgroundsResponse.isSuccessful && natureResponse.isSuccessful &&
                scienceResponse.isSuccessful && computerResponse.isSuccessful
            ) {
                val customModel: List<CategoryModelSticker> = listOf(
                    CategoryModelSticker(
                        "Backgrounds",
                        hit = natureResponse.body()?.hits ?: emptyList()
                    ),
                    CategoryModelSticker(
                        "Animals",
                        hit = backgroundsResponse.body()?.hits ?: emptyList()
                    ),

                    CategoryModelSticker(
                        "Buildings",
                        hit = scienceResponse.body()?.hits ?: emptyList()
                    ),
                    CategoryModelSticker(
                        "Food",
                        hit = computerResponse.body()?.hits ?: emptyList()
                    )
                )
                local.stickerDao().insertData(customModel)
            } else {
                // Handle unsuccessful response
            }
        } catch (e: Exception) {
            // Handle exceptions
        }
    }

    override suspend fun getAllBackgroundsRemote() {
        try {
            val natureResponse = fetchStickers("nature")
            val backgroundsResponse = fetchStickers("travel")
            val scienceResponse = fetchStickers("health")
            val computerResponse = fetchStickers("computer")

            if (backgroundsResponse.isSuccessful && natureResponse.isSuccessful &&
                scienceResponse.isSuccessful && computerResponse.isSuccessful
            ) {
                val customModel = listOf(
                    CategoryModel("Nature", hit = natureResponse.body()?.hits ?: emptyList()),
                    CategoryModel(
                        "Backgrounds",
                        hit = backgroundsResponse.body()?.hits ?: emptyList()
                    ),

                    CategoryModel("Health", hit = scienceResponse.body()?.hits ?: emptyList()),
                    CategoryModel("Computer", hit = computerResponse.body()?.hits ?: emptyList())
                )
                local.backgroundDao().insertData(customModel)
            } else {
                // Handle unsuccessful response
            }
        } catch (e: Exception) {
            // Handle exceptions
        }
    }

    private suspend fun fetchStickers(category: String): Response<ImageList> {
        return withContext(Dispatchers.IO) {
            pixaBayApi.getDesignsBackground(
                1,
                10,
                category,
                "all",
                "popular",
                "vector",
                true
            )
        }
    }

    private suspend fun fetchBackgrounds(category: String): Response<ImageList> {
        return withContext(Dispatchers.IO) {
            pixaBayApi.getDesignsBackground(1, 10, category, "vertical", "latest", "photo", true)
        }
    }

    override suspend fun getAllDataFromFireStore() {
        if (isDataBaseEmpty() && isInternetAvailable(context)) {

            val weddingInvitation = getAllInvitation.collection("wedding").get().await()
            val bridalShower = getAllInvitation.collection("bs").get().await()
            val birthday = getAllInvitation.collection("bd").get().await()

            val dataList = mutableListOf<AllCardsDesigns>()

            fun addDocumentsToDataList(documents: QuerySnapshot, category: String) {
                for (document in documents) {
//                    val thumbnailUrl = document.data["thumbnail"].toString()
//                    val bitmap = downloadBitmap(thumbnailUrl)
//                    bitmap?.let {
//                        val fileName = document.id
//                        val path = saveBitmapToFile(it, fileName)
                    val customModel = AllCardsDesigns(
                        docId = document.id,
                        thumbnail = document.data["thumbnail"].toString(),
                        category = category
                    )
                    dataList.add(customModel)
//                    }

                }
            }

            addDocumentsToDataList(bridalShower, "Bridal Shower")
            addDocumentsToDataList(birthday, "Birthday")
            addDocumentsToDataList(weddingInvitation, "Wedding")

            // Insert data into local database
            local.cardsDao().insertData(dataList)
            return
        }
    }

    private fun saveBitmapToFile(bitmap: Bitmap, fileName: String): String {
        val file = File(context.filesDir, fileName)
        FileOutputStream(file).use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        }
        return file.absolutePath
    }

    override suspend fun downloadBitmap(imageUrl: String): Bitmap? {

        return try {

            /*   val target = Glide.with(context)
                   .asBitmap()
                   .load(imageUrl)
                   .submit()

               // Get the downloaded Bitmap
               val bitmap = withContext(Dispatchers.IO) {
                   target.get()
               }*/
            val bitmap = withContext(Dispatchers.IO) {
                Glide.with(context)
                    .asBitmap()
                    .load(imageUrl)
                    .submit()
                    .get() // This is a blocking call, but it's done on a background thread
            }
            // Cleanup and return the Bitmap
//            Glide.with(context).clear(target)
            bitmap

        } catch (e: Exception) {
            Log.e("ImageDownload", "Error downloading image: ${e.message}")
            null
        }
    }

    override val getAllDataFromLocal: Flow<List<AllCardsDesigns>> = flow {
        val data = local.cardsDao().getData()
        emit(data)
    }


    override suspend fun checkNetworkAvailability(category: String) {
        if (!isInternetAvailable(context)) {
            //Do the DB Call
        } else {
            getPixaBayDesigns(category);
        }
    }

    override suspend fun getPixaBayDesigns(category: String) {
        try {
            val response =
                pixaBayApi.getDesignsBackground(
                    1,
                    20,
                    category,
                    "vertical",
                    "latest",
                    "photo",
                    true
                )
            if (response.isSuccessful) {
//                Utils.log("${response.body()}")
                designsLiveData.postValue(response.body()?.hits)

            } else {
                //unsuccessful
            }
        } catch (e: Exception) {
            //exception
        }
    }


    override suspend fun singleDesignCall(category: String, docId: String) {
        val collectionRef = when (category) {
            "Wedding" -> {
//                getAllGreeting.collection("party")
                getAllInvitation.collection("wedding")
            }

            "Bridal Shower" -> getAllInvitation.collection("bs")
            "Birthday" -> getAllInvitation.collection("bd")
            else -> null
        }

        collectionRef?.document(docId)?.addSnapshotListener { snapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val background = snapshot.getString("background") ?: ""

                val arrayOfMaps = snapshot.get("views") as? List<Map<String, Any>> ?: emptyList()

                val listOfView = mutableListOf<AllViews>()
                for (data in arrayOfMaps) {
                    val customSingleDetail = AllViews(
                        viewId = data["viewId"] as? String,
                        viewData = data["data"] as? String,
                        alignment = data["alignment"] as? String,
                        viewType = data["viewType"] as? String,
                        fontSize = data["fontSize"] as? String,
                        font = data["font"] as? String,
                        letterSpacing = data["letterSpacing"] as? String,
                        lineHeight = data["lineHeight"] as? String,
                        textStyle = data["textStyle"] as? String,
                        color = data["color"] as? String,
                        xCoordinate = data["x"] as? String,
                        yCoordinate = data["y"] as? String,
                        width = data["width"] as? String,
                        height = data["height"] as? String,
                        priority = data["priority"] as? String
                    )
                    listOfView.add(customSingleDetail)
                }

                CoroutineScope(Dispatchers.IO).launch {
                    val bitmap = downloadBitmap(background)

                }
                val customModel = LocalCardDetailsModel(
                    docId = docId,
                    background = background,
                    listOfView
                )

                CoroutineScope(Dispatchers.IO).launch {
                    local.cardsDao().insertCardsDetail(customModel)
                }
            }
        }
    }
}
