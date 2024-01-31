package com.saad.invitationmaker.features.home.invitation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.saad.invitationmaker.app.utils.Utils
import com.saad.invitationmaker.databinding.FragmentInvitationsBinding
import com.saad.invitationmaker.features.home.adapters.InvitationAdapter

class InvitationsFragment(private val onItemClick: (String, Int) -> Unit) : Fragment() {
    private lateinit var binding: FragmentInvitationsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentInvitationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("MyrecyclerView", "getItemCount: Inside my fragment")

        val recyclerView: RecyclerView = binding.recyclerView
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        val adapter = InvitationAdapter(Utils.invitationsList) { category, position ->
            onListItemClick(category, position)
            Utils.log("Got the category $category")
        }
        recyclerView.adapter = adapter
    }

    private fun onListItemClick(item: String, position: Int) {
        onItemClick.invoke(item, position)
    }
}