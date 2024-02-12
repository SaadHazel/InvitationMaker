package com.saad.invitationmaker.features.editor.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.saad.invitationmaker.app.utils.Utils
import com.saad.invitationmaker.features.editor.bottomSheets.fragment.StickerFragment
import com.saad.invitationmaker.features.editor.models.stickers.SingleStickerUrlList

class ViewPagerAdapter(
    activity: FragmentActivity,
    private val urls: SingleStickerUrlList,
    private val getSticker: (stickerUrl: String) -> Unit,
) :
    FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {

        val premiumUrls = mutableListOf<String>()
        val freemiumUrls = mutableListOf<String>()


        for (url in urls.data) {
            if (url.license == "premium") {
                premiumUrls.add(url.url)
            } else {
                freemiumUrls.add(url.url)
            }
        }

        val getPremiumUrls: List<String> = premiumUrls.toList()
        return if (position == 0 && premiumUrls.isNotEmpty()) {
            Utils.log("ViewPagerLog: Premium URLs found. Position: $premiumUrls")
            StickerFragment(getPremiumUrls) { sticker ->
                getSticker(sticker)
            }
        } else
            StickerFragment(freemiumUrls) { sticker ->
                getSticker(sticker)
            }
    }
}