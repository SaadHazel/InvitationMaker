package com.saad.invitationmaker.features.editor


//import dev.eren.removebg.RemoveBg
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.slider.Slider
import com.saad.invitationmaker.R
import com.saad.invitationmaker.app.utils.log
import com.saad.invitationmaker.core.extensions.gone
import com.saad.invitationmaker.core.extensions.inflateAndGone
import com.saad.invitationmaker.core.extensions.visible
import com.saad.invitationmaker.core.network.models.Hit
import com.saad.invitationmaker.databinding.ActivityEditorBinding
import com.saad.invitationmaker.databinding.AlignLayoutViewBinding
import com.saad.invitationmaker.databinding.ColorLayoutViewBinding
import com.saad.invitationmaker.databinding.ColorLayoutViewImageBinding
import com.saad.invitationmaker.databinding.ControlLayoutViewImageBinding
import com.saad.invitationmaker.databinding.ControlsLayoutViewBinding
import com.saad.invitationmaker.databinding.DRotateLayoutViewBinding
import com.saad.invitationmaker.databinding.FontsLayoutViewBinding
import com.saad.invitationmaker.databinding.OpacityLayoutViewBinding
import com.saad.invitationmaker.databinding.OpacityLayoutViewImageBinding
import com.saad.invitationmaker.databinding.RotateLayoutViewBinding
import com.saad.invitationmaker.databinding.RotateLayoutViewImageBinding
import com.saad.invitationmaker.databinding.ShadowAngleLayoutViewBinding
import com.saad.invitationmaker.databinding.ShadowColorLayoutViewBinding
import com.saad.invitationmaker.databinding.ShadowOpacityLayoutViewBinding
import com.saad.invitationmaker.databinding.ShadowSingleSliderViewBinding
import com.saad.invitationmaker.databinding.SizeLayoutItemBinding
import com.saad.invitationmaker.databinding.SizeLayoutViewImageBinding
import com.saad.invitationmaker.databinding.SpacerLayoutViewBinding
import com.saad.invitationmaker.databinding.StyleLayoutItemBinding
import com.saad.invitationmaker.features.backgrounds.callbacks.BackgroundCallBack
import com.saad.invitationmaker.features.backgrounds.models.CategoryModel
import com.saad.invitationmaker.features.editor.adapters.BackgroundsAdapter
import com.saad.invitationmaker.features.editor.adapters.ColorsAdapter
import com.saad.invitationmaker.features.editor.adapters.FontsAdapter
import com.saad.invitationmaker.features.editor.adapters.LayersAdapter
import com.saad.invitationmaker.features.editor.adapters.MainOptionsAdapter
import com.saad.invitationmaker.features.editor.adapters.ShadowAdapter
import com.saad.invitationmaker.features.editor.bottomSheets.EditTextBottomSheet
import com.saad.invitationmaker.features.editor.bottomSheets.NeonTextBottomSheet
import com.saad.invitationmaker.features.editor.bottomSheets.SaveBottomSheet
import com.saad.invitationmaker.features.editor.bottomSheets.StickerBottomSheet
import com.saad.invitationmaker.features.editor.callbacks.ExitConfirmationDialogListener
import com.saad.invitationmaker.features.editor.callbacks.ItemColorCallBack
import com.saad.invitationmaker.features.editor.callbacks.ItemFontClickCallback
import com.saad.invitationmaker.features.editor.callbacks.ItemShadowClickCallback
import com.saad.invitationmaker.features.editor.callbacks.ItemTouchHelperAdapter
import com.saad.invitationmaker.features.editor.callbacks.ItemTouchHelperCallback
import com.saad.invitationmaker.features.editor.callbacks.MainEditorOptionsItemClick
import com.saad.invitationmaker.features.editor.callbacks.UpdateTouchListenerCallback
import com.saad.invitationmaker.features.editor.dialogs.ExitDialog
import com.saad.invitationmaker.features.editor.models.CategoryModelSticker
import com.saad.invitationmaker.features.editor.models.Fonts
import com.saad.invitationmaker.features.editor.models.LayersModel
import com.saad.invitationmaker.features.editor.models.MainOptionsData
import com.saad.invitationmaker.features.editor.models.ViewsData
import com.saad.invitationmaker.features.editor.touchListners.CornerIconListener
import com.saad.invitationmaker.features.editor.touchListners.DraggableImageView
import com.saad.invitationmaker.features.editor.touchListners.DraggableTextView
import com.saad.invitationmaker.features.editor.utils.CreateViews
import com.saad.invitationmaker.features.home.models.AllViews
import com.saad.invitationmaker.features.home.models.DraftAllViews
import com.skydoves.colorpickerview.listeners.ColorListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class EditorActivity : AppCompatActivity(), ItemTouchHelperAdapter, UpdateTouchListenerCallback {
    private val tag = "EDITORACTIVITYTAG"
    val allview = AllViews()
    private lateinit var binding: ActivityEditorBinding
    private val editedViews: MutableList<DraftAllViews> = mutableListOf()
    private var controlLayoutView: ControlsLayoutViewBinding? = null
    private var alignLayoutView: AlignLayoutViewBinding? = null
    private var dRotateLayoutView: DRotateLayoutViewBinding? = null
    private var rotateLayoutView: RotateLayoutViewBinding? = null
    private var spacerLayoutView: SpacerLayoutViewBinding? = null
    private var opacityLayoutView: OpacityLayoutViewBinding? = null
    private var styleLayoutView: StyleLayoutItemBinding? = null
    private var sizeLayoutView: SizeLayoutItemBinding? = null
    private var fontLayoutView: FontsLayoutViewBinding? = null
    private var colorLayoutView: ColorLayoutViewBinding? = null
    private var shadowAngleLayoutView: ShadowAngleLayoutViewBinding? = null
    private var shadowSingleSliderLayoutView: ShadowSingleSliderViewBinding? = null
    private var shadowColorLayoutView: ShadowColorLayoutViewBinding? = null
    private var shadowOpacityLayoutView: ShadowOpacityLayoutViewBinding? = null

    //Image Stubs
    private var controlLayoutViewImage: ControlLayoutViewImageBinding? = null
    private var sizeLayoutViewImage: SizeLayoutViewImageBinding? = null
    private var colorLayoutViewImage: ColorLayoutViewImageBinding? = null
    private var opacityLayoutViewImage: OpacityLayoutViewImageBinding? = null
    private var rotateLayoutViewImage: RotateLayoutViewImageBinding? = null


    private val viewModel: EditorViewModel by viewModels()
    private lateinit var editorContainer: ConstraintLayout
    private lateinit var mainOptionsRecyclerView: RecyclerView
    private lateinit var selectedOptionsRecyclerView: RecyclerView
    private var recyclerViewItemsImage: RecyclerView? = null
    private lateinit var mainOptionsAdapter: MainOptionsAdapter
    private lateinit var selectedAdapterOptions: MainOptionsAdapter
    private lateinit var createViews: CreateViews
    private lateinit var views: List<ViewsData>
    private lateinit var bottomOptionsData: List<MainOptionsData>
    private lateinit var bottomSelectedOptionsData: List<MainOptionsData>
    private lateinit var bottomSelectedOptionsDataImage: List<MainOptionsData>
    private lateinit var fontsList: List<Fonts>
    private lateinit var colorList: List<String>
    private lateinit var shadowList: List<String>
    private var draggableTextView: DraggableTextView? = null
    private lateinit var draggableImageView: DraggableImageView
    private var cornerIconSize: Int = 0
    private lateinit var bottomRightCornerIcon: CornerIconListener
    private lateinit var bottomLeftCornerIcon: CornerIconListener
    private lateinit var topRightCornerIcon: CornerIconListener
    private lateinit var topLeftCornerIcon: CornerIconListener
    private lateinit var addNeonText: NeonTextBottomSheet
    private var saveDesign: SaveBottomSheet? = null
    private var addSticker: StickerBottomSheet? = null

    private var listOfUrls: List<CategoryModelSticker>? = null

    private var listOfHits: List<Hit>? = null

    private var viewStub: ViewStub? = null
    private var viewStubAlign: ViewStub? = null
    private var viewStubDRotate: ViewStub? = null
    private var viewStubRotate: ViewStub? = null
    private var viewStubSpacer: ViewStub? = null
    private var viewStubOpacity: ViewStub? = null
    private var viewStubStyle: ViewStub? = null
    private var viewStubSize: ViewStub? = null
    private var viewStubFont: ViewStub? = null
    private var viewStubColor: ViewStub? = null
    private var viewStubShadowAngle: ViewStub? = null
    private var viewStubShadowSingleSlider: ViewStub? = null
    private var viewStubColorShadow: ViewStub? = null
    private var viewStubOpacityShadow: ViewStub? = null

    //Image ViewStubs
    private var viewStubControlImage: ViewStub? = null
    private var viewStubSizeImage: ViewStub? = null
    private var viewStubColorImage: ViewStub? = null
    private var viewStubOpacityImage: ViewStub? = null
    private var viewStubRotateImage: ViewStub? = null

    //All editing values
    /*  private var angleX: Float = 0f
      private var angleY: Float = 0f
      private var blurVal: Float = 1f*/
    private var viewData: String? = null
    private var viewId: String? = null
    private var alignment: String? = null
    private var opacity: String? = null
    private var rotation: String? = null
    private var rotationX: String? = 0.0f.toString()
    private var rotationY: String? = 0.0f.toString()
    private var dRotationX: String? = 0.0f.toString()
    private var dRotationY: String? = 0.0f.toString()
    private var shadowAngleX: Float = 0f
    private var shadowAngleY: Float = 0f
    private var shadowBlur: Float = 1f
    private var shadowColor: String = "#000000"
    var viewType: String? = null
    private var fontSize: String? = null
    var font: String? = null
    private var letterSpacing: String? = null
    private var lineHeight: String? = null
    var textStyle: String? = null
    var width: String? = null
    var height: String? = null
    var priority: String? = null


    private var currentView: View? = null

    private var currentViewColor: String? = null
    private var currentY: Float? = null
    private var currentX: Float? = null
    private val layerList = mutableListOf<LayersModel>()
    private val adapter: LayersAdapter by lazy {
        LayersAdapter(
            layerList, this,
        )
    }
    private var isRecyclerViewVisible = false
    private var boolTopBar = false
    private lateinit var itemTouchHelper: ItemTouchHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.AllBackgrounds.observe(this@EditorActivity) {
            listOfHits = it
        }
        viewModel.AllSticker.observe(this) { data ->
            log(tag, "Inside Observer $data")
            if (data != null) {
                listOfUrls = data
                log(tag, "Stickers $listOfUrls")
                addSticker?.updateData(listOfUrls!!)
            }
        }

        val extras = intent.extras
        val url: String? = extras?.getString("url")
        val imagePath: ByteArray? = intent.getByteArrayExtra("imagePath")
        var imagePathGallery: String? = null
        if (url == null && imagePath == null) {
            imagePathGallery = extras?.getString("imagePathGallery")
        }

        Log.d("ImageFromPrev", "$imagePath")
        if (extras != null && url == null && imagePath == null && imagePathGallery == null) {
            val value = extras.getString("docId")
            val category = extras.getString("category")
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.getSingleCardDesign(category!!, value!!)
            }
        }



        binding.stub.setOnInflateListener { _, inflateId ->
            controlLayoutView = ControlsLayoutViewBinding.bind(inflateId)
        }

        binding.stubAlign.setOnInflateListener { _, inflateId ->
            alignLayoutView = AlignLayoutViewBinding.bind(inflateId)
        }
        binding.stubDRotate.setOnInflateListener { _, inflateId ->
            dRotateLayoutView = DRotateLayoutViewBinding.bind(inflateId)
        }

        binding.stubRotate.setOnInflateListener { _, inflateId ->
            rotateLayoutView = RotateLayoutViewBinding.bind(inflateId)
        }

        binding.stubSpacer.setOnInflateListener { _, inflated ->
            spacerLayoutView = SpacerLayoutViewBinding.bind(inflated)
        }

        binding.stubOpacity.setOnInflateListener { _, inflated ->
            opacityLayoutView = OpacityLayoutViewBinding.bind(inflated)
        }

        binding.stubStyle.setOnInflateListener { _, inflated ->
            styleLayoutView = StyleLayoutItemBinding.bind(inflated)
        }

        binding.stubSize.setOnInflateListener { _, inflated ->
            sizeLayoutView = SizeLayoutItemBinding.bind(inflated)
        }

        binding.stubFont.setOnInflateListener { _, inflated ->
            fontLayoutView = FontsLayoutViewBinding.bind(inflated)
        }

        binding.stubColor.setOnInflateListener { _, inflated ->
            colorLayoutView = ColorLayoutViewBinding.bind(inflated)
        }

        binding.stubShadowAngle.setOnInflateListener { _, inflated ->
            shadowAngleLayoutView = ShadowAngleLayoutViewBinding.bind(inflated)
        }

        binding.stubShadowSingleSlider.setOnInflateListener { _, inflated ->
            shadowSingleSliderLayoutView = ShadowSingleSliderViewBinding.bind(inflated)
        }

        binding.stubColorShadow.setOnInflateListener { _, inflated ->
            shadowColorLayoutView = ShadowColorLayoutViewBinding.bind(inflated)
        }

        binding.stubOpacityShadow.setOnInflateListener { _, inflated ->
            shadowOpacityLayoutView = ShadowOpacityLayoutViewBinding.bind(inflated)
        }
