package com.saad.invitationmaker.features.categories.showCategory

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import com.saad.invitationmaker.databinding.FragmentShowCategoriesBinding
import com.saad.invitationmaker.features.backgrounds.models.CategoryModel
import com.saad.invitationmaker.features.categories.CategoriesViewModel
import com.saad.invitationmaker.features.categories.adapters.CategoryShowAdapter
import com.saad.invitationmaker.features.categories.mappers.toMyData
import com.saad.invitationmaker.features.editor.EditorActivity
import com.saad.invitationmaker.features.home.models.AllCardsDesigns
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class ShowCategoriesFragment(
    private val category: CategoryModel? = null,
    private val homeDataList: List<AllCardsDesigns>? = null,
    private val listOfString: MutableList<String> = mutableListOf(),
    private val parentLifeCycleOwner: LifecycleOwner,
) : Fragment() {
    private var binding: FragmentShowCategoriesBinding? = null
    private var adapter: CategoryShowAdapter? = null
    private val viewModel: CategoriesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentShowCategoriesBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (category != null) {
            adapter = category.hit.toMyData().images.let {
                CategoryShowAdapter(itemList = it) { url, category ->
                    val i = Intent(activity, EditorActivity::class.java)
                    i.putExtra("url", url)
                    startActivity(i)
                    (activity as Activity?)!!.overridePendingTransition(0, 0)
                }
            }
        } else {
            if (homeDataList != null) {
                for (data in homeDataList) {
                    listOfString.add(data.thumbnail)
                }
            }
            Log.d("ShowCategoryList", "list: $listOfString")
            adapter = CategoryShowAdapter(homeDataList = homeDataList) { category, docId ->
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.getSingleCardDesign(category, docId!!)
                    withContext(Dispatchers.Main) {
                        val i = Intent(activity, EditorActivity::class.java)
                        i.putExtra("docId", docId)
                        i.putExtra("category", category)
                        startActivity(i)
                        (activity as Activity?)!!.overridePendingTransition(0, 0)
                    }
                }
            }
        }

        binding?.recyclerView?.adapter = adapter
    }
}