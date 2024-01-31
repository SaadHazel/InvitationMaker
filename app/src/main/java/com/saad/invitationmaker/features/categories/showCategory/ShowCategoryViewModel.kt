package com.saad.invitationmaker.features.categories.showCategory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saad.invitationmaker.core.network.models.Hit
import com.saad.invitationmaker.core.network.repo.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowCategoryViewModel @Inject constructor(
    private val repo: Repo,
) : ViewModel() {
    
    val designs: LiveData<List<Hit>>
        get() = repo.designs

    fun getDesigns(category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.networkCheck(category)
        }
    }
}