package com.saad.invitationmaker.features.home.greetings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.saad.invitationmaker.app.utils.invitationsList
import com.saad.invitationmaker.databinding.FragmentGreetingsBinding
import com.saad.invitationmaker.features.home.adapters.InvitationAdapter

class GreetingsFragment : Fragment() {
    private lateinit var binding: FragmentGreetingsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentGreetingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val shuffledInvitationsList = invitationsList.shuffled()
        val recyclerView: RecyclerView = binding.recyclerView
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        val adapter = InvitationAdapter(shuffledInvitationsList) { category, position ->
            onListItemClick(category, position)
        }
        recyclerView.adapter = adapter
    }

    private fun onListItemClick(item: String, position: Int) {
        /*  navigate(
              R.id.nav_host_fragment,
              InvitationsFragmentDirections.toCategoriesFragment(*//*category = item*//*)
        )*/

    }
}