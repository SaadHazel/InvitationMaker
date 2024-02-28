package com.saad.invitationmaker.features.backgrounds

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saad.invitationmaker.core.repo.Repo
import com.saad.invitationmaker.features.backgrounds.models.CategoryModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BackgroundViewModel @Inject constructor(
    private val repo: Repo,
) : ViewModel() {

    init {
        CoroutineScope(Dispatchers.IO).launch {
            if (repo.isBackgroundDataBaseEmpty()) {
                repo.getAllBackgroundsRemote()
                getAllBackgrounds()
            } else {
                getAllBackgrounds()
            }
        }
    }

    private val _AllBackgrounds = MutableLiveData<List<CategoryModel>>()

    val AllBackgrounds: LiveData<List<CategoryModel>>
        get() = _AllBackgrounds

    private fun getAllBackgrounds() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getAllBackgrounds.collect { all ->
                _AllBackgrounds.postValue(all)
            }
        }
    }
}