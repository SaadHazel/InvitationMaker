package com.saad.invitationmaker.features.editor.bottomSheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.saad.invitationmaker.app.utils.log
import com.saad.invitationmaker.core.extensions.showSnackBarMessage
import com.saad.invitationmaker.databinding.FragmentSaveBottomSheetBinding
import com.saad.invitationmaker.features.editor.utils.SaveDesignMethods

class SaveBottomSheet(private val editorView: ConstraintLayout) : BottomSheetDialogFragment() {
    private var binding: FragmentSaveBottomSheetBinding? = null
    private val tag = "SaveBottomSheet"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSaveBottomSheetBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.textSaveAsImage?.setOnClickListener {
            val filePath = SaveDesignMethods.getHighQualitySaveImageFilePath(
                requireContext(),
                editorView,

                )
            log(tag, "FilePath: $filePath")
            requireContext().showSnackBarMessage(
                editorView,
                "Image Saved in folder \"Pictures/Invitation Maker\" in Storage"
            )
            dismiss()
        }
        binding?.textSaveAsPdf?.setOnClickListener {
            val filePath = SaveDesignMethods.saveAsPDF(requireContext(), editorView)
            log(tag, "FilePath: $filePath")
            requireContext().showSnackBarMessage(
                editorView,
                "Pdf Saved in folder \"Document/Invitation Maker\" in Storage"
            )
            dismiss()

        }
    }

}