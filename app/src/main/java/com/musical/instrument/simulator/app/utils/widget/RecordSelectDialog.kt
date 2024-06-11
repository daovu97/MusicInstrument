package com.musical.instrument.simulator.app.utils.widget

import android.app.Dialog
import android.os.Bundle
import android.view.WindowManager
import com.musical.instrument.simulator.app.base.BaseDialogFragment
import com.musical.instrument.simulator.app.base.setOnSingleClickListener
import com.musical.instrument.simulator.app.databinding.DialogRecordBinding

class RecordSelectDialog : BaseDialogFragment<DialogRecordBinding>(DialogRecordBinding::inflate) {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            )
        }
    }

    interface Listener {
        fun onRecordSelectMicro()
        fun onRecordSelectPiano()

        fun onRecordSelectCancel()
    }

    private val callback: Listener
        get() = (parentFragment as? Listener)
            ?: (activity as? Listener)
            ?: error("No callback for ConfirmDialog")

    override fun setupView() {
        super.setupView()

        binding.layoutMicro.setOnSingleClickListener {
            callback.onRecordSelectMicro()
            dismiss()
        }

        binding.layoutPiano.setOnSingleClickListener {
            callback.onRecordSelectPiano()
            dismiss()
        }

        binding.buttonCloseDialogRecorde.setOnSingleClickListener {
            callback.onRecordSelectCancel()
            dismiss()
        }
    }
}