//Image stubs
        binding.stubControlImage.setOnInflateListener { _, inflated ->
            controlLayoutViewImage = ControlLayoutViewImageBinding.bind(inflated)
        }

        binding.stubSizeImage.setOnInflateListener { _, inflated ->
            sizeLayoutViewImage = SizeLayoutViewImageBinding.bind(inflated)
        }

        binding.stubColorImage.setOnInflateListener { _, inflated ->
            colorLayoutViewImage = ColorLayoutViewImageBinding.bind(inflated)
        }

        binding.stubOpacityImage.setOnInflateListener { _, inflated ->
            opacityLayoutViewImage = OpacityLayoutViewImageBinding.bind(inflated)

        }

        binding.stubRotateImage.setOnInflateListener { _, inflated ->
            rotateLayoutViewImage = RotateLayoutViewImageBinding.bind(inflated)
        }

        init()
        setData()
//        creatingCornerIcons()
        saveDesign()
//        creatingViews()

    }

    private fun saveDesign() = binding.apply {
        saveDesign = SaveBottomSheet(editorContainer)
        btnSave.setOnClickListener {
            saveDesign!!.show(supportFragmentManager, "BSSaveDesignFragment")
        }
    }

    // Method to add or update edited views
    private fun updateView() {
        val view = DraftAllViews(
            viewId = viewId,
            viewData = viewData,
            alignment = alignment,
            opacity = opacity,
            rotation = rotation,
            rotationX = rotationX,
            rotationY = rotationY,
            dRotationX = dRotationX,
            dRotationY = dRotationY,
            shadowAngleX = shadowAngleX,
            shadowAngleY = shadowAngleY,
            shadowBlur = shadowBlur,
            shadowColor = shadowColor.toString(), // Converting Int to String
            viewType = viewType,
            fontSize = fontSize,
            font = font,
            letterSpacing = letterSpacing,
            lineHeight = lineHeight,
            textStyle = textStyle,
            color = currentViewColor,
            xCoordinate = currentX.toString(),
            yCoordinate = currentY.toString(),
            width = width,
            height = height,
            priority = priority
        )

        val existingView = editedViews.find { it.viewId == view.viewId }

        if (existingView != null) {
            existingView.apply {
                viewId = view.viewId ?: this.viewId
                viewData = view.viewData ?: this.viewData
                alignment = view.alignment ?: this.alignment
                opacity = view.opacity ?: this.opacity
                rotation = view.rotation ?: this.rotation
                rotationX = view.rotationX ?: this.rotationX
                rotationY = view.rotationY ?: this.rotationY
                dRotationX = view.dRotationX ?: this.dRotationX
                dRotationY = view.dRotationY ?: this.dRotationY
                shadowAngleX = view.shadowAngleX ?: this.shadowAngleX
                shadowAngleY = view.shadowAngleY ?: this.shadowAngleY
                shadowBlur = view.shadowBlur ?: this.shadowBlur
                shadowColor = view.shadowColor ?: this.shadowColor
                viewType = view.viewType ?: this.viewType
                fontSize = view.fontSize ?: this.fontSize
                font = view.font ?: this.font
                letterSpacing = view.letterSpacing ?: this.letterSpacing
                lineHeight = view.lineHeight ?: this.lineHeight
                textStyle = view.textStyle ?: this.textStyle
                color = view.color ?: this.color
                xCoordinate = view.xCoordinate ?: this.xCoordinate
                yCoordinate = view.yCoordinate ?: this.yCoordinate
                width = view.width ?: this.width
                height = view.height ?: this.height
                priority = view.priority ?: this.priority
            }
        } else {
            editedViews.add(view)
        }
        log(tag, "EditedviewListSize - ${editedViews.size}")
        editedViews.forEach { editedView ->
            if (editedView.viewId == viewId) {
                with(editedView) {
                    Log.d(tag, "View ID: $viewId")
                    Log.d(tag, "View Data: $viewData")
                    Log.d(tag, "Alignment: $alignment")
                    Log.d(tag, "Opacity: $opacity")
                    Log.d(tag, "Rotation: $rotation")
                    Log.d(tag, "Rotation X: $rotationX")
                    Log.d(tag, "Rotation Y: $rotationY")
                    Log.d(tag, "Delta Rotation X: $dRotationX")
                    Log.d(tag, "Delta Rotation Y: $dRotationY")
                    Log.d(tag, "Shadow Angle X: $shadowAngleX")
                    Log.d(tag, "Shadow Angle Y: $shadowAngleY")
                    Log.d(tag, "Shadow Blur: $shadowBlur")
                    Log.d(tag, "Shadow Color: $shadowColor")
                    Log.d(tag, "View Type: $viewType")
                    Log.d(tag, "Font Size: $fontSize")
                    Log.d(tag, "Font: $font")
                    Log.d(tag, "Letter Spacing: $letterSpacing")
                    Log.d(tag, "Line Height: $lineHeight")
                    Log.d(tag, "Text Style: $textStyle")
                    Log.d(tag, "Color: $color")
                    Log.d(tag, "X Coordinate: $xCoordinate")
                    Log.d(tag, "Y Coordinate: $yCoordinate")
                    Log.d(tag, "Width: $width")
                    Log.d(tag, "Height: $height")
                    Log.d(tag, "Priority: $priority")
                }
            }
        }
    }

    private fun setData() {

        val extras = intent.extras
        var backgroundUrl: String? = null
        var backgroundGallery: Uri? = null
        var backgroundCamera: Bitmap? = null
        if (intent.hasExtra("imagePath")) {
            backgroundCamera = BitmapFactory.decodeByteArray(
                intent.getByteArrayExtra("imagePath"),
                0,
                intent.getByteArrayExtra("imagePath")!!.size
            )
        } else if (intent.hasExtra("imagePathGallery")) {
            backgroundGallery = Uri.parse(extras?.getString("imagePathGallery"));
        } else if (intent.hasExtra("url")) {
            backgroundUrl = extras?.getString("url")
        }

        if (backgroundUrl != null) {
            Glide.with(this@EditorActivity).asBitmap().load(backgroundUrl).fitCenter()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?,
                    ) {
                        // Set the loaded bitmap as the background of the CardView
                        val drawable = BitmapDrawable(resources, resource)
                        editorContainer.background = drawable
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // Implement if needed
                    }
                })

        } else if (backgroundCamera != null) {

            Log.d("ImageFromPrev", "InGliding $backgroundCamera")
            binding.backgroundImg.visible()
            binding.backgroundImg.setImageBitmap(backgroundCamera)

            /*  Glide.with(this@EditorActivity).asBitmap().load(imagePath).fitCenter()
                  .placeholder(R.drawable.ic_launcher_foreground)
                  .into(object : CustomTarget<Bitmap>() {
                      override fun onResourceReady(
                          resource: Bitmap,
                          transition: Transition<in Bitmap>?,
                      ) {
                          // Set the loaded bitmap as the background of the CardView
                          val drawable = BitmapDrawable(resources, resource)
                          editorContainer.background = drawable
                      }

                      override fun onLoadCleared(placeholder: Drawable?) {
                          // Implement if needed
                      }
                  })*/

        } else if (backgroundGallery != null) {
            Glide.with(this@EditorActivity).asBitmap().load(backgroundGallery).fitCenter()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?,
                    ) {
                        // Set the loaded bitmap as the background of the CardView
                        val drawable = BitmapDrawable(resources, resource)
                        editorContainer.background = drawable
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // Implement if needed
                    }
                })
        } else {
            loaderForData(true)
            viewModel.singleCard.observe(this) { dataList ->

                log(tag, "$dataList")
//                viewModel.saveGreetingToWeddingCollection(dataList)
                if (dataList == null) return@observe

                val background = dataList.background
                //setting background of the editor view
                Glide.with(this@EditorActivity).asBitmap().load(background).override(600, 600)
                    .fitCenter()
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?,
                        ) {
                            // Set the loaded bitmap as the background of the CardView
                            val drawable = BitmapDrawable(resources, resource)
                            editorContainer.background = drawable
                            loaderForData(false)
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                            // Implement if needed
                        }
                    })


                for (data in dataList.views) {
                    val viewId = data.viewId
                    val viewData = data.viewData
                    val alignment = data.alignment
                    val viewType = data.viewType
                    val fontSize = data.fontSize
                    val font = data.font
                    val letterSpacing = data.letterSpacing
                    val lineHeight = data.lineHeight
                    val textStyle = data.textStyle
                    val color = data.color
                    val xCoordinate = data.xCoordinate
                    val yCoordinate = data.yCoordinate
                    val width = data.width
                    val height = data.height
                    val priority = data.priority
                    viewType?.let { viewtype ->
                        xCoordinate?.toFloat()?.let { x ->
                            yCoordinate?.toFloat()?.let { y ->
                                viewData?.let { data ->
                                    priority?.toInt()?.let { priority ->
                                        width?.let { width ->
                                            height?.let { height ->
                                                color?.let { color ->
                                                    fontSize?.let { size ->
                                                        textStyle?.let { style ->
                                                            font?.let { font ->
                                                                alignment?.let { align ->
                                                                    letterSpacing?.let { letterSpace ->
                                                                        viewId?.let {
                                                                            makingViewsDraggable(
                                                                                viewType = viewtype,
                                                                                x = x,
                                                                                y = y,
                                                                                data = data,
                                                                                priority = priority,
                                                                                width = width,
                                                                                height = height,
                                                                                color = color,
                                                                                fontSize = size,
                                                                                textStyle = style,
                                                                                font = font,
                                                                                alignment = align,
                                                                                letterSpacing = letterSpace,
                                                                                viewId = it.toInt()
                                                                            )
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    this@EditorActivity.viewId = viewId
                    this@EditorActivity.viewType = viewType
                    this@EditorActivity.viewData = viewData
                    this@EditorActivity.alignment = alignment
                    this@EditorActivity.fontSize = fontSize
                    this@EditorActivity.font = font
                    this@EditorActivity.letterSpacing = letterSpacing
                    this@EditorActivity.lineHeight = lineHeight
                    this@EditorActivity.textStyle = textStyle
                    this@EditorActivity.currentViewColor = color
                    this@EditorActivity.currentX = xCoordinate?.toFloat()
                    this@EditorActivity.currentY = yCoordinate?.toFloat()
                    this@EditorActivity.width = width
                    this@EditorActivity.height = height
                    this@EditorActivity.priority = priority
                    updateView()

                }

            }
        }

    }

    /*    private fun creatingCornerIcons() {
            bottomRightCornerIcon = CornerIconListener(this, cornerIconSize, R.drawable.dot)
            topRightCornerIcon = CornerIconListener(this, cornerIconSize, R.drawable.dot)
            topLeftCornerIcon = CornerIconListener(this, cornerIconSize, R.drawable.dot)
            bottomLeftCornerIcon = CornerIconListener(this, cornerIconSize, R.drawable.dot)

            //Adding views to Layout
            editorContainer.addView(bottomRightCornerIcon)
            editorContainer.addView(topRightCornerIcon)
            editorContainer.addView(topLeftCornerIcon)
            editorContainer.addView(bottomLeftCornerIcon)
            hideCreatedCornerIcon()
        }

        private fun hideCreatedCornerIcon() {
            bottomRightCornerIcon.invisible()
            topRightCornerIcon.invisible()
            topLeftCornerIcon.invisible()
            bottomLeftCornerIcon.invisible()
        }*/


    private fun init() = binding.apply {
        recyclerViewItemsImage = recyclerViewSelectedOptionsImage
        viewStub = binding.stub.viewStub
        viewStub?.inflateAndGone()

        viewStubAlign = binding.stubAlign.viewStub
        viewStubAlign?.inflateAndGone()

        viewStubDRotate = binding.stubDRotate.viewStub
        viewStubDRotate?.inflateAndGone()

        viewStubRotate = binding.stubRotate.viewStub
        viewStubRotate?.inflateAndGone()

        viewStubSpacer = binding.stubSpacer.viewStub
        viewStubSpacer?.inflateAndGone()

        viewStubOpacity = binding.stubOpacity.viewStub
        viewStubOpacity?.inflateAndGone()

        viewStubStyle = binding.stubStyle.viewStub
        viewStubStyle?.inflateAndGone()

        viewStubSize = binding.stubSize.viewStub
        viewStubSize?.inflateAndGone()


        viewStubFont = binding.stubFont.viewStub
        viewStubFont?.inflateAndGone()

        viewStubColor = binding.stubColor.viewStub
        viewStubColor?.inflateAndGone()

        viewStubShadowAngle = binding.stubShadowAngle.viewStub
        viewStubShadowAngle?.inflateAndGone()

        viewStubShadowSingleSlider = binding.stubShadowSingleSlider.viewStub
        viewStubShadowSingleSlider?.inflateAndGone()

        viewStubColorShadow = binding.stubColorShadow.viewStub
        viewStubColorShadow?.inflateAndGone()

        viewStubOpacityShadow = binding.stubOpacityShadow.viewStub
        viewStubOpacityShadow?.inflateAndGone()

        viewStubControlImage = binding.stubControlImage.viewStub
        viewStubControlImage?.inflateAndGone()

        viewStubSizeImage = binding.stubSizeImage.viewStub
        viewStubSizeImage?.inflateAndGone()

        viewStubColorImage = binding.stubColorImage.viewStub
        viewStubColorImage?.inflateAndGone()

        viewStubOpacityImage = binding.stubOpacityImage.viewStub
        viewStubOpacityImage?.inflateAndGone()

        viewStubRotateImage = binding.stubRotateImage.viewStub
        viewStubRotateImage?.inflateAndGone()

        createViews = CreateViews(this@EditorActivity)
        editorContainer = binding.editorLayout
        addSticker = StickerBottomSheet { stickerUrl ->
//            CoroutineScope(Dispatchers.IO).launch {
//                viewModel.saveTempSticker(stickerUrl)
//                val dataBitmap = viewModel.getTempSticker()

//                withContext(Dispatchers.Main) {
            val randomViewId = (10..30).random()
            makingViewsDraggable(
                "image",
                x = 200f,
                y = 200f,
                data = stickerUrl,
//                        bitmap = dataBitmap,
                priority = 6,
                viewId = randomViewId,
//                        imageType = "BITMAP",
            )

        }
        addNeonText = NeonTextBottomSheet { text ->
            val randomViewId = (30..60).random()
            makingViewsDraggable(
                "text",
                100f,
                100f,
                data = text,
                priority = 7,
                fontSize = "56",
                viewId = randomViewId.toInt()
            )
        }

        cornerIconSize = 40

        bottomSelectedOptionsDataImage = listOf(
            MainOptionsData(R.drawable.bottom_control_selected, "Controls"),
            MainOptionsData(R.drawable.font_size_s, "Size"),
            MainOptionsData(R.drawable.bottom_color_selected, "Color"),
            MainOptionsData(R.drawable.active_opacity_icon, "Opacity"),
            MainOptionsData(R.drawable.rotation_icon, "Rotation"),
        )

        bottomOptionsData = listOf(
            MainOptionsData(R.drawable.selected_neon, "ADD TEXT"),
//            MainOptionsData(R.drawable.add_text_s, "ADD TEXT"),
            MainOptionsData(R.drawable.bgremoveicon, "BG REMOVER"),
            MainOptionsData(R.drawable.sticker_s, "STICKER"),
            MainOptionsData(R.drawable.backgrounds_selected, "BACKGROUNDS"),
            MainOptionsData(R.drawable.baseline_add_circle_outline_24, "EFFECTS"),
        )

        bottomSelectedOptionsData = listOf(
            MainOptionsData(R.drawable.bottom_control_selected, "Controls"),
            MainOptionsData(R.drawable.center_selected, "Align"),
            MainOptionsData(R.drawable.font_s, "Fonts"),
            MainOptionsData(R.drawable.font_size_s, "Size"),
            MainOptionsData(R.drawable.bottom_color_selected, "Color"),
            MainOptionsData(R.drawable.active_text_style_icon, "Style"),
            MainOptionsData(R.drawable.active_shadow_icon, "Shadow"),
            MainOptionsData(R.drawable.active_opacity_icon, "Opacity"),
            MainOptionsData(R.drawable.rotation_icon, "Rotation"),
            MainOptionsData(R.drawable.active_spacing_icon, "Spacing"),
            MainOptionsData(R.drawable.botom_rotation_selected, "3D Rotate"),
        )

        fontsList = listOf(
            Fonts("Roboto", fonts = R.font.roboto_regular),
            Fonts("Kanit", fonts = R.font.kanit_regular),
            Fonts("Bahianita", fonts = R.font.bahianita_regular),
            Fonts("ProtestStrike", fonts = R.font.proteststrike_regular),
            Fonts("ProtestRevolution", fonts = R.font.protestrevolution_regular),
            Fonts("SixtyFour", fonts = R.font.sixtyfour_regular),
            Fonts("Ubuntu", fonts = R.font.ubuntu_regular),
            Fonts("WorkBench", fonts = R.font.workbench_regular),
        )

        shadowList = listOf(
            "OFF",
            "ANGLE",
            "BLUR",
            "COLOR",
            "OPACITY"
        )
        colorList = listOf(
            "#FF000000",     // Black
            "#FFF14E6F",     // Random
            "#FFDBDB",       // Active Indicator Background
            "#FF7A7979",     // Grey
            "#FF393939",     // Daynight Text Color
            "#FFCFCECE",     // Light Grey
            "#FFF64B4B",     // Blue
            "#FFFD5071",     // Color Pink
            "#FFFFFFFF",     // Daynight Text White
            "#FFE5E5E5",     // Daynight Layer Item
            "#FF393939",     // Color Accent
            "#FF1394FA",     // Color Blue
            "#FF000000",     // Black (duplicate)
            "#FFF14E6F",     // Random (duplicate)
            "#FFDBDB",       // Active Indicator Background (duplicate)
            "#FF7A7979",     // Grey (duplicate)
            "#FF393939",     // Daynight Text Color (duplicate)
            "#FFCFCECE",     // Light Grey (duplicate)
            "#FFF64B4B",     // Blue (duplicate)
            "#FFFD5071",     // Color Pink (duplicate)
            "#FFFFFFFF",     // Daynight Text White (duplicate)
            "#FFE5E5E5",     // Daynight Layer Item (duplicate)
            "#FF393939",     // Color Accent (duplicate)
            "#FF1394FA"
        )
        /*

                views = listOf(
                    ViewsData(
                        priority = 0,
                        backgroundDesignUrl = createViews.backgroundDesigns,
                        fontSize = "21",
                        color = "#000000",
                        font = "poppins",
                        height = "32",
                        width = "202",
                        textStyle = "regular",
                        viewType = "text",
                        alignment = "left",
                        letterSpacing = "6",
                        data = "THE WEDDING OFF",
                        x = 342f,
                        y = 369.27f
                    ),
                    ViewsData(
                        1, createViews.backgroundDesigns,
                        fontSize = "120.83",
                        color = "#007357",
                        font = "cormorantSC",
                        height = "146",
                        width = "265",
                        textStyle = "bold",
                        viewType = "text",
                        alignment = "left",
                        letterSpacing = "0",
                        data = "ALEX",
                        x = 274f,
                        y = 369.27f
                    ),
                    ViewsData(
                        2, createViews.backgroundDesigns,
                        fontSize = "120.83",
                        color = "#7D7D7D",
                        font = "cormorantSC",
                        height = "146",
                        width = "67",
                        textStyle = "bold",
                        viewType = "text",
                        alignment = "left",
                        letterSpacing = "0",
                        data = "&",
                        x = 552f,
                        y = 369.27f
                    ),
                    ViewsData(
                        3, createViews.backgroundDesigns,
                        fontSize = "120.83",
                        color = "#007357",
                        font = "cormorantSC",
                        height = "146",
                        width = "259",
                        textStyle = "bold",
                        viewType = "text",
                        alignment = "left",
                        letterSpacing = "0",
                        data = "JULIE",
                        x = 351f,
                        y = 486.27f
                    ),
                    ViewsData(
                        4, createViews.backgroundDesigns,
                        fontSize = "23",
                        color = "#000000",
                        font = "poppins",
                        height = "35",
                        width = "52",
                        textStyle = "regular",
                        viewType = "text",
                        alignment = "left",
                        letterSpacing = "6",
                        data = "AUG",
                        x = 417f,
                        y = 643.27f
                    ),
                    ViewsData(
                        5, createViews.backgroundDesigns,
                        fontSize = "23",
                        color = "#000000",
                        font = "poppins",
                        height = "35",
                        width = "126",
                        textStyle = "regular",
                        viewType = "text",
                        alignment = "left",
                        letterSpacing = "6",
                        data = "SATURDAY",
                        x = 254f,
                        y = 703.27f
                    ),
                    ViewsData(
                        6, createViews.backgroundDesigns,
                        fontSize = "80.5",
                        color = "#373332",
                        font = "poppins",
                        height = "121",
                        width = "84",
                        textStyle = "bold",
                        viewType = "text",
                        alignment = "left",
                        letterSpacing = "6",
                        data = "31",
                        x = 401f,
                        y = 660.27f
                    ),
                    ViewsData(
                        7, createViews.backgroundDesigns,
                        fontSize = "23",
                        color = "#000000",
                        font = "poppins",
                        height = "35",
                        width = "60",
                        textStyle = "regular",
                        viewType = "text",
                        alignment = "left",
                        letterSpacing = "6",
                        data = "2024",
                        x = 417f,
                        y = 763.27f
                    ),
                    ViewsData(
                        8, createViews.backgroundDesigns,
                        fontSize = "23",
                        color = "#000000",
                        font = "poppins",
                        height = "35",
                        width = "97",
                        textStyle = "regular",
                        viewType = "text",
                        alignment = "left",
                        letterSpacing = "6",
                        data = "AT 4 PM",
                        x = 552f,
                        y = 703.27f
                    ),
                    ViewsData(
                        9,
                        createViews.backgroundDesigns,
                        fontSize = "63.69",
                        color = "#000000",
                        font = "poppins",
                        height = "77",
                        width = "369",
                        textStyle = "bold",
                        viewType = "text",
                        alignment = "left",
                        letterSpacing = "0",
                        data = "THE CEREMONY",
                        x = 247f,
                        y = 833.27f
                    ),
                    ViewsData(
                        10,
                        createViews.backgroundDesigns,
                        fontSize = "24",
                        color = "#383433",
                        font = "poppins",
                        height = "36",
                        width = "576",
                        textStyle = "regular",
                        viewType = "text",
                        alignment = "left",
                        letterSpacing = "6",
                        data = "Grand Taj Hotel , Lake View Park, Los Angles",
                        x = 159f,
                        y = 926.27f
                    ),

                    )
        */

        mainOptionsRecyclerView = horizontalRecyclerViewMainOptions
        mainOptionsRecyclerView.visibility = View.VISIBLE

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
        selectedItemOptionsRecyclerView()
        selectedItemOptionsImageRecyclerView()
        setupRecyclerView()
        setupButtonLayers()
        setUpBackButton()
        undoRedoListener()
    }

    private fun selectedItemOptionsImageRecyclerView() = binding.apply {

        recyclerViewSelectedOptionsImage.layoutManager = LinearLayoutManager(
            this@EditorActivity, LinearLayoutManager.HORIZONTAL, false
        )

        recyclerViewSelectedOptionsImage.adapter = MainOptionsAdapter(
            bottomSelectedOptionsDataImage,
            object : MainEditorOptionsItemClick {
                override fun onItemClick(text: String) {
                    callBackSelectedItemImage(text)
                }
            })

        selectedOptionsRecyclerView.adapter = selectedAdapterOptions
    }

    private fun callBackSelectedItemImage(text: String) {
        when (text) {
            "Controls" -> {
                log(tag, "Control Image")
                viewStubControlImage?.let { hideAllStubs(it.id) }
                controlLayoutLogicImage()
            }

            "Opacity" -> {
                log(tag, "Opacity Image")
                viewStubOpacityImage?.let { hideAllStubs(it.id) }
                opacityLogicImage()
            }

            "Size" -> {
                log(tag, "Size Image")
                viewStubSizeImage?.let { hideAllStubs(it.id) }
                sizeLogicImage()
            }

            "Color" -> {
                log(tag, "Color Image")
                viewStubColorImage?.let { hideAllStubs(it.id) }
                colorLayoutLogicImage()
            }

            "Rotation" -> {
                log(tag, "Rotation Image")
                viewStubRotateImage?.let { hideAllStubs(it.id) }
                rotateLogicImage()
            }
        }
    }

    private fun setUpBackButton() = binding.apply {
        btnBack.setOnClickListener {
            val exitConfirmationDialog = ExitDialog()
            exitConfirmationDialog.setListener(object : ExitConfirmationDialogListener {
                override fun onYesClicked() {
                    log(tag, "Yes")
                    finish()
                }

                override fun onNoClicked() {
                    log(tag, "No")

                }

                /*   override fun onSaveDraftClicked() {
                       log(tag, "Save")
                   }*/

            })
            exitConfirmationDialog.show(supportFragmentManager, "ExitConfirmationDialog")
        }
    }

    private fun selectedItemOptionsRecyclerView() = binding.apply {

        selectedOptionsRecyclerView = recyclerViewSelectedOptions
        selectedOptionsRecyclerView.layoutManager = LinearLayoutManager(
            this@EditorActivity, LinearLayoutManager.HORIZONTAL, false
        )

        selectedAdapterOptions = MainOptionsAdapter(
            bottomSelectedOptionsData,
            object : MainEditorOptionsItemClick {
                override fun onItemClick(text: String) {
                    callBackSelectedItem(text)
                }
            })

        selectedOptionsRecyclerView.adapter = selectedAdapterOptions
    }

    fun callBackSelectedItem(text: String) {

        when (text) {
            "Controls" -> {
                hideAllCorners()
                viewStub?.let { hideAllStubs(it.id) }
                controlLayoutLogic()
            }

            "Align" -> {
                hideAllCorners()
                viewStubAlign?.let { hideAllStubs(it.id) }
                alignLayoutLogic()
            }

            "Shadow" -> {
                hideAllCorners()
                hideAllStubs(0)
                shadowLayoutLogic()
            }

            "Fonts" -> {
                hideAllCorners()
                viewStubFont?.let { hideAllStubs(it.id) }
                fontLayoutLogic()
            }

            "Color" -> {
                hideAllCorners()
                viewStubColor?.let { hideAllStubs(it.id) }
                colorLayoutLogic()
            }

            "3D Rotate" -> {

                hideAllCorners()
                viewStubDRotate?.let { hideAllStubs(it.id) }
                dRotateLogic()
            }

            "Rotation" -> {

                hideAllCorners()
                viewStubRotate?.id?.let { hideAllStubs(it) }
                rotateLogic()
            }

            "Spacing" -> {

                hideAllCorners()
                viewStubSpacer?.id?.let { hideAllStubs(it) }
                spacerLogic()
            }

            "Opacity" -> {

                hideAllCorners()
                viewStubOpacity?.id?.let { hideAllStubs(it) }
                opacityLogic()
            }

            "Style" -> {

                hideAllCorners()
                viewStubStyle?.id?.let { hideAllStubs(it) }
                styleLogic()
            }

            "Size" -> {

                hideAllCorners()
                viewStubSize?.id?.let { hideAllStubs(it) }
                sizeLogic()
            }
        }
    }

    private fun shadowLayoutLogic() {
        binding.shadowRecyclerView.visible()
        val recyclerViewShadow = binding.shadowRecyclerView
        recyclerViewShadow.layoutManager = LinearLayoutManager(
            this@EditorActivity, LinearLayoutManager.HORIZONTAL, false
        )
        recyclerViewShadow.adapter = ShadowAdapter(
            shadowList, object : ItemShadowClickCallback {
                override fun onShadowClick(shadow: String) {
                    when (shadow) {
                        "OFF" -> {
                            hideAllCornerWithoutShadowList()
                            hideShadowStubs(0)
                            shadowOffLogic()
                        }

                        "ANGLE" -> {
                            hideAllCornerWithoutShadowList()

                            viewStubShadowAngle?.let { hideShadowStubs(it.id) }
                            shadowAngleLogic()
                        }

                        "BLUR" -> {
                            hideAllCornerWithoutShadowList()

                            viewStubShadowSingleSlider?.let { hideShadowStubs(it.id) }
                            shadowBlurLogic()

                        }

                        "COLOR" -> {
                            hideAllCornerWithoutShadowList()

                            viewStubColorShadow?.let { hideShadowStubs(it.id) }
                            shadowColorLogic()
                        }

                        "OPACITY" -> {
                            hideAllCornerWithoutShadowList()
                            viewStubOpacityShadow?.let { hideShadowStubs(it.id) }
                            shadowOpacityLogic()

                        }
                    }
                }
            }
        )
    }

    private fun shadowOffLogic() {
        shadowBlur = 1f
        shadowAngleX = 0f
        shadowAngleY = 0f
        shadowColor = "#000000"
        updateView()
        (currentView as TextView).setShadowLayer(
            shadowBlur, shadowAngleX, shadowAngleY, Color.parseColor(shadowColor)
        )
    }

    private fun shadowOpacityLogic() = shadowOpacityLayoutView?.apply {
        seekBar.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being started
                hideAllCornerWithoutShadowList()
            }

            override fun onStopTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being stopped
            }
        })
        seekBar.addOnChangeListener { slider, value, fromUser ->

            val opacity = (value / 100f).coerceAtMost(1f)
            val shadowColorWithOpacity = Color.argb(
                (opacity * 255).toInt(),
                Color.red(Color.parseColor(shadowColor)),
                Color.green(Color.parseColor(shadowColor)),
                Color.blue(Color.parseColor(shadowColor))
            )

            (currentView as TextView).setShadowLayer(
                shadowBlur, shadowAngleX, shadowAngleY,
                shadowColorWithOpacity
            )
        }
    }

    private fun shadowColorLogic() = shadowColorLayoutView?.apply {
        val recyclerViewColor = shadowColorRecyclerView

        binding.colorPicker.attachAlphaSlider(binding.alphaSlideBar)
        binding.colorPicker.setColorListener(ColorListener { color, fromUser ->
            val hexColor = String.format("#%06X", (0xFFFFFF and color))
            shadowColor = hexColor
            updateView()
            log(tag, "Color Val Picker: ${Color.parseColor(hexColor)}")

            (currentView as TextView).setShadowLayer(
                shadowBlur, shadowAngleX, shadowAngleY, Color.parseColor(shadowColor)
            )
        })
        recyclerViewColor.layoutManager = GridLayoutManager(
            this@EditorActivity, 2, GridLayoutManager.HORIZONTAL, false
        )
        recyclerViewColor.adapter = ColorsAdapter(colorList, object : ItemColorCallBack {
            override fun onItemColorClick(color: String) {
//                val hexColor = String.format("#%06X", (0xFFFFFF and color))

                shadowColor = color
                updateView()
                log(tag, "Color Val Recycle: $shadowColor")

                (currentView as TextView).setShadowLayer(
                    shadowBlur, shadowAngleX, shadowAngleY, Color.parseColor(shadowColor)
                )
            }

            override fun onItemColorPickerClick(toggle: Boolean) {

                if (toggle) {
                    binding.colorPicker.visible()
                    binding.alphaSlideBar.visible()
                    binding.tvDone.visible()
                    recyclerViewColor.gone()
                    binding.shadowRecyclerView.gone()
                    binding.recyclerViewSelectedOptions.gone()
                    viewStubColorShadow?.gone()
                } else {
                    binding.colorPicker.gone()
                    binding.alphaSlideBar.gone()
                    binding.shadowRecyclerView.visible()
                    binding.recyclerViewSelectedOptions.visible()
                    viewStubColorShadow?.visible()
                }
            }
        })

        binding.tvDone.setOnClickListener {
            recyclerViewColor.visible()
            binding.colorPicker.gone()
            binding.tvDone.gone()
            binding.alphaSlideBar.gone()
            binding.shadowRecyclerView.visible()
            binding.recyclerViewSelectedOptions.visible()
            binding.stubColor.viewStub?.visible()
        }
    }


    private fun shadowBlurLogic() = shadowSingleSliderLayoutView?.apply {
        seekBar.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being started
                hideAllCornerWithoutShadowList()


            }

            override fun onStopTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being stopped
                updateView()

            }
        })
        seekBar.addOnChangeListener { slider, value, fromUser ->
            // Responds to when slider's value is changed
            shadowBlur = value
            (currentView as TextView).setShadowLayer(
                shadowBlur,
                shadowAngleX,
                shadowAngleY,
                Color.parseColor(shadowColor)
            );
//            Utils.log("addOnChangeListener ${slider.value}")
        }
    }

    private fun shadowAngleLogic() = shadowAngleLayoutView?.apply {

        val color: Int = (currentView as TextView).currentTextColor
        val hexColor = String.format("#%06X", 0xFFFFFF and color)



        seekBarX.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being started
                hideAllCornerWithoutShadowList()


            }

            override fun onStopTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being stopped
                updateView()

            }
        })
        seekBarX.addOnChangeListener { slider, value, fromUser ->
            // Responds to when slider's value is changed
            shadowAngleX = value
            (currentView as TextView).setShadowLayer(
                shadowBlur, shadowAngleX, shadowAngleY, Color.parseColor(shadowColor)
            );
//            Utils.log("addOnChangeListener ${slider.value}")
        }

        //Perspective Y
        seekBarY.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being started
                hideAllCornerWithoutShadowList()

            }

            override fun onStopTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being stopped

                updateView()
            }
        })
        seekBarY.addOnChangeListener { slider, value, fromUser ->
            shadowAngleY = value
            // Responds to when slider's value is changed
            (currentView as TextView).setShadowLayer(
                shadowBlur, shadowAngleX, shadowAngleY,
                Color.parseColor(shadowColor)
            )
//            Utils.log("addOnChangeListener ${slider.value}")
        }
    }

    private fun colorLayoutLogic() = colorLayoutView?.apply {
        val recyclerViewColor = colorRecyclerView

        binding.colorPicker.attachAlphaSlider(binding.alphaSlideBar)
        binding.colorPicker.setColorListener(ColorListener { color, _ ->
            val colorHex = String.format("#%06X", 0xFFFFFF and color)

            log(tag, "color from picker: $color")
            log(tag, "color from picker: $colorHex")
            currentViewColor = colorHex
            updateView()
            (currentView as TextView).setTextColor(Color.parseColor(colorHex))
        })
        recyclerViewColor.layoutManager = GridLayoutManager(
            this@EditorActivity, 2, GridLayoutManager.HORIZONTAL, false
        )
        recyclerViewColor.adapter = ColorsAdapter(colorList, object : ItemColorCallBack {
            override fun onItemColorClick(color: String) {
//                val colorHex = String.format("#%06X", 0xFFFFFF and color)
                currentViewColor = color.toString()
                updateView()
//                log(tag, "color hex: $colorHex")
                log(tag, "color hex: $color")
                (currentView as TextView).setTextColor(Color.parseColor(color))
            }

            override fun onItemColorPickerClick(toggle: Boolean) {

                if (toggle) {
                    binding.colorPicker.visible()
                    binding.alphaSlideBar.visible()
                    binding.tvDone.visible()
                    recyclerViewColor.gone()
                    binding.recyclerViewSelectedOptions.gone()
                    binding.stubColor.viewStub?.gone()
                } else {
                    binding.colorPicker.gone()
                    binding.alphaSlideBar.gone()

                    binding.recyclerViewSelectedOptions.visible()
                    binding.stubColor.viewStub?.visible()
                }
            }
        })
        binding.tvDone.setOnClickListener {
            recyclerViewColor.visible()
            binding.colorPicker.gone()
            binding.tvDone.gone()
            binding.alphaSlideBar.gone()
            binding.recyclerViewSelectedOptions.visible()
            binding.stubColor.viewStub?.visible()
        }
    }

    private fun colorLayoutLogicImage() = colorLayoutViewImage?.apply {
        val recyclerViewColor = colorRecyclerView
        binding.colorPicker.attachAlphaSlider(binding.alphaSlideBar)
        binding.colorPicker.setColorListener(ColorListener { color, fromUser ->
            (currentView as ImageView).setColorFilter(color)

        })
        recyclerViewColor.layoutManager = GridLayoutManager(
            this@EditorActivity, 2, GridLayoutManager.HORIZONTAL, false
        )
        recyclerViewColor.adapter = ColorsAdapter(colorList, object : ItemColorCallBack {
            override fun onItemColorClick(color: String) {
//                val colorHex = String.format("#%06X", 0xFFFFFF and color)
                currentViewColor = color
                log(tag, "color hex: $currentViewColor")
                (currentView as ImageView).setColorFilter(Color.parseColor(color))
            }

            override fun onItemColorPickerClick(toggle: Boolean) {

                if (toggle) {
                    binding.colorPicker.visible()
                    binding.alphaSlideBar.visible()
                    binding.tvDone.visible()
                    recyclerViewColor.gone()
                    binding.recyclerViewSelectedOptions.gone()
                    binding.stubColor.viewStub?.gone()
                } else {
                    binding.colorPicker.gone()
                    binding.alphaSlideBar.gone()

                    binding.recyclerViewSelectedOptions.visible()
                    binding.stubColor.viewStub?.visible()
                }
            }
        })
        binding.tvDone.setOnClickListener {
            recyclerViewColor.visible()
            binding.colorPicker.gone()
            binding.tvDone.gone()
            binding.alphaSlideBar.gone()
            binding.recyclerViewSelectedOptions.visible()
            binding.stubColor.viewStub?.visible()
        }
    }

    private fun fontLayoutLogic() = fontLayoutView?.apply {
        fontRecyclerView.layoutManager = LinearLayoutManager(
            this@EditorActivity, LinearLayoutManager.HORIZONTAL, false
        )
        fontRecyclerView.adapter =
            FontsAdapter(this@EditorActivity, fontsList, object : ItemFontClickCallback {
                override fun itemClick(text: Int, fontName: String) {

                    val typeface = ResourcesCompat.getFont(this@EditorActivity, text)
                    (currentView as TextView).typeface = typeface
                    font = fontName
                    updateView()
                }
            })
    }

    private fun sizeLogic() = sizeLayoutView?.apply {
        sliderSize.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being started
                hideAllCorners()

            }

            override fun onStopTrackingTouch(slider: Slider) {
                this@EditorActivity.fontSize = slider.value.toString()
                updateView()
                // Responds to when slider's touch event is being stopped

            }
        })
        sliderSize.addOnChangeListener { _, value, _ ->
            // Responds to when slider's value is changed
            (currentView as TextView).textSize = value
        }
    }

    private fun sizeLogicImage() = sizeLayoutViewImage?.apply {
        sliderSize.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being started
                hideAllCorners()

            }

            override fun onStopTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being stopped


            }
        })
        sliderSize.addOnChangeListener { _, value, _ ->
            // Responds to when slider's value is changed
            currentView?.layoutParams = ViewGroup.LayoutParams(
                value.toInt(),
                value.toInt()
            )

        }
    }

    private fun styleLogic() = styleLayoutView?.apply {
        (currentView as TextView)
        actionBold.setOnClickListener {
//            currentText.setTypeface(null, Typeface.BOLD)
            val currentTypeface = (currentView as TextView).typeface ?: Typeface.DEFAULT
            val isBold = currentTypeface.isBold
            val isItalic = currentTypeface.isItalic


            if (isBold) {
                (currentView as TextView).setTypeface(null, Typeface.NORMAL)
                textStyle = "normal"
                updateView()
            } else if (isItalic) {
                (currentView as TextView).setTypeface(null, Typeface.BOLD_ITALIC)
                textStyle = "bold_italic"
                updateView()

            } else {
                (currentView as TextView).setTypeface(null, Typeface.BOLD)
                textStyle = "bold"
                updateView()

            }
        }
        actionUnderline.setOnClickListener {
//            currentText.paintFlags = currentText.paintFlags or Paint.UNDERLINE_TEXT_FLAG;
            val isUnderlined =
                (currentView as TextView).paintFlags and Paint.UNDERLINE_TEXT_FLAG != 0

            if (isUnderlined) {
                (currentView as TextView).paintFlags =
                    (currentView as TextView).paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
                textStyle = "normal"
                updateView()
            } else {
                (currentView as TextView).paintFlags =
                    (currentView as TextView).paintFlags or Paint.UNDERLINE_TEXT_FLAG
                textStyle = "underline"
                updateView()
            }
        }
        actionItalic.setOnClickListener {

            val currentTypeface = (currentView as TextView).typeface ?: Typeface.DEFAULT
            val isItalic = currentTypeface.isItalic

            if (isItalic) {
                (currentView as TextView).setTypeface(null, Typeface.NORMAL)
                textStyle = "normal"
                updateView()
            } else {
                (currentView as TextView).setTypeface(null, Typeface.ITALIC)
                textStyle = "italic"
                updateView()
            }

        }
        actionUpperCase.setOnClickListener {
            (currentView as TextView).text = (currentView as TextView).text.toString().uppercase()
            textStyle = "uppercase"
            updateView()
        }
        actionLowerCase.setOnClickListener {
            (currentView as TextView).text = (currentView as TextView).text.toString().lowercase()
            textStyle = "lowercase"
            updateView()

        }
    }

    private fun opacityLogic() = opacityLayoutView?.apply {
        log(tag, "color hex in opacity: $currentViewColor")

        sliderOpacity.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being started
                hideAllCorners()
            }

            override fun onStopTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being stopped

            }
        })
        sliderOpacity.addOnChangeListener { slider, value, fromUser ->
            // Responds to when slider's value is changed
            val currentColor = Color.parseColor(currentViewColor)
            (currentView as TextView).setTextColor(
                Color.argb(
                    value.toInt(),
                    Color.red(currentColor), Color.green(currentColor),
                    Color.blue(currentColor)
                )
            )
            val percentage = (value / 255 * 100).toInt()
            percentageTv.text = percentage.toString()
        }
    }

    private fun opacityLogicImage() = opacityLayoutViewImage?.apply {


        sliderOpacity.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being started
                hideAllCorners()
            }

            override fun onStopTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being stopped

            }
        })
        sliderOpacity.addOnChangeListener { slider, value, fromUser ->
            // Responds to when slider's value is changed
            (currentView as ImageView).imageAlpha = value.toInt()

            val percentage = (value / 255 * 100).toInt()
            percentageTv.text = percentage.toString()
        }
    }

    private fun spacerLogic() = spacerLayoutView?.apply {
        sliderSpacing.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being started
                hideAllCorners()
            }

            override fun onStopTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being stopped
                updateView()
            }
        })
        sliderSpacing.addOnChangeListener { slider, value, fromUser ->
            // Responds to when slider's value is changed
            letterSpacing = value.toString()
            (currentView as TextView).letterSpacing = value
        }
    }

    private fun rotateLogic() = rotateLayoutView?.apply {

        seekbar.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being started
                hideAllCorners()

            }

            override fun onStopTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being stopped

                rotation = slider.value.toString()
                updateView()
            }

        })
        seekbar.addOnChangeListener { slider, value, fromUser ->
            // Responds to when slider's value is changed

            currentView?.rotation = value

        }
    }

    private fun rotateLogicImage() = rotateLayoutViewImage?.apply {
        seekbar.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being started
                hideAllCorners()

            }

            override fun onStopTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being stopped


            }

        })
        seekbar.addOnChangeListener { slider, value, fromUser ->
            // Responds to when slider's value is changed
            currentView?.rotation = value

        }
    }

    private fun dRotateLogic() = dRotateLayoutView?.apply {

        seekBarX.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being started
                hideAllCorners()


            }

            override fun onStopTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being stopped

                dRotationX = slider.value.toString()
                updateView()
            }
        })
        seekBarX.addOnChangeListener { slider, value, fromUser ->
            // Responds to when slider's value is changed
            currentView?.rotationY = value
//            Utils.log("addOnChangeListener ${slider.value}")
        }

        //Perspective Y
        seekBarY.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being started
                hideAllCorners()


            }

            override fun onStopTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being stopped
                dRotationY = slider.value.toString()
                updateView()
            }
        })
        seekBarY.addOnChangeListener { slider, value, fromUser ->
            // Responds to when slider's value is changed
            currentView?.rotationX = value
//            Utils.log("addOnChangeListener ${slider.value}")
        }
    }

    private fun hideAllCorners() {
        /*   draggableTextView?.hideIcons(
               topLeftCornerIcon,
               topRightCornerIcon,
               bottomRightCornerIcon,
               bottomLeftCornerIcon,
           )*/
        binding.shadowRecyclerView.gone()
    }

    private fun hideAllCornerWithoutShadowList() {
        /*   draggableTextView?.hideIcons(
               topLeftCornerIcon,
               topRightCornerIcon,
               bottomRightCornerIcon,
               bottomLeftCornerIcon,
           )*/
    }

    private fun hideAllStubs(stubId: Int) {
        val allStubs = listOf(
            viewStub,
            viewStubAlign,
            viewStubDRotate,
            viewStubRotate,
            viewStubSpacer,
            viewStubOpacity,
            viewStubStyle,
            viewStubSize,
            viewStubFont,
            viewStubColor,
            viewStubShadowAngle,
            viewStubShadowSingleSlider,
            viewStubColorShadow,
            viewStubOpacityShadow,
            viewStubControlImage,
            viewStubColorImage,
            viewStubSizeImage,
            viewStubOpacityImage,
            viewStubRotateImage
        )
        allStubs.forEach { currentStub ->
            if (currentStub?.id != stubId) {
                currentStub?.gone()
            } else {
                currentStub.visible()
            }
        }
    }

    fun hideShadowStubs(stubId: Int) {
        val allStubs = listOf(
            viewStubShadowAngle,
            viewStubShadowSingleSlider,
            viewStubColorShadow,
            viewStubOpacityShadow
        )
        allStubs.forEach { currentStub ->
            if (currentStub?.id != stubId) {
                currentStub?.gone()
            } else {
                currentStub.visible()
            }
        }
    }

    fun callBackItems(text: String) {

        if (text == "ADD TEXT") {
            binding.recyclerViewBackground.gone()
            addNeonText.show(supportFragmentManager, "BSDialogFragment")
        } else if (text == "STICKER") {
            log(tag, "Sticker")
            if (listOfUrls != null) {
                binding.recyclerViewBackground.gone()
                addSticker?.show(supportFragmentManager, "StickerDialogFragment")
            }
        } else if (text == "BACKGROUNDS") {
            log(tag, "BACKGROUNDS")
            binding.recyclerViewBackground.visible()
            binding.recyclerViewBackground.layoutManager = LinearLayoutManager(
                this@EditorActivity, LinearLayoutManager.HORIZONTAL,
                false
            )

            binding.recyclerViewBackground.adapter =
                listOfHits?.let {
                    BackgroundsAdapter(it, object :
                        BackgroundCallBack {
                        override fun onBackgroundClick(url: String) {
                            Glide.with(this@EditorActivity).asBitmap().load(url).override(200, 200)
                                .fitCenter()
                                .placeholder(R.drawable.ic_launcher_foreground)
                                .into(object : CustomTarget<Bitmap>() {
                                    override fun onResourceReady(
                                        resource: Bitmap,
                                        transition: Transition<in Bitmap>?,
                                    ) {
                                        // Set the loaded bitmap as the background of the CardView
                                        val drawable = BitmapDrawable(resources, resource)
                                        editorContainer.background = drawable
                                    }

                                    override fun onLoadCleared(placeholder: Drawable?) {
                                        // Implement if needed
                                    }
                                })
                        }

                        override fun onCategoryClick(data: List<CategoryModel>) {
                            //Nothing yet
                        }
                    }
                    )
                }
        } else if (text == "BG REMOVER") {
            /* log(tag, "BG REMOVER")
             binding.recyclerViewBackground.gone()
             openGallery()*/
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

//        val remover = RemoveBg(this@EditorActivity)
        if (requestCode == REQUEST_IMAGE_CAPTURE_GALLERY && resultCode == Activity.RESULT_OK) {
            val selectedImageUri: Uri? = data?.data
            log(tag, "selected Image $selectedImageUri")

            /*selectedImageUri?.let { uri ->
                val inputStream: InputStream? = contentResolver.openInputStream(uri)
                inputStream?.use { stream ->
                    val bitmap: Bitmap? = BitmapFactory.decodeStream(stream)
                    if (bitmap != null) {
                        CoroutineScope(Dispatchers.IO).launch {
                            remover.clearBackground(bitmap)
                                .onStart {
                                    loader(true)
                                }
                                .onCompletion {
                                    loader(false)
                                }
                                .collect { output ->
                                    withContext(Dispatchers.Main) {
                                        val viewId = (50..70).random()
                                        makingViewsDraggable(
                                            "image",
                                            200f,
                                            300f,
                                            viewId,
                                            data = "",
                                            priority = 12,
                                            imageType = "BITMAP",
                                            bitmap = output
                                        )
                                    }
                                }
                        }

                        *//*     BackgroundRemover.bitmapForProcessing(bitmap,
                                 true,
                                 listener = object : OnBackgroundChangeListener {
                                     override fun onFailed(exception: Exception) {
                                         log(tag, "exception $exception")
                                     }

                                     override fun onSuccess(bitmap: Bitmap) {
                                         log(tag, "successful bitmap $bitmap")
                                         val viewId = (50..70).random()
                                         makingViewsDraggable(
                                             "image",
                                             200f,
                                             300f,
                                             viewId,
                                             data = "",
                                             priority = 12,
                                             imageType = "BITMAP",
                                             bitmap = bitmap
                                         )
                                     }
                                 })*//*
                    }
                }
            }*/
        }
    }

    private fun loaderForData(toggle: Boolean) {
        if (toggle) {
            CoroutineScope(Dispatchers.Main).launch {
                binding.progressBar.visible()
            }
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                binding.progressBar.gone()
            }
        }

    }

    private fun loader(toggle: Boolean) {
        if (toggle) {
            CoroutineScope(Dispatchers.Main).launch {
                binding.progressBar.visible()
                binding.transparentGreyView.visible()
            }
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                binding.progressBar.gone()
                binding.transparentGreyView.gone()
            }
        }

    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE_GALLERY)
    }

    /* private fun creatingViews() = binding.apply {

         for (data in views) {
             makingViewsDraggable(
                 data.viewType,
                 data.x,
                 data.y,
                 data = data.data,
                 priority = data.priority,
                 width = data.width,
                 height = data.height,
                 color = data.color,
                 fontSize = data.fontSize,
                 textStyle = data.textStyle,
                 font = data.font,
                 alignment = data.alignment,
                 letterSpacing = data.letterSpacing,
                 viewId = data.v
             )

         }
     }*/

    private fun makingViewsDraggable(
        viewType: String,
        x: Float,
        y: Float,
        viewId: Int,
        imageType: String = "",
        bitmap: Bitmap? = null,
        width: String = "200",
        height: String = "200",
        color: String = "#000000",
        fontSize: String = "20",
        textStyle: String = "regular",
        font: String = "poppins",
        alignment: String = "left",
        letterSpacing: String = "0",
        data: String,
        priority: Int,
    ) = binding.apply {
        if (viewType == "text") {
            draggableTextView =
                DraggableTextView(
                    this@EditorActivity,
                    currentPosition = { y, x ->
                        currentY = y
                        currentX = x
                    },
                    callback = this@EditorActivity,
                    onItemClick = { booleanCallback ->
                        boolTopBar = booleanCallback
                        toggleUndoRedo(booleanCallback)
                    },
                    x = x,
                    y = y,
                    opacity = opacity,
                    rotation = rotation,
                    text = data,
                    width = width,
                    height = height,
                    color = color,
                    viewId = viewId,
                    fontSize = fontSize,
                    textStyle = textStyle,
                    font = font,
                    alignment = alignment,
                    rotationX = rotationX,
                    rotationY = rotationY,
                    dRotationX = dRotationX,
                    dRotationY = dRotationY,
                    shadowAngleX = shadowAngleX.toString(),
                    shadowAngleY = shadowAngleY.toString(),
                    shadowBlur = shadowBlur.toString(),
                    shadowColor = shadowColor.toString(),
                    viewType = viewType,
                    lineHeight = lineHeight,
                    priority = priority.toString(),
                    parent = editorContainer,
                    letterSpacing = letterSpacing,
                    currentView = { currentViewId, currentView, isSelected, viewData ->
                        this@EditorActivity.currentView = currentView
                        val currentColor =
                            (this@EditorActivity.currentView as TextView).currentTextColor
                        this@EditorActivity.currentViewColor =
                            java.lang.String.format("#%06X", 0xFFFFFF and currentColor)
                        this@EditorActivity.viewId = currentViewId
                        this@EditorActivity.viewData = viewData
//                        log(tag, "CurrentViewID: ${this@EditorActivity.viewId}")
//                        enableCornerListeners(currentView)
                        if (isSelected) {
                            if (stub.isInflated) {
                                hideAllStubs(0)
                                hideShadowStubs(0)
                            }
                            binding.recyclerViewBackground.gone()
                            recyclerViewSelectedOptionsImage.gone()
                            mainOptionsRecyclerView.visibility = View.GONE
                            selectedOptionsRecyclerView.visibility = View.VISIBLE
                        }
                    }
                )

            editorContainer.addView(draggableTextView)
            viewModel.storeInitialPosition(draggableTextView!!)
            viewModel.addDynamicView(draggableTextView!!)
            layers(draggableTextView!!, data, priority)

            draggableTextView?.enableDragAndDrop(
                /*topLeftCornerIcon,
                topRightCornerIcon,
                bottomRightCornerIcon,
                bottomLeftCornerIcon,*/
            )

        } else if (viewType == "image") {
            draggableImageView =
                DraggableImageView(
                    this@EditorActivity, x, y, imageType, bitmap, data,
                    onItemClick = { booleanCallback ->
                        boolTopBar = booleanCallback
                        toggleUndoRedo(booleanCallback)
                    },
                    callback = this@EditorActivity

                ) { currentView, isSelected, url ->
                    this@EditorActivity.currentView = currentView
                    this@EditorActivity.viewData = url
                    currentY = currentView.y
                    currentX = currentView.x
//                    enableCornerListeners(currentView)
                    if (isSelected) {
                        if (stub.isInflated) {
                            hideAllStubs(0)
                            hideShadowStubs(0)
                        }
//                            stub.viewStub?.visibility = View.GONE
                        mainOptionsRecyclerView.gone()
                        shadowRecyclerView.gone()
                        selectedOptionsRecyclerView.gone()
                        recyclerViewSelectedOptionsImage.visible()
                    }
                }
            binding.editorLayout.addView(draggableImageView)
            viewModel.storeInitialPosition(draggableImageView)
            viewModel.addDynamicView(draggableImageView)
            layers(draggableImageView, data, priority)

            draggableImageView.enableDragAndDrop()
        }
    }

    /*    private fun enableCornerListeners(draggableView: View) = binding.apply {
            */
    /* bottomRightCornerIcon.attachTo(draggableTextView, CornerIconListener.Corner.BOTTOM_RIGHT)
         topRightCornerIcon.attachTo(draggableTextView, CornerIconListener.Corner.TOP_RIGHT)
         topLeftCornerIcon.attachTo(draggableTextView, CornerIconListener.Corner.TOP_LEFT)
         bottomLeftCornerIcon.attachTo(draggableTextView, CornerIconListener.Corner.BOTTOM_LEFT)*//*

*/
    /*        bottomRightCornerIcon.enableResizeOnTouch(
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
        )*/
    /*
    }*/

    private fun alignLayoutLogic() = alignLayoutView?.apply {
        if (currentView is TextView) {
            alignLeft.setOnClickListener {
                (currentView as TextView).gravity = Gravity.CENTER or Gravity.START
                this@EditorActivity.alignment = "left"
                updateView()
            }
            alignCenter.setOnClickListener {
                (currentView as TextView).gravity = Gravity.CENTER
                this@EditorActivity.alignment = "center"
                updateView()
            }
            alignRight.setOnClickListener {
                (currentView as TextView).gravity = Gravity.CENTER or Gravity.END
                this@EditorActivity.alignment = "right"
                updateView()
            }
        }
    }

    private fun controlLayoutLogic() = controlLayoutView?.apply {
        var xDirection = 200f
        var yDirection = 200f

        actionDown.setOnClickListener {
            currentView?.y = currentView?.y!! + 10
            currentY = currentView?.y
            updateView()
        }
        actionUp.setOnClickListener {
            currentView?.y = currentView?.y!! - 10
            currentY = currentView?.y
            updateView()

        }
        actionLeft.setOnClickListener {
            currentView?.x = currentView?.x!! - 10
            currentX = currentView?.x
            updateView()
        }
        actionRight.setOnClickListener {
            currentView?.x = currentView?.x!! + 10
            currentX = currentView?.x
            updateView()
        }

        actionFlipHorizontally.setOnClickListener {


            val currentRotationY = currentView?.rotationY ?: 0f

            val newRotationY = if (currentRotationY == 0f) 180f else 0f
            currentView?.rotationY = newRotationY
            rotationY = currentView?.rotationY.toString()
            updateView()
        }
        actionFlipVertically.setOnClickListener {
            val currentRotationX = currentView?.rotationX ?: 0f

            val newRotationX = if (currentRotationX == 0f) 180f else 0f
            currentView?.rotationX = newRotationX
            rotationX = currentView?.rotationX.toString()
            updateView()

        }

        actionDelete.setOnClickListener {
            editorContainer.removeView(currentView)
//            currentView?.let { (parent as ViewGroup).removeView(it) }


//            currentView?.gone()
            if (binding.stub.isInflated) {
                binding.stub.root.gone()
                binding.stubAlign.root.gone()
            }

            mainOptionsRecyclerView.visibility = View.VISIBLE
            selectedOptionsRecyclerView.visibility = View.GONE
        }
        actionCopy.setOnClickListener {
            xDirection += 20
            yDirection += 20

            if (currentView is TextView) {
                val data = (currentView as TextView).text.toString()
                val randomViewId = (40..60).random()
                makingViewsDraggable(
                    viewType = "text", x = xDirection,
                    y = yDirection,
                    data = data,
                    priority = 10,
                    fontSize = "56",
                    viewId = randomViewId,
                )
                updateView()
            }
        }
        actionEdit.setOnClickListener {

            val currentText = (currentView as TextView).text.toString()
            val editBottomSheet = EditTextBottomSheet(currentText) { newText ->
                (currentView as TextView).text = newText
                viewData = newText
                updateView()
            }
            editBottomSheet.show(supportFragmentManager, "EditTextDialogFragment")
        }
    }

    private fun controlLayoutLogicImage() = controlLayoutViewImage?.apply {

        actionDown.setOnClickListener {
            currentView?.y = currentView?.y!! + 10
            currentY = currentView?.y
            updateView()
        }
        actionUp.setOnClickListener {
            currentView?.y = currentView?.y!! - 10
            currentY = currentView?.y
            updateView()

        }
        actionLeft.setOnClickListener {
            currentView?.x = currentView?.x!! - 10
            currentX = currentView?.x
            updateView()
        }
        actionRight.setOnClickListener {
            currentView?.x = currentView?.x!! + 10
            currentX = currentView?.x
            updateView()
        }

        actionFlipHorizontally.setOnClickListener {
            val currentRotationY = currentView?.rotationY ?: 0f
            val newRotationY = if (currentRotationY == 0f) 180f else 0f
            currentView?.rotationY = newRotationY
        }
        actionFlipVertically.setOnClickListener {
            val currentRotationX = currentView?.rotationX ?: 0f
            val newRotationX = if (currentRotationX == 0f) 180f else 0f
            currentView?.rotationX = newRotationX
        }

        actionDelete.setOnClickListener {
            editorContainer.removeView(currentView)
            if (binding.stubControlImage.isInflated) {
                binding.stubControlImage.root.gone()
            }

            mainOptionsRecyclerView.visibility = View.VISIBLE
            recyclerViewItemsImage?.visibility = View.GONE
        }
        actionCopy.setOnClickListener {
            var xDirection = currentView?.x!!
            var yDirection = currentView?.y!!

            xDirection += 10
            yDirection += 10

            val randomViewId = (10..30).random()
            if (currentView is ImageView) {
                makingViewsDraggable(
                    "image",
                    x = xDirection,
                    y = yDirection,
                    data = viewData!!,
                    priority = 6,
                    viewId = randomViewId,
                )
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun layers(view: View, text: String, priority: Int) {
        layerList.add(
            LayersModel(view, text, lock = false, hide = false, priority = priority)
        )
        layerList.sortBy { it.priority }

        adapter.notifyDataSetChanged()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewLayers.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewLayers.adapter = adapter

        itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback(adapter))
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewLayers)
    }

    private fun setupButtonLayers() = binding.apply {

        btnLayers.setOnClickListener {
            isRecyclerViewVisible = !isRecyclerViewVisible
            recyclerViewLayers.visibility = if (isRecyclerViewVisible) {
                View.VISIBLE
            } else {
                View.GONE
            }
            btnLayers.setImageResource(if (isRecyclerViewVisible) R.drawable.baseline_layers_24_filled else R.drawable.baseline_layers_24)
        }
    }

    private fun toggleUndoRedo(toggle: Boolean) = binding.apply {
        if (toggle) {
            isRecyclerViewVisible = false
            btnUndo.visibility = View.GONE
            btnRefresh.visibility = View.GONE
            btnRedo.visibility = View.GONE
            btnSave.gone()
            btnDone.visibility = View.VISIBLE
            recyclerViewLayers.visibility = View.GONE
            btnBack.visibility = View.GONE
        }
        val allParent = currentView?.parent as? ViewGroup

        btnDone.setOnClickListener {
//            hideAllCorners()
            hideAllStubs(0)
            if (allParent != null) {
                draggableTextView?.resetBackgroundForAllViews(allParent)
//                draggableImageView.resetBackgroundForAllViews(allParent)
            }
            recyclerViewSelectedOptionsImage.gone()
            isRecyclerViewVisible = false
            shadowRecyclerView.gone()
            btnUndo.visibility = View.VISIBLE
            btnRefresh.visibility = View.VISIBLE
            btnRedo.visibility = View.VISIBLE
            btnBack.visibility = View.VISIBLE
            btnSave.visible()
            selectedOptionsRecyclerView.gone()
            mainOptionsRecyclerView.visible()
            recyclerViewLayers.visibility = View.GONE
            btnDone.visibility = View.GONE
        }
    }

    private fun undoRedoListener() = binding.apply {

        btnUndo.setOnClickListener {

            val viewToUndo = viewModel.popLastChangeOrder()
            viewToUndo?.let {
                viewModel.undoToInitialPosition(it)
            }
        }
        btnRedo.setOnClickListener {
            val viewToRedo = viewModel.popLastRedo()
            viewToRedo?.let {
                viewModel.redoToInitialPosition(it)
            }
        }
        btnRefresh.setOnClickListener {
            viewModel.resetAllViewsToInitialPosition(viewModel.dynamicViewsList)
            /*   draggableTextView?.hideIcons(
                   topLeftCornerIcon,
                   topRightCornerIcon,
                   bottomRightCornerIcon,
                   bottomLeftCornerIcon,
               )*/

        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        //Not implemented yet
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }

    override fun updateZOrder() {
        adapter.updateZOrder()
    }

    override fun onDrag(view: View) {
        viewModel.addChangeToOrder(view)
        updateView()
    }

    /* override fun onGettingAllTheValues(
         viewId: String?,
 //        viewData: String?,
         alignment: String?,
         opacity: String?,
         rotation: String?,
         rotationX: String?,
         rotationY: String?,
         dRotationX: String?,
         dRotationY: String?,
         shadowAngleX: String?,
         shadowAngleY: String?,
         shadowBlur: String?,
         shadowColor: String?,
         viewType: String?,
         fontSize: String?,
         font: String?,
         letterSpacing: String?,
         lineHeight: String?,
         textStyle: String?,
         color: String?,
         width: String?,
         height: String?,
         priority: String?,
     ) {
         Log.d(tag, "viewId: $viewId")
         Log.d(tag, "alignment: $alignment")
         Log.d(tag, "opacity: $opacity")
         Log.d(tag, "rotation: $rotation")
         Log.d(tag, "rotationX: $rotationX")
         Log.d(tag, "rotationY: $rotationY")
         Log.d(tag, "dRotationX: $dRotationX")
         Log.d(tag, "dRotationY: $dRotationY")
         Log.d(tag, "shadowAngleX: $shadowAngleX")
         Log.d(tag, "shadowAngleY: $shadowAngleY")
         Log.d(tag, "shadowBlur: $shadowBlur")
         Log.d(tag, "shadowColor: $shadowColor")
         Log.d(tag, "viewType: $viewType")
         Log.d(tag, "fontSize: $fontSize")
         Log.d(tag, "font: $font")
         Log.d(tag, "letterSpacing: $letterSpacing")
         Log.d(tag, "lineHeight: $lineHeight")
         Log.d(tag, "textStyle: $textStyle")
         Log.d(tag, "color: $color")
         Log.d(tag, "width: $width")
         Log.d(tag, "height: $height")
         Log.d(tag, "priority: $priority")
     }*/

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val exitConfirmationDialog = ExitDialog()
        exitConfirmationDialog.setListener(object : ExitConfirmationDialogListener {
            override fun onYesClicked() {
                log(tag, "Yes")
                finish()
            }

            override fun onNoClicked() {
                log(tag, "No")

            }

            /*  override fun onSaveDraftClicked() {
                  log(tag, "Save")
                  val cvonvert = AllViews(

                  )


              }*/

        })
        exitConfirmationDialog.show(supportFragmentManager, "ExitConfirmationDialog")
        if (!exitConfirmationDialog.isVisible) {
            return
        } else {
            super.onBackPressed()
        }

    }

    companion object {
        const val REQUEST_IMAGE_CAPTURE_GALLERY = 1
    }
}