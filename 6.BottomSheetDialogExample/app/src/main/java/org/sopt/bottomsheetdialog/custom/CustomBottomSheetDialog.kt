package org.sopt.bottomsheetdialog.custom

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.sopt.bottomsheetdialog.databinding.LayoutCustomBottomSheetBinding

class CustomBottomSheetDialog : BottomSheetDialogFragment() {
    private lateinit var binding : LayoutCustomBottomSheetBinding
    private var callbackButtonClickListener: (() -> Unit?)? = null // 콜백

    //Bottom Sheet Dialog 구현부분
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutCustomBottomSheetBinding.inflate(inflater, container, false)

        binding.clButton1.setOnClickListener { makeAlertDialog("버튼1", "버튼1이 클릭 되었습니다!") }
        binding.clButton2.setOnClickListener { makeAlertDialog("버튼2", "버튼2가 클릭 되었습니다!") }
        binding.clCallback.setOnClickListener {
            callbackButtonClickListener?.invoke()
            dismiss()
        }
        binding.clExit.setOnClickListener { dismiss() }

        return binding.root
    }

    fun setCallbackButtonClickListener(listener: () -> Unit) {
        this.callbackButtonClickListener = listener
    }

    private fun makeAlertDialog(title : String, message : String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(
                "확인"
            ) { _: DialogInterface?, _: Int -> }
            setNegativeButton("취소") { _: DialogInterface?, _: Int ->  }
        }.show()
    }
}