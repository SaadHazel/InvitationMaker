package com.saad.invitationmaker.features.editor


import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
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
import com.saad.invitationmaker.app.utils.Utils
import com.saad.invitationmaker.core.extensions.gone
import com.saad.invitationmaker.core.extensions.inflateAndGone
import com.saad.invitationmaker.core.extensions.visible
import com.saad.invitationmaker.databinding.ActivityEditorBinding
import com.saad.invitationmaker.databinding.AlignLayoutViewBinding
import com.saad.invitationmaker.databinding.ColorLayoutViewBinding
import com.saad.invitationmaker.databinding.ControlsLayoutViewBinding
import com.saad.invitationmaker.databinding.DRotateLayoutViewBinding
import com.saad.invitationmaker.databinding.FontsLayoutViewBinding
import com.saad.invitationmaker.databinding.OpacityLayoutViewBinding
import com.saad.invitationmaker.databinding.RotateLayoutViewBinding
import com.saad.invitationmaker.databinding.SizeLayoutItemBinding
import com.saad.invitationmaker.databinding.SpacerLayoutViewBinding
import com.saad.invitationmaker.databinding.StyleLayoutItemBinding
import com.saad.invitationmaker.features.editor.adapters.ColorsAdapter
import com.saad.invitationmaker.features.editor.adapters.FontsAdapter
import com.saad.invitationmaker.features.editor.adapters.LayersAdapter
import com.saad.invitationmaker.features.editor.adapters.MainOptionsAdapter
import com.saad.invitationmaker.features.editor.bottomSheets.EditTextBottomSheet
import com.saad.invitationmaker.features.editor.bottomSheets.NeonTextBottomSheet
import com.saad.invitationmaker.features.editor.bottomSheets.StickerBottomSheet
import com.saad.invitationmaker.features.editor.callbacks.ItemColorCallBack
import com.saad.invitationmaker.features.editor.callbacks.ItemFontClickCallback
import com.saad.invitationmaker.features.editor.callbacks.ItemTouchHelperAdapter
import com.saad.invitationmaker.features.editor.callbacks.ItemTouchHelperCallback
import com.saad.invitationmaker.features.editor.callbacks.MainEditorOptionsItemClick
import com.saad.invitationmaker.features.editor.callbacks.UpdateTouchListenerCallback
import com.saad.invitationmaker.features.editor.mappers.toMyData
import com.saad.invitationmaker.features.editor.models.Fonts
import com.saad.invitationmaker.features.editor.models.LayersModel
import com.saad.invitationmaker.features.editor.models.MainOptionsData
import com.saad.invitationmaker.features.editor.models.ViewsData
import com.saad.invitationmaker.features.editor.models.stickers.SingleStickerUrlList
import com.saad.invitationmaker.features.editor.touchListners.CornerIconListener
import com.saad.invitationmaker.features.editor.touchListners.DraggableImageView
import com.saad.invitationmaker.features.editor.touchListners.DraggableTextView
import com.saad.invitationmaker.features.editor.utils.CreateViews
import com.skydoves.colorpickerview.listeners.ColorListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EditorActivity : AppCompatActivity(), ItemTouchHelperAdapter, UpdateTouchListenerCallback {
    private lateinit var binding: ActivityEditorBinding
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

    private val viewModel: EditorViewModel by viewModels()
    private lateinit var editorContainer: ConstraintLayout
    private lateinit var mainOptionsRecyclerView: RecyclerView
    private lateinit var selectedOptionsRecyclerView: RecyclerView
    private lateinit var mainOptionsAdapter: MainOptionsAdapter
    private lateinit var selectedAdapterOptions: MainOptionsAdapter
    private lateinit var createViews: CreateViews
    private lateinit var views: List<ViewsData>
    private lateinit var bottomOptionsData: List<MainOptionsData>
    private lateinit var bottomSelectedOptionsData: List<MainOptionsData>
    private lateinit var fontsList: List<Fonts>
    private lateinit var colorList: List<Int>
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

    private var currentView: View? = null
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

        init()

        viewModel.stickers.observe(this) { data ->
            if (data != null) {
                listOfUrls = data.toMyData()
//                Utils.log("LiveData: $listOfUrls")
                addSticker.updateData(listOfUrls!!)
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

        createViews = CreateViews(this@EditorActivity)
        editorContainer = binding.editorLayout
        addSticker = StickerBottomSheet { stickerUrl ->
            makingViewsDraggable("image", 200f, 200f, stickerUrl, 6)
        }
        addNeonText = NeonTextBottomSheet { text ->
            makingViewsDraggable("text", 100f, 100f, text, 7)
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

        colorList = listOf(
            R.color.black,
            R.color.random,
            R.color.active_indicator_bg,
            R.color.grey,
            R.color.daynight_text_Color,
            R.color.light_grey,
            R.color.blue,
            R.color.colorpink,
            R.color.daynight_text_white,
            R.color.daynight_layeritem,
            R.color.colorAccent,
            R.color.colorBlue,
            R.color.black,
            R.color.random,
            R.color.active_indicator_bg,
            R.color.grey,
            R.color.daynight_text_Color,
            R.color.light_grey,
            R.color.blue,
            R.color.colorpink,
            R.color.daynight_text_white,
            R.color.daynight_layeritem,
            R.color.colorAccent,
            R.color.colorBlue
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
        setupRecyclerView()
        setupButtonLayers()
        undoRedoListener()
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
        Utils.log("callBackSelectedItem - Selected Item: $text")
        when (text) {
            "Controls" -> {
                Utils.log("Controls")
                hideAllCorners()
                viewStub?.let { hideAllStubs(it.id) }
                controlLayoutLogic()
            }

            "Align" -> {
                Utils.log("Align")
                hideAllCorners()
                viewStubAlign?.let { hideAllStubs(it.id) }
                alignLayoutLogic()
            }

            "Fonts" -> {
                Utils.log("Fonts")
                hideAllCorners()
                viewStubFont?.let { hideAllStubs(it.id) }
                fontLayoutLogic()
            }

            "Color" -> {
                Utils.log("Color")
                hideAllCorners()
                viewStubColor?.let { hideAllStubs(it.id) }
                colorLayoutLogic()
            }

            "3D Rotate" -> {
                Utils.log("3D Rotate")
                hideAllCorners()
                viewStubDRotate?.let { hideAllStubs(it.id) }
                dRotateLogic()
            }

            "Rotation" -> {
                Utils.log("Rotate")
                hideAllCorners()
                viewStubRotate?.id?.let { hideAllStubs(it) }
                rotateLogic()
            }

            "Spacing" -> {
                Utils.log("Spacing")
                hideAllCorners()
                viewStubSpacer?.id?.let { hideAllStubs(it) }
                spacerLogic()
            }

            "Opacity" -> {
                Utils.log("Opacity")
                hideAllCorners()
                viewStubOpacity?.id?.let { hideAllStubs(it) }
                opacityLogic()
            }

            "Style" -> {
                Utils.log("Style")
                hideAllCorners()
                viewStubStyle?.id?.let { hideAllStubs(it) }
                styleLogic()
            }

            "Size" -> {
                Utils.log("Size")
                hideAllCorners()
                viewStubSize?.id?.let { hideAllStubs(it) }
                sizeLogic()
            }
        }
    }

    private fun colorLayoutLogic() = colorLayoutView?.apply {
        val recyclerViewColor = colorRecyclerView
        binding.colorPicker.attachAlphaSlider(binding.alphaSlideBar)
        binding.colorPicker.setColorListener(ColorListener { color, fromUser ->
            (currentView as TextView).setTextColor(color)
        })
        recyclerViewColor.layoutManager = GridLayoutManager(
            this@EditorActivity, 2, GridLayoutManager.HORIZONTAL, false
        )
        recyclerViewColor.adapter = ColorsAdapter(colorList, object : ItemColorCallBack {
            override fun onItemColorClick(color: Int) {
                Utils.log("Colors: $color")
                (currentView as TextView).setTextColor(resources.getColor(color, null))
            }

            override fun onItemColorPickerClick(toggle: Boolean) {
                Utils.log("toggle: $toggle")
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
                override fun itemClick(text: Int) {
                    Utils.log("Font: $text")
                    val typeface = ResourcesCompat.getFont(this@EditorActivity, text)
                    (currentView as TextView).typeface = typeface
                }

            })
    }

    private fun sizeLogic() = sizeLayoutView?.apply {
        sliderSize.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being started
                hideAllCorners()
                Utils.log("onStartTrackingTouch ${slider.value}")
            }

            override fun onStopTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being stopped
                Utils.log("onStopTrackingTouch ${slider.value}")

            }
        })
        sliderSize.addOnChangeListener { slider, value, fromUser ->
            // Responds to when slider's value is changed
            currentView?.layoutParams = ViewGroup.LayoutParams(
                value.toInt(),
                value.toInt()
            )
            Utils.log("addOnChangeListener ${slider.value}")
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
            } else if (isItalic) {
                (currentView as TextView).setTypeface(null, Typeface.BOLD_ITALIC)
            } else {
                (currentView as TextView).setTypeface(null, Typeface.BOLD)
            }
        }
        actionUnderline.setOnClickListener {
//            currentText.paintFlags = currentText.paintFlags or Paint.UNDERLINE_TEXT_FLAG;
            val isUnderlined =
                (currentView as TextView).paintFlags and Paint.UNDERLINE_TEXT_FLAG != 0

            if (isUnderlined) {
                (currentView as TextView).paintFlags =
                    (currentView as TextView).paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
            } else {
                (currentView as TextView).paintFlags =
                    (currentView as TextView).paintFlags or Paint.UNDERLINE_TEXT_FLAG
            }
        }
        actionItalic.setOnClickListener {

            val currentTypeface = (currentView as TextView).typeface ?: Typeface.DEFAULT
            val isItalic = currentTypeface.isItalic

            if (isItalic) {
                (currentView as TextView).setTypeface(null, Typeface.NORMAL)
            } else {
                (currentView as TextView).setTypeface(null, Typeface.ITALIC)
            }

        }
        actionUpperCase.setOnClickListener {
            (currentView as TextView).text = (currentView as TextView).text.toString().uppercase()
        }
        actionLowerCase.setOnClickListener {
            (currentView as TextView).text = (currentView as TextView).text.toString().lowercase()
        }
    }

    private fun opacityLogic() = opacityLayoutView?.apply {
        sliderOpacity.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being started
                hideAllCorners()
                Utils.log("onStartTrackingTouch ${slider.value}")
            }

            override fun onStopTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being stopped
                Utils.log("onStopTrackingTouch ${slider.value}")

            }
        })
        sliderOpacity.addOnChangeListener { slider, value, fromUser ->
            // Responds to when slider's value is changed
            (currentView as TextView).setTextColor(Color.argb(value.toInt(), 250, 250, 250));
            val percentage = (value / 255 * 100).toInt()
            percentageTv.text = percentage.toString()
            Utils.log("addOnChangeListener ${slider.value}")
        }
    }

    private fun spacerLogic() = spacerLayoutView?.apply {
        sliderSpacing.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being started
                hideAllCorners()
                Utils.log("onStartTrackingTouch ${slider.value}")
            }

            override fun onStopTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being stopped
                Utils.log("onStopTrackingTouch ${slider.value}")

            }

        })
        sliderSpacing.addOnChangeListener { slider, value, fromUser ->
            // Responds to when slider's value is changed
            (currentView as TextView).letterSpacing = value
            Utils.log("addOnChangeListener ${slider.value}")
        }
    }

    private fun rotateLogic() = rotateLayoutView?.apply {


        seekbar.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being started
                hideAllCorners()
                Utils.log("onStartTrackingTouch ${slider.value}")
            }

            override fun onStopTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being stopped
                Utils.log("onStopTrackingTouch ${slider.value}")

            }

        })
        seekbar.addOnChangeListener { slider, value, fromUser ->
            // Responds to when slider's value is changed
            currentView?.rotation = value
            Utils.log("addOnChangeListener ${slider.value}")
        }
    }


    private fun dRotateLogic() = dRotateLayoutView?.apply {

        seekBarX.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being started
                hideAllCorners()
                Utils.log("onStartTrackingTouch ${slider.value}")

            }

            override fun onStopTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being stopped
                Utils.log("onStopTrackingTouch ${slider.value}")

            }

        })
        seekBarX.addOnChangeListener { slider, value, fromUser ->
            // Responds to when slider's value is changed
            currentView?.rotationY = value
            Utils.log("addOnChangeListener ${slider.value}")
        }

        //Perspective Y
        seekBarY.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being started
                hideAllCorners()
                Utils.log("onStartTrackingTouch ${slider.value}")

            }

            override fun onStopTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being stopped
                Utils.log("onStopTrackingTouch ${slider.value}")

            }

        })
        seekBarY.addOnChangeListener { slider, value, fromUser ->
            // Responds to when slider's value is changed
            currentView?.rotationX = value
            Utils.log("addOnChangeListener ${slider.value}")
        }
    }

    private fun Slider.setTrackColors() {
        val value = value
        val half = valueTo / 2
        val inactiveColor = ContextCompat.getColor(context, R.color.grey)
        val activeColor = ContextCompat.getColor(context, R.color.random)

        // Determine active and inactive colors based on slider value
        if (value == 0f) {
            trackActiveTintList = ContextCompat.getColorStateList(context, R.color.random)!!
            trackInactiveTintList = ContextCompat.getColorStateList(context, R.color.grey)!!
        } else if (value < 0) {
            val ratio = value / half // ratio of the filled track
            trackActiveTintList = ContextCompat.getColorStateList(context, R.color.random)!!
            trackInactiveTintList = ContextCompat.getColorStateList(context, R.color.grey)!!
            trackActiveTintList = ContextCompat.getColorStateList(context, R.color.grey)!!

            trackActiveTintList = ContextCompat.getColorStateList(context, R.color.grey)!!
        } else {
            val ratio = value / half // ratio of the filled track
            trackInactiveTintList = ContextCompat.getColorStateList(context, R.color.random)!!
            trackActiveTintList = ContextCompat.getColorStateList(context, R.color.grey)!!

            trackInactiveTintList = ContextCompat.getColorStateList(context, R.color.grey)!!
        }
    }

    private fun hideAllCorners() {
        draggableTextView.hideIcons(
            topLeftCornerIcon,
            topRightCornerIcon,
            bottomRightCornerIcon,
            bottomLeftCornerIcon,
        )
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
            viewStubColor
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
            makingViewsDraggable(data.viewType, data.x, data.y, data.data, priority = data.priority)

        }
    }

    private fun makingViewsDraggable(
        viewType: String,
        x: Float,
        y: Float,
        data: String,
        priority: Int,
    ) = binding.apply {
        if (viewType == "text") {
            draggableTextView =
                DraggableTextView(
                    this@EditorActivity, x, y, data,
                    currentPosition = { y, x ->
                        currentY = y
                        currentX = x
                        Utils.log("Current X: $currentX Current Y $currentY")
                    },
                    callback = this@EditorActivity,
                    onItemClick = { booleanCallback ->
                        boolTopBar = booleanCallback
                        toggleUndoRedo(booleanCallback)
                    },
                    currentView = { currentView, isSelected ->
                        this@EditorActivity.currentView = currentView
                        enableCornerListeners(currentView)
                        if (isSelected) {
                            mainOptionsRecyclerView.visibility = View.GONE
                            selectedOptionsRecyclerView.visibility = View.VISIBLE
                        }
                    }
                )

            editorContainer.addView(draggableTextView)
            viewModel.storeInitialPosition(draggableTextView)
            viewModel.addDynamicView(draggableTextView)
            layers(draggableTextView, data, priority)

            draggableTextView.enableDragAndDrop(
                topLeftCornerIcon,
                topRightCornerIcon,
                bottomRightCornerIcon,
                bottomLeftCornerIcon,
            )

        } else if (viewType == "image") {
            draggableImageView =
                DraggableImageView(
                    this@EditorActivity, x, y, data,
                    onItemClick = { booleanCallback ->
                        boolTopBar = booleanCallback
                        toggleUndoRedo(booleanCallback)
                    },
                    callback = this@EditorActivity

                ) { currentView, isSelected ->
                    this@EditorActivity.currentView = currentView
                    currentY = currentView.y
                    enableCornerListeners(currentView)
                    if (isSelected) {
                        if (stub.isInflated) {
                            hideAllStubs(0)
                        }
//                            stub.viewStub?.visibility = View.GONE
                        mainOptionsRecyclerView.visibility = View.VISIBLE
                        selectedOptionsRecyclerView.visibility = View.GONE
                    }
                }
            binding.editorLayout.addView(draggableImageView)

            viewModel.storeInitialPosition(draggableImageView)
            viewModel.addDynamicView(draggableImageView)
            layers(draggableImageView, data, priority)

            draggableImageView.enableDragAndDrop(
                topLeftCornerIcon,
                topRightCornerIcon,
                bottomRightCornerIcon,
                bottomLeftCornerIcon,
            )
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

    private fun alignLayoutLogic() = alignLayoutView?.apply {
        if (currentView is TextView) {
            alignLeft.setOnClickListener {
                (currentView as TextView).gravity = Gravity.CENTER or Gravity.START
            }
            alignCenter.setOnClickListener {
                (currentView as TextView).gravity = Gravity.CENTER
            }
            alignRight.setOnClickListener {
                (currentView as TextView).gravity = Gravity.CENTER or Gravity.END
            }
        }
    }

    private fun controlLayoutLogic() = controlLayoutView?.apply {
        var xDirection = 200f
        var yDirection = 200f

        actionDown.setOnClickListener {
            draggableTextView.hideIcons(
                topLeftCornerIcon,
                topRightCornerIcon,
                bottomRightCornerIcon,
                bottomLeftCornerIcon,
            )

            currentView?.y = currentView?.y!! + 10

        }
        actionUp.setOnClickListener {
            draggableTextView.hideIcons(
                topLeftCornerIcon,
                topRightCornerIcon,
                bottomRightCornerIcon,
                bottomLeftCornerIcon,
            )

            currentView?.y = currentView?.y!! - 10

        }
        actionLeft.setOnClickListener {
            draggableTextView.hideIcons(
                topLeftCornerIcon,
                topRightCornerIcon,
                bottomRightCornerIcon,
                bottomLeftCornerIcon,
            )
            currentView?.x = currentView?.x!! - 10

        }
        actionRight.setOnClickListener {
            draggableTextView.hideIcons(
                topLeftCornerIcon,
                topRightCornerIcon,
                bottomRightCornerIcon,
                bottomLeftCornerIcon,
            )
            currentView?.x = currentView?.x!! + 10

        }

        actionFlipHorizontally.setOnClickListener {
            draggableTextView.hideIcons(
                topLeftCornerIcon,
                topRightCornerIcon,
                bottomRightCornerIcon,
                bottomLeftCornerIcon,
            )

            val currentRotationY = currentView?.rotationY ?: 0f
            Utils.log("currentRotationX: $currentRotationY")
            val newRotationY = if (currentRotationY == 0f) 180f else 0f
            currentView?.rotationY = newRotationY
        }
        actionFlipVertically.setOnClickListener {
            draggableTextView.hideIcons(
                topLeftCornerIcon,
                topRightCornerIcon,
                bottomRightCornerIcon,
                bottomLeftCornerIcon,
            )
            val currentRotationX = currentView?.rotationX ?: 0f
            Utils.log("currentRotationX: $currentRotationX")
            val newRotationX = if (currentRotationX == 0f) 180f else 0f
            currentView?.rotationX = newRotationX
        }

        actionDelete.setOnClickListener {
            draggableTextView.hideIcons(
                topLeftCornerIcon,
                topRightCornerIcon,
                bottomRightCornerIcon,
                bottomLeftCornerIcon,
            )
            currentView?.gone()
            if (binding.stub.isInflated) {
                binding.stub.root.gone()
                binding.stubAlign.root.gone()
            }
//                            stub.viewStub?.visibility = View.GONE
            mainOptionsRecyclerView.visibility = View.VISIBLE
            selectedOptionsRecyclerView.visibility = View.GONE
        }
        actionCopy.setOnClickListener {
            xDirection += 20
            yDirection += 20
            draggableTextView.hideIcons(
                topLeftCornerIcon,
                topRightCornerIcon,
                bottomRightCornerIcon,
                bottomLeftCornerIcon,
            )
            Utils.log("Current X: $currentX Current Y $currentY")
            if (currentView is TextView) {
                val data = (currentView as TextView).text.toString()
                Utils.log("Data in view: $data")

                makingViewsDraggable(
                    "text", xDirection,
                    yDirection, data,
                    5
                )
            }
        }
        actionEdit.setOnClickListener {
            draggableTextView.hideIcons(
                topLeftCornerIcon,
                topRightCornerIcon,
                bottomRightCornerIcon,
                bottomLeftCornerIcon,
            )
            val currentText = (currentView as TextView).text.toString()
            val editBottomSheet = EditTextBottomSheet(currentText) { newText ->
                (currentView as TextView).text = newText
            }
            editBottomSheet.show(supportFragmentManager, "EditTextDialogFragment")
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
            btnDone.visibility = View.VISIBLE
            recyclerViewLayers.visibility = View.GONE
            btnBack.visibility = View.GONE
        }

        btnDone.setOnClickListener {
            isRecyclerViewVisible = false
            btnUndo.visibility = View.VISIBLE
            btnRefresh.visibility = View.VISIBLE
            btnRedo.visibility = View.VISIBLE
            btnBack.visibility = View.VISIBLE
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
            draggableTextView.hideIcons(
                topLeftCornerIcon,
                topRightCornerIcon,
                bottomRightCornerIcon,
                bottomLeftCornerIcon,
            )

        }
        btnRedo.setOnClickListener {
            val viewToRedo = viewModel.popLastRedo()
            viewToRedo?.let {
                viewModel.redoToInitialPosition(it)
            }
            draggableTextView.hideIcons(
                topLeftCornerIcon,
                topRightCornerIcon,
                bottomRightCornerIcon,
                bottomLeftCornerIcon,
            )

        }
        btnRefresh.setOnClickListener {
            viewModel.resetAllViewsToInitialPosition(viewModel.dynamicViewsList)
            draggableTextView.hideIcons(
                topLeftCornerIcon,
                topRightCornerIcon,
                bottomRightCornerIcon,
                bottomLeftCornerIcon,
            )

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
        Utils.log("Call Back From Listener")


    }

}
