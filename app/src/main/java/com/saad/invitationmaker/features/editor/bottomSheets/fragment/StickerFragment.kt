package com.saad.invitationmaker.features.editor.bottomSheets.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.saad.invitationmaker.databinding.FragmentStickerBinding
import com.saad.invitationmaker.features.editor.adapters.StickerAdapter
import com.saad.invitationmaker.features.editor.callbacks.StickerCallBack


class StickerFragment(
    private val urls: List<String>,
    private val getSticker: (url: String) -> Unit,
) : Fragment() {
    private lateinit var binding: FragmentStickerBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StickerAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentStickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        recyclerView = binding.verticalRecyclerView

        adapter = StickerAdapter(
            urls,
            object : StickerCallBack {
                override fun onStickerClick(text: String) {
                    getSticker(text)
                }
            })

        val gridLayoutManager = GridLayoutManager(activity, 4)
//        gridLayoutManager.orientation = GridLayoutManager.HORIZONTAL // Corrected this line
        recyclerView.adapter = adapter
        recyclerView.layoutManager = gridLayoutManager

        //To block scroll of all parents
        /*   recyclerView.addOnItemTouchListener(object : OnItemTouchListener {
               override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                   rv.parent.requestDisallowInterceptTouchEvent(true)
                   return false
               }

               override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
               override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
           })*/
    }
}