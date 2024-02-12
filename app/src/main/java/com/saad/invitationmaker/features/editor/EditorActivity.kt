package com.saad.invitationmaker.features.editor

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.saad.invitationmaker.R
import com.saad.invitationmaker.app.utils.Utils
import com.saad.invitationmaker.databinding.ActivityEditorBinding
import com.saad.invitationmaker.features.editor.adapters.MainOptionsAdapter
import com.saad.invitationmaker.features.editor.bottomSheets.NeonTextBottomSheet
import com.saad.invitationmaker.features.editor.bottomSheets.StickerBottomSheet
import com.saad.invitationmaker.features.editor.callbacks.MainEditorOptionsItemClick
import com.saad.invitationmaker.features.editor.mappers.toMyData
import com.saad.invitationmaker.features.editor.models.MainOptionsData
import com.saad.invitationmaker.features.editor.models.ViewsData
import com.saad.invitationmaker.features.editor.models.stickers.SingleStickerUrlList
import com.saad.invitationmaker.features.editor.touchListners.CornerIconListener
import com.saad.invitationmaker.features.editor.touchListners.DraggableImageView
import com.saad.invitationmaker.features.editor.touchListners.DraggableTextView
import com.saad.invitationmaker.features.editor.utils.CreateViews
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditorBinding
    private val viewModel: EditorViewModel by viewModels()
    private lateinit var editorContainer: ConstraintLayout
    private lateinit var mainOptionsRecyclerView: RecyclerView
    private lateinit var mainOptionsAdapter: MainOptionsAdapter
    private lateinit var createViews: CreateViews
    private lateinit var views: List<ViewsData>
    private lateinit var bottomOptionsData: List<MainOptionsData>
    private lateinit var draggableTextView: DraggableTextView
    private lateinit var draggableImageView: DraggableImageView
    private var cornerIconSize: Int = 0
    private lateinit var bottomRightCornerIcon: CornerIconListener
    private lateinit var bottomLeftCornerIcon: CornerIconListener
    private lateinit var topRightCornerIcon: CornerIconListener
    private lateinit var topLeftCornerIcon: CornerIconListener
    private lateinit var addNeonText: NeonTextBottomSheet
    private lateinit var addSticker: StickerBottomSheet
    private var listOfUrls: SingleStickerUrlList? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

        viewModel.stickers.observe(this) { data ->
            if (data != null) {
                listOfUrls = data.toMyData()
                Utils.log("LiveData: $listOfUrls")
                addSticker.updateData(listOfUrls!!)
            } else {
                Utils.log("LiveData is null")
            }
        }

        creatingCornerIcons()

        creatingViews()

    }


    private fun creatingCornerIcons() {
        bottomRightCornerIcon = CornerIconListener(this, cornerIconSize, R.drawable.dot)
        topRightCornerIcon = CornerIconListener(this, cornerIconSize, R.drawable.dot)
        topLeftCornerIcon = CornerIconListener(this, cornerIconSize, R.drawable.dot)
        bottomLeftCornerIcon = CornerIconListener(this, cornerIconSize, R.drawable.dot)

        //Adding views to Layout
        editorContainer.addView(bottomRightCornerIcon)
        editorContainer.addView(topRightCornerIcon)
        editorContainer.addView(topLeftCornerIcon)
        editorContainer.addView(bottomLeftCornerIcon)
    }

    private fun init() = binding.apply {
        createViews = CreateViews(this@EditorActivity)
        editorContainer = binding.editorLayout
        addSticker = StickerBottomSheet { stickerUrl ->
            makingViewsDraggable("image", 200f, 200f, stickerUrl)
        }
        addNeonText = NeonTextBottomSheet { text ->
            makingViewsDraggable("text", 100f, 100f, text)
        }

        cornerIconSize = 40
        bottomOptionsData = listOf(
            MainOptionsData(R.drawable.selected_neon, "ADD TEXT"),
//            MainOptionsData(R.drawable.add_text_s, "ADD TEXT"),
            MainOptionsData(R.drawable.bgremoveicon, "BG REMOVER"),
            MainOptionsData(R.drawable.sticker_s, "STICKER"),
            MainOptionsData(R.drawable.backgrounds_selected, "BACKGROUNDS"),
            MainOptionsData(R.drawable.baseline_add_circle_outline_24, "EFFECTS"),
        )

        views = listOf(
            ViewsData(0, createViews.backgroundDesigns, "text", "First Text", 250f, 200f),
            ViewsData(1, createViews.backgroundDesigns, "text", "Second Text", 300f, 350f),
            ViewsData(2, createViews.backgroundDesigns, "image", createViews.viewImage, 250f, 550f),
            ViewsData(3, createViews.backgroundDesigns, "text", "End Text", 250f, 1200f),
        )

        //setting background of the editor view
        Glide.with(this@EditorActivity).asBitmap().load(createViews.backgroundDesigns).fitCenter()
            .placeholder(R.drawable.ic_launcher_foreground).into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    // Set the loaded bitmap as the background of the CardView
                    val drawable = BitmapDrawable(resources, resource)
                    editorContainer.background = drawable
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // Implement if needed
                }
            })

        mainOptionsRecyclerView = horizontalRecyclerViewMainOptions

        mainOptionsRecyclerView.layoutManager = LinearLayoutManager(
            this@EditorActivity, LinearLayoutManager.HORIZONTAL, false
        )
        mainOptionsAdapter = MainOptionsAdapter(
            bottomOptionsData,
            object : MainEditorOptionsItemClick {
                override fun onItemClick(text: String) {
                    callBackItems(text)
                }

            })
        mainOptionsRecyclerView.adapter = mainOptionsAdapter
    }

    fun callBackItems(text: String) {
        if (text == "ADD TEXT") {
            addNeonText.show(supportFragmentManager, "BSDialogFragment")
        } else if (text == "STICKER") {
            Utils.log("Inside callBackItemsL: $listOfUrls")
            if (listOfUrls != null) {
                addSticker.show(supportFragmentManager, "StickerDialogFragment")
            }
        }
    }

    private fun creatingViews() = binding.apply {
        for (data in views) {
            makingViewsDraggable(data.viewType, data.x, data.y, data.data)
        }
    }

    private fun makingViewsDraggable(viewType: String, x: Float, y: Float, data: String) {
        if (viewType == "text") {
            draggableTextView =
                DraggableTextView(this@EditorActivity, x, y, data)
            editorContainer.addView(draggableTextView)

            draggableTextView.enableDragAndDrop(
                topLeftCornerIcon,
                topRightCornerIcon,
                bottomRightCornerIcon,
                bottomLeftCornerIcon,
            )
            enableCornerListeners(draggableTextView)
        } else if (viewType == "image") {
            draggableImageView =
                DraggableImageView(this@EditorActivity, x, y, data)
            binding.editorLayout.addView(draggableImageView)
            draggableImageView.enableDragAndDrop(
                topLeftCornerIcon,
                topRightCornerIcon,
                bottomRightCornerIcon,
                bottomLeftCornerIcon,
            )
            enableCornerListeners(draggableImageView)
        }
    }
 
    private fun enableCornerListeners(draggableView: View) = binding.apply {
        /* bottomRightCornerIcon.attachTo(draggableTextView, CornerIconListener.Corner.BOTTOM_RIGHT)
         topRightCornerIcon.attachTo(draggableTextView, CornerIconListener.Corner.TOP_RIGHT)
         topLeftCornerIcon.attachTo(draggableTextView, CornerIconListener.Corner.TOP_LEFT)
         bottomLeftCornerIcon.attachTo(draggableTextView, CornerIconListener.Corner.BOTTOM_LEFT)*/

        bottomRightCornerIcon.enableResizeOnTouch(
            draggableView,
            minWidth = 100,
            minHeight = 100,
            maxWidth = 600,
            maxHeight = 600,
        )

        topRightCornerIcon.enableResizeOnTouch(
            draggableView,
            minWidth = 100,
            minHeight = 100,
            maxWidth = 600,
            maxHeight = 600
        )

        topLeftCornerIcon.enableResizeOnTouch(
            draggableView,
            minWidth = 100,
            minHeight = 100,
            maxWidth = 600,
            maxHeight = 600
        )

        bottomLeftCornerIcon.enableResizeOnTouch(
            draggableView,
            minWidth = 100,
            minHeight = 100,
            maxWidth = 600,
            maxHeight = 600
        )
    }

}