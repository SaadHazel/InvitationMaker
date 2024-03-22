package com.saad.invitationmaker.features.editor.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.saad.invitationmaker.R
import com.saad.invitationmaker.features.editor.callbacks.ExitConfirmationDialogListener

class ExitDialog : DialogFragment() {

    private var listener: ExitConfirmationDialogListener? = null

    fun setListener(listener: ExitConfirmationDialogListener) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.exit_editing_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.btn_yes).setOnClickListener {
            listener?.onYesClicked()
            dismiss()
        }

        view.findViewById<Button>(R.id.btn_no).setOnClickListener {
            listener?.onNoClicked()
            dismiss()
        }

        /*  view.findViewById<Button>(R.id.btn_savedrafts).setOnClickListener {
              listener?.onSaveDraftClicked()
              dismiss()
          }*/
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}