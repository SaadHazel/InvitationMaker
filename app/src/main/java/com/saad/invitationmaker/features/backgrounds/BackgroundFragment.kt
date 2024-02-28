package com.saad.invitationmaker.features.backgrounds

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.saad.invitationmaker.R
import com.saad.invitationmaker.app.utils.log
import com.saad.invitationmaker.core.extensions.gone
import com.saad.invitationmaker.core.extensions.navigate
import com.saad.invitationmaker.core.extensions.visible
import com.saad.invitationmaker.databinding.FragmentBackgroundBinding
import com.saad.invitationmaker.features.backgrounds.adapters.BackgroundParentAdapter
import com.saad.invitationmaker.features.backgrounds.callbacks.BackgroundCallBack
import com.saad.invitationmaker.features.backgrounds.models.CategoryModel
import com.saad.invitationmaker.features.editor.EditorActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream


@AndroidEntryPoint
class BackgroundFragment : Fragment() {
    private var binding: FragmentBackgroundBinding? = null
    private val viewModel: BackgroundViewModel by viewModels()
    private val tag = "BackgroundFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBackgroundBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loaderForData(true)
        viewModel.AllBackgrounds.observe(viewLifecycleOwner) { all ->
            if (all == null) return@observe
            loaderForData(false)
            log("BackgroundViewModel", "In fragment ${all[1].category}")
            mainRecyclerView(all)
        }

        getBackground()

    }

    private fun getBackground() = binding?.apply {
        cameraBtn.setOnClickListener {
            // Launch the camera intent
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)

        }

        galleryBtn.setOnClickListener {
            openGallery()
        }
    }

    private fun mainRecyclerView(data: List<CategoryModel>) = binding?.apply {

        parentRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        parentRecyclerView.adapter = BackgroundParentAdapter(data, object : BackgroundCallBack {
            override fun onBackgroundClick(url: String) {
                val i = Intent(activity, EditorActivity::class.java)
                i.putExtra("url", url)
                startActivity(i)
                (activity as Activity?)!!.overridePendingTransition(0, 0)
            }

            override fun onCategoryClick(data: List<CategoryModel>) {
                navigate(
                    R.id.nav_host_fragment,
                    BackgroundFragmentDirections.toCategoriesFragmentFromBg(data.toTypedArray())
                )
            }
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val photo: Bitmap? = data!!.extras!!["data"] as Bitmap?
            Log.d("ImageFromPrev", "Photo $photo")
            if (photo != null) {
                val intent = Intent(activity, EditorActivity::class.java)
                val bs = ByteArrayOutputStream()
                photo.compress(Bitmap.CompressFormat.PNG, 100, bs)
                intent.putExtra("imagePath", bs.toByteArray())
                startActivity(intent)
            }
        } else if (requestCode == PICK_IMAGE_REQUEST && resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImageUri: Uri? = data?.data
            if (selectedImageUri != null) {
                val intent = Intent(activity, EditorActivity::class.java)
                intent.putExtra("imagePathGallery", selectedImageUri.toString())
                startActivity(intent)
            }
        }
    }

    private fun loaderForData(toggle: Boolean) = binding?.apply {
        if (toggle) {
            progressBar.visible()
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                progressBar.gone()
            }
        }

    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
        const val PICK_IMAGE_REQUEST = 2
    }
}