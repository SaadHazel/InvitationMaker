package com.saad.invitationmaker.features.editor.bottomSheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.saad.invitationmaker.databinding.FragmentEditTextBottomSheetBinding

class EditTextBottomSheet(
    private val currentText: String,
    private val callBack: (data: String) -> Unit,
) :
    BottomSheetDialogFragment() {
    private var binding: FragmentEditTextBottomSheetBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentEditTextBottomSheetBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.inputText?.setText(currentText)

        binding?.imageView3?.setOnClickListener {
            callBack(binding?.inputText?.text.toString())
            binding?.inputText?.text?.clear()
            this.dismiss()
        }
        binding?.imageView2?.setOnClickListener {
            this.dismiss()
        }
    }
}