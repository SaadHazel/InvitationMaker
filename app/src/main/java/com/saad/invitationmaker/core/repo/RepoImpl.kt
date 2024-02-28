package com.saad.invitationmaker.core.repo

import android.content.Context
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
    /* private val getAllGreeting =
         firestore.collection("categories").document("greeting")*/


    private val getAllInvitation = firestore.collection("categories").document("invitation")

    override val designsLiveData = SingleLiveEvent<List<Hit>>()

//    override val stickerLiveData = SingleLiveEvent<List<Hit>>()

//    override val stickerLiveData = MutableLiveData<List<Data>?>()

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
//            val partyGreeting = getAllGreeting.collection("party").get().await()

            val dataList = mutableListOf<AllCardsDesigns>()

            fun addDocumentsToDataList(documents: QuerySnapshot, category: String) {
                for (document in documents) {
                    val customModel = AllCardsDesigns(
                        docId = document.id,
                        thumbnail = document.data["thumbnail"].toString(),
                        category = category
                    )
                    dataList.add(customModel)
                }
            }

            // Add documents for each category
//            addDocumentsToDataList(partyGreeting, "Wedding")
            addDocumentsToDataList(bridalShower, "Bridal Shower")
            addDocumentsToDataList(birthday, "Birthday")
            addDocumentsToDataList(weddingInvitation, "Wedding")

            // Insert data into local database
            local.cardsDao().insertData(dataList)

            /* for (document in partyGreeting) {
                 val customModel = AllCardsDesigns(
                     docId = document.id,
                     thumbnail = document.data["thumbnail"].toString(),
                     category = "Wedding"
                 )
                 dataList.add(customModel)
             }
             for (document in bridalShower) {
                 val customModel = AllCardsDesigns(
                     docId = document.id,
                     thumbnail = document.data["thumbnail"].toString(),
                     category = "Bridal Shower"
                 )
                 dataList.add(customModel)
             }
             for (document in birthday) {
                 val customModel = AllCardsDesigns(
                     docId = document.id,
                     thumbnail = document.data["thumbnail"].toString(),
                     category = "Birthday"
                 )
                 dataList.add(customModel)
             }
             for (document in weddingInvitation) {
                 val customModel = AllCardsDesigns(
                     docId = document.id,
                     thumbnail = document.data["thumbnail"].toString(),
                     category = "Wedding"
                 )
                 dataList.add(customModel)
             }
             local.cardsDao().insertData(dataList)*/
            return
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
//            getSticker(category)
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

    /* override suspend fun getSticker(category: String) {
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
             *//*       val response = stickerApi.getStickers(
                       locale = "en-US",
                       page = 1,
                       limit = 20,
                       order = "latest",
                       term = "flowers",
                       contentType = "vector"
                   )*//*
            if (response.isSuccessful) {
                *//*    val stickerResponse: Stickers? = response.body()
                    val stickerData = stickerResponse?.data
                    log("EDITORACTIVITYTAG", "stickerData $stickerData")*//*
                stickerLiveData.postValue(response.body()?.hits)
            } else {
                log("EDITORACTIVITYTAG", "Response UnSuccessful")
            }
        } catch (e: Exception) {
            log("EDITORACTIVITYTAG", "error in fetching $e")
        }
    }*/

    /*    override suspend fun saveGreetingToWeddingCollection(dataList: LocalCardDetailsModel) {
            val weddingData = hashMapOf(
                "background" to dataList.background,
                "views" to dataList.views.map { view ->
                    hashMapOf(
                        "viewId" to view.viewId,
                        "data" to view.viewData,
                        "alignment" to view.alignment,
                        "viewType" to view.viewType,
                        "fontSize" to view.fontSize,
                        "font" to view.font,
                        "letterSpacing" to view.letterSpacing,
                        "lineHeight" to view.lineHeight,
                        "textStyle" to view.textStyle,
                        "color" to view.color,
                        "x" to view.xCoordinate,
                        "y" to view.yCoordinate,
                        "width" to view.width,
                        "height" to view.height,
                        "priority" to view.priority
                    )
                }
            )

            firestore.collection("categories").document("invitation").collection("wedding")
                .add(weddingData)
                .addOnSuccessListener { documentReference ->
                    println("Greeting document added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    println("Error adding greeting document: $e")
                }
        }*/

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

    /* override suspend fun singleDesignCall(category: String, docId: String) {
         if (category == "greetingParty") {
             try {
                 getAllGreeting.collection("party").document(docId)
                     .addSnapshotListener { snapshot, e ->
                         if (e != null) {
                             return@addSnapshotListener
                         }
                         if (snapshot != null && snapshot.exists()) {
                             val background = snapshot.getString("background") ?: ""
                             val arrayOfMaps =
                                 snapshot.get("views") as? List<Map<String, Any>> ?: emptyList()
                             val listOfView: MutableList<AllViews> = mutableListOf()
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
                                 log("GEttingData", "$customSingleDetail")
                                 listOfView.add(customSingleDetail)
                             }
                             log("GEttingData", "In List $listOfView")
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
             } catch (e: Exception) {

             }
         } else if (category == "invitationWedding") {
             try {
                 getAllInvitation.collection("wedding").document(docId)
                     .addSnapshotListener { snapshot, e ->
                         if (e != null) {
                             return@addSnapshotListener
                         }
                         if (snapshot != null && snapshot.exists()) {
                             val background = snapshot.getString("background") ?: ""
                             val arrayOfMaps =
                                 snapshot.get("views") as? List<Map<String, Any>> ?: emptyList()
                             val listOfView: MutableList<AllViews> = mutableListOf()
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
             } catch (e: Exception) {

             }
         }
     }*/
}
