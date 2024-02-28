package com.saad.invitationmaker.features.editor.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.saad.invitationmaker.features.editor.bottomSheets.fragment.StickerFragment
import com.saad.invitationmaker.features.editor.models.CategoryModelSticker

class ViewPagerAdapter(
    activity: FragmentActivity,
    private val urls: List<CategoryModelSticker>,
    private val getSticker: (stickerUrl: String) -> Unit,
) :
    FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = urls.size

    override fun createFragment(position: Int): Fragment {
        val categoryIndex = position % urls.size
        val category = urls.getOrNull(categoryIndex)
        return if (category != null) {
            StickerFragment(urls = category) { getSticker(it) }
        } else {
            Fragment()
        }
    }
}