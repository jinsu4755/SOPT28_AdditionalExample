package org.sopt.bottomsheetdialog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import org.sopt.bottomsheetdialog.custom.CustomBottomSheetDialog
import org.sopt.bottomsheetdialog.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initClickEvent()
    }

    private fun makeBottomSheetDialog() {
        val customBottomSheetDialog = CustomBottomSheetDialog()
        customBottomSheetDialog.setCallbackButtonClickListener {
            startActivity(Intent(this@MainActivity, ResultActivity::class.java))
        }
        customBottomSheetDialog.show(supportFragmentManager, "custom")
    }

    private fun initClickEvent() {
        binding.btnDialog.setOnClickListener {
            makeBottomSheetDialog()
        }
    }
}