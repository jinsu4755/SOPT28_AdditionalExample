package org.sopt.dialogexample

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import org.sopt.dialogexample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setButtonEvent(binding)
    }

    private fun setButtonEvent(binding: ActivityMainBinding) {
        with(binding) {
            showMessageDialogButton.setOnClickListener { showMessageDialog() }
            showListDialogButton.setOnClickListener { showListDialog() }
        }
    }

    private fun showMessageDialog() {
        /*메시지를 보여주는 다이얼로그의 경우는 아래와 같이 만들 수 있습니다.
        * Builder를 이용하여 create과정까지 dialog를 담는 변수를 만들 수도 있습니다.
        * 주로 Ok버튼은 가장 오른쪽에
        * Cancel 과 같은 버튼은 Ok 버튼 왼쪽에
        * 중립적인 이벤트를 처리하는 버튼은 가장 왼쪽에 있습니다.
        *
        * 다이얼로그 빌더에 각 부분은 보이는 것과 같은 역할을 합니다.*/
        AlertDialog.Builder(this)
            .setMessage("메시지를 보여주는 다이얼로그 입니다!")
            .setCancelable(false) // 다이얼로그 바깥을 눌러 캔슬되지 않도록 설정
            .setPositiveButton("긍정") { dialog, which ->
                toastMessage("긍정적인 이벤트 발생!")
            }.setNegativeButton("부정") { dialog, which ->
                toastMessage("부정적인 이벤트 발생!")
            }.setNeutralButton("중립") { dialog, which ->
                toastMessage("중립적 이벤트 발생!")
            }.create() // 다이얼로그 생성
            .show() // 다이얼로그 보여주는 메소드
    }

    private fun showListDialog() {
        
    }

    private fun toastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT)
            .show()
    }
}
