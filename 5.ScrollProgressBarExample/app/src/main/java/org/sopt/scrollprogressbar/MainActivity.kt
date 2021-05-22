package org.sopt.scrollprogressbar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.sopt.scrollprogressbar.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setScrollEvent(binding)
    }

    private fun setScrollEvent(binding: ActivityMainBinding) {
        with(binding) {
            scrollContainer.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                val scrollLength = scrollContainer.getChildAt(0).height - scrollContainer.height

                scrollProgressBar.apply {
                    max = scrollLength
                    progress = scrollY
                }
            }
        }
    }
}
