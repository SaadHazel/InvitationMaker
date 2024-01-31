package com.saad.invitationmaker.features.categories.showCategory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import com.saad.invitationmaker.app.utils.Utils
import com.saad.invitationmaker.databinding.FragmentShowCategoriesBinding
import com.saad.invitationmaker.features.categories.adapters.CategoryShowAdapter
import com.saad.invitationmaker.features.categories.mappers.toMyData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowCategoriesFragment(
    private val category: String,
    private val parentLifeCycleOwner: LifecycleOwner,
) : Fragment() {
    private lateinit var binding: FragmentShowCategoriesBinding
    private val viewModel: ShowCategoryViewModel by viewModels()
    private lateinit var adapter: CategoryShowAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentShowCategoriesBinding.inflate(inflater, container, false)
        adapter = CategoryShowAdapter(ArrayList()) {}
        binding.recyclerView.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchableString = category.replace(" ", "+")
        viewModel.getDesigns(searchableString)

        viewModel.designs.observe(parentLifeCycleOwner) { data ->
            requireActivity().runOnUiThread {
                adapter.setData(data.toMyData().images)
                Utils.log("Data ${data.size}")
            }
        }
    }

    //Send design to editorFragment
    /*  private fun onListItemClick(background: String) {
          //Send this to editor fragment
      }*/

    override fun onPause() {
        super.onPause()
        Utils.log("Fragment onPause")
    }

    override fun onStop() {
        super.onStop()
        Utils.log("Fragment onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Utils.log("View Destroyed")
    }

    override fun onDestroy() {
        super.onDestroy()
        Utils.log("Fragment Destroyed")
    }

    override fun onDetach() {
        super.onDetach()
        Utils.log("Fragment onDetach")
    }
}