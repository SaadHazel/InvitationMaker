package com.saad.invitationmaker.features.categories

import androidx.lifecycle.ViewModel
import com.saad.invitationmaker.core.repo.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val repo: Repo,
) : ViewModel() {
    suspend fun getSingleCardDesign(category: String, docId: String) {
        repo.saveSingleCardLocally(category, docId)
    }
}