package com.saad.invitationmaker.features.editor

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saad.invitationmaker.core.network.repo.Repo
import com.saad.invitationmaker.features.editor.models.stickers.Data
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditorViewModel @Inject constructor(private val repo: Repo) : ViewModel() {
    init {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getSticker("dummy")
        }
    }

    val stickers: LiveData<List<Data>?>
        get() = repo.stickers
}