package org.sopt.snackbarexample

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import org.sopt.snackbarexample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setButtonClickEvent(binding)
    }

    private fun setButtonClickEvent(binding: ActivityMainBinding) {
        with(binding) {
            showMessageSnackBarButton.setOnClickListener { showMessageSnackBar(binding) }
            showHasButtonSnackBar.setOnClickListener { showSnackBarWithButton(binding) }
            showCustomSnackBar.setOnClickListener { }
        }
    }

    private fun showMessageSnackBar(binding: ActivityMainBinding) {
        Snackbar.make(binding.root, "메시지만 있습니다~", Snackbar.LENGTH_SHORT)
            .show()
    }

    private fun showSnackBarWithButton(binding: ActivityMainBinding) {
        Snackbar.make(binding.root, "버튼이 있네요!", Snackbar.LENGTH_INDEFINITE)
            .setAction("close") {
                Toast.makeText(this, "스넥바 버튼 이벤트!!!", Toast.LENGTH_SHORT)
                    .show()
            }.show()
    }
}
