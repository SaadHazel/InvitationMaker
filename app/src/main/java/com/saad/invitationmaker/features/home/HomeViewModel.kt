package com.saad.invitationmaker.features.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.saad.invitationmaker.core.repo.Repo
import com.saad.invitationmaker.features.home.models.AllCardsDesigns
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: Repo,
) : ViewModel() {
    init {
        CoroutineScope(Dispatchers.IO).launch {
            if (repo.isDataBaseEmpty()) {
                fetchAllCardsFromRemote()
            }
            fetchAllCardsFromLocal()
        }
    }

    private val _cards = MutableLiveData<List<AllCardsDesigns>>()
    val cards: LiveData<List<AllCardsDesigns>> = _cards

    suspend fun getSingleCardDesign(category: String, docId: String) {
        repo.saveSingleCardLocally(category, docId)
    }


    private suspend fun fetchAllCardsFromLocal() {
        repo.getAllDataFromLocal.collect { cards ->
            _cards.postValue(cards)
        }
    }

    private suspend fun fetchAllCardsFromRemote() {
        repo.getAllDataFromFireStore()
    }
}