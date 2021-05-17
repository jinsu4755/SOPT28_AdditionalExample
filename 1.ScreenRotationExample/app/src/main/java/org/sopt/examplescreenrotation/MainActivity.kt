package org.sopt.examplescreenrotation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.sopt.examplescreenrotation.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var score = 0

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        // 하단에서 저장한 bundle 을 onCreate 에서 가져올 수 있다.
        /*만약 저장된 bundle 이 있다면 가져오고 없으면 초기 score 값을 전달한다.*/
        score = savedInstanceState?.getInt("score") ?: 0
        initView(score)
        setClickEvent()
    }

    private fun initView(savedScore: Int) {
        with(binding) {
            scoreText.text = savedScore.toString()
        }
    }

    private fun setClickEvent() {
        with(binding) {
            increaseScoreButton.setOnClickListener { increaseScoreEvent() }
            decreaseScoreButton.setOnClickListener { decreaseScoreEvent() }
        }
    }

    private fun increaseScoreEvent() {
        score++
        updateScoreText()
    }

    private fun decreaseScoreEvent() {
        score--
        updateScoreText()
    }

    private fun updateScoreText() {
        binding.scoreText.text = score.toString()
    }

    /*
    onCreate 에서 null check 를 하지 않고 싶다면 아래 방법으로 사용하는 방법도 있다.

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        binding.scoreText.text = savedInstanceState.getInt("score").toString()
    }
    */

    /* 엑티비티가 화면 회전 혹은 재실행시 어떤 데이터 상태를 저장해야할때 호출하는 생명주기
    * 엑티비티가 Destroy 되기 전에 호출된다.*/
    override fun onSaveInstanceState(outState: Bundle) {
        // bundle 이라는 상자에 데이터를 저장
        // intent 가 데이터를 전달하는 수단이라면 bundle 은 그 데이터를 저장하는 상자
        // bundle 은 여러가지 타입을 저장하는 MAP 클래스
        outState.putInt("score", score)
        super.onSaveInstanceState(outState)
    }
}
