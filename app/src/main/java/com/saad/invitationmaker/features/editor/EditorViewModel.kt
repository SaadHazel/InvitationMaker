package com.saad.invitationmaker.features.editor

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saad.invitationmaker.core.network.models.Hit
import com.saad.invitationmaker.core.repo.Repo
import com.saad.invitationmaker.features.editor.bottomSheets.fragment.TempModel
import com.saad.invitationmaker.features.editor.models.CategoryModelSticker
import com.saad.invitationmaker.features.home.models.LocalCardDetailsModel
import com.sofit.app.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.Stack
import javax.inject.Inject

@HiltViewModel
class EditorViewModel @Inject constructor(private val repo: Repo) : ViewModel() {
    init {

        CoroutineScope(Dispatchers.IO).launch {
            if (repo.isBackgroundDataBaseEmpty()) {
                repo.getAllBackgroundsRemote()
                getAllBackgrounds()
            } else {
                getAllBackgrounds()
            }
            if (repo.isStickerDataBaseEmpty()) {
                repo.getAllStickersRemote()
                getAllStickers()
            } else {
                getAllStickers()
            }
        }
    }

    private val _AllBackgrounds = MutableLiveData<List<Hit>>()
    private val _AllSticker = MutableLiveData<List<CategoryModelSticker>>()

    val AllBackgrounds: LiveData<List<Hit>>
        get() = _AllBackgrounds

    val AllSticker: LiveData<List<CategoryModelSticker>>
        get() = _AllSticker

    private fun getAllBackgrounds() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getAllBackgrounds.collect { all ->
                _AllBackgrounds.postValue(all[0].hit)
            }
        }
    }

    suspend fun saveTempSticker(stickerUrl: String) {

        val bitmap = repo.downloadBitmap(stickerUrl)
//        val bitmapString = bitmap?.let { bitmapToString(it) }
        val customModel = bitmap?.let { TempModel(0, it) }
        repo.saveTempStickerToDB(customModel!!)
    }

    suspend fun getTempSticker(): Bitmap {
        val data = repo.getTempStickerFromRoom()
//        val bitmap = stringToBitmap(data.stickerUrl)
        return data.stickerUrl
    }

    fun stringToBitmap(encodedString: String): Bitmap? {
        val decodedBytes = Base64.decode(encodedString, Base64.DEFAULT)
        return if (decodedBytes.isNotEmpty()) {
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } else {
            null
        }
    }

    private fun bitmapToString(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun getAllStickers() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getAllStickerss.collect { all ->
                _AllSticker.postValue(all)
            }
        }
    }

    private val _singleCard = SingleLiveEvent<LocalCardDetailsModel>()
    val singleCard: LiveData<LocalCardDetailsModel> = _singleCard


    suspend fun getSingleCardDesign(category: String, docId: String) {
        repo.getSingleCardDetailsLocal(category, docId).collect {
            _singleCard.postValue(it)
        }
    }

    /*  fun saveGreetingToWeddingCollection(dataList: LocalCardDetailsModel) {
          viewModelScope.launch(Dispatchers.IO) {
              repo.saveGreetingToWeddingCollection(dataList)
          }
      }*/

//    val stickers: LiveData<List<Hit>>
//        get() = repo.stickers

    // List to store references to dynamically created views
    val dynamicViewsList = mutableListOf<View>()
    private val initialPositionsListOfAllViews =
        mutableListOf<MutableMap<View, Pair<Float, Float>>>()
    private val initialPositions = mutableMapOf<View, Pair<Float, Float>>()
    private val updatedPositions = mutableMapOf<View, Pair<Float, Float>>()

    // Stack to keep track of the order of changes
    private val changeOrderStack = Stack<View>()

    // Stack to keep track of views that have been undone
    private val redoStack = Stack<View>()


    fun addDynamicView(view: View) {
        dynamicViewsList.add(view)
    }

    fun storeInitialPosition(view: View) {
        initialPositions[view] = Pair(view.x, view.y)
        // Clone the current initialPositions map to store in the list
        val clonedMap = HashMap<View, Pair<Float, Float>>(initialPositions)
        initialPositionsListOfAllViews.add(clonedMap)
    }

    fun undoToInitialPosition(view: View) {
        storeUpdatedPosition(view)
        addToRedoStack(view)

        /*     // Get the last stored initial positions map
             val lastInitialPositions = initialPositionsList.lastOrNull()


             lastInitialPositions?.let { initialPositionsMap ->
                 initialPositionsMap[view]?.let { (initialX, initialY) ->
                     // Animating the changes for a smoother transition
                     view.animate().x(initialX).y(initialY).setDuration(300).start()
                 }
             }*/
        initialPositions[view]?.let { (initialX, initialY) ->
            // Animating the changes for a smoother transition
            view.animate().x(initialX).y(initialY).setDuration(300).start()
        }
    }

    fun addChangeToOrder(view: View) {
        changeOrderStack.push(view)


        for (element in changeOrderStack) {

        }

    }

    fun popLastChangeOrder(): View? {
        return if (changeOrderStack.isNotEmpty()) {
            changeOrderStack.pop()
        } else {
            null
        }
    }

    //Redo
    private fun addToRedoStack(view: View) {
        redoStack.push(view)
    }

    private fun storeUpdatedPosition(view: View) {
        updatedPositions[view] = Pair(view.x, view.y)
    }

    fun popLastRedo(): View? {
        return if (redoStack.isNotEmpty()) {
            redoStack.pop()
        } else {
            null
        }
    }


    fun resetAllViewsToInitialPosition(views: List<View>) {
        for (view in views) {
            undoToInitialPosition(view)
        }
    }

    fun clearRedoStack() {
        redoStack.clear()
    }

    fun redoToInitialPosition(view: View) {
        addChangeToOrder(view)
        updatedPositions[view]?.let { (initialX, initialY) ->
            // Animating the changes for a smoother transition
            view.animate().x(initialX).y(initialY).setDuration(300).start()
        }
    }


}