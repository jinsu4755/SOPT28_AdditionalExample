package org.sopt.examplescreenrotation

import android.app.Application
import org.sopt.examplescreenrotation.util.LifecycleLogger

class ScreenRotationApplication : Application() {
    /*Application Class
    * 어플리케이션 컴포넌트 사이에서 공동으로(전역으로) 상태를 유지하기 위한 기본 클래스
    * 앱이 실행될때 가장 먼저 초기화 됨
    * Application 을 상속하여 만들 수 있음
    * 어플리케이션 사이 컴포넌트들이 공동으로 상태를 유지하기 위해 사용하기 때문에
    * 공통되는 내용(맴버 변수 or 메소드)를 작성하면 어디서든 context 를 이용해 접근 가능
    * manifest 에 name 에 해당 클래스의 이름을 등록해줘야 한다.
    * 자세한건 공식 문서 확인하기
    * https://developer.android.com/reference/android/app/Application */
    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(LifecycleLogger())
    }
}
