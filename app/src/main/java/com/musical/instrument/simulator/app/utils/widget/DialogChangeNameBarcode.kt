package com.musical.instrument.simulator.app.utils.widget

import android.content.DialogInterface
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.musical.instrument.simulator.app.R
import com.musical.instrument.simulator.app.base.BaseDialogFragment
import com.musical.instrument.simulator.app.databinding.DialogChangenameBarcodeBinding
import kotlinx.coroutines.launch

const val ARG_EDT = "ARG_EDT"

class DialogChangeNameBarcode :
    BaseDialogFragment<DialogChangenameBarcodeBinding>(DialogChangenameBarcodeBinding::inflate) {
    interface Listener {
        fun dialogChangeNameBarcodeOnCancel()

        fun dialogChangeNameBarcodeOnClickOk(value: String)
    }

    private val callback: Listener
        get() = (parentFragment as? Listener) ?: (activity as? Listener)
        ?: error("No callback for ConfirmDialog")


    override fun setupView() {
        super.setupView()
        val scannedResult = arguments?.getString(ARG_EDT)
        if (scannedResult?.trim().isNullOrBlank()) {
            binding.baseTextview8.text = getText(R.string.name)
        }
        binding.edtBarcode.setText(scannedResult)
        binding.txtOk.setOnClickListener {
            callback.dialogChangeNameBarcodeOnClickOk(binding.edtBarcode.text.toString().trim())
            dismiss()
        }
        binding.txtCancel.setOnClickListener { callback.dialogChangeNameBarcodeOnCancel(); dismiss() }
        lifecycleScope.launch {
            binding.edtBarcode.requestFocus()
        }
    }

    companion object {
        fun newInstance(
            scanningResult: String
        ): DialogChangeNameBarcode = DialogChangeNameBarcode().apply {
            arguments = Bundle().apply {
                putString(ARG_EDT, scanningResult)
            }
        }

    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
    }
}