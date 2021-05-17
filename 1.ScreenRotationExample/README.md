## 화면 회전 예제(Activity 회전에도 상태 저장하기)

### Activity의 상태 저장이 필요한 이유

**Activity가 종료될 수 있는 상황들**

1. 뒤로가기 키 혹은 onBackPressed()가 호출되는경우
2. finish() 메소드 혹은 관련 메소드 실행시
3. 시스템으로 인한 종료.

이 중에서 3번을 자세하게 알아보면

Android OS에서 모든 앱은 우선순위가 있습니다. 기본 제공하는 전화,카메라와 같은 앱도 우리가 만드는 앱과 동일한 레벨이지만 기본 앱은 시스템 권한을 사용할 수 있고 우선순위가 높은 차이가 있습니다.

우리가 만드는 앱에는 현재 사용자가 사용하고있는 앱(포그라운드 앱)일 수록 우선순위가 높고
우리가 사용하지 않고(백그라운드)있는 앱은 우선순위가 낮습니다.

이러한 우선순위란 앱을 사용함에 있어 사용하는 기기에서 메모리가 부족할때 강제 종료하는 앱을 정하는 기준이라고 보면 이해하기 쉽습니다.

예를 들어 지금 사용자가 사용하는 앱을 시스템이 강제 종료해선 안되겠죠?
반대로 백그라운드에서 돌고있는 앱은 언제든 시스템이 메모리가 부족할때 강제 종료하여 부족한 메모리를
받아올 수 있습니다.
그러나 백그라운드에 있어도 전화앱 자체가 죽어버리면 안되겠죠?

이러한 상황에서 Android OS는 각 앱이라는 프로세스에 우선순위를 지정하여 관리합니다.

또한 화면을 회전할 경우에도 Activity가 onDestroy가 호출되고 onCreate가 호출되며 재 실행됩니다.

위와 같은 경우에 Activity 상태 저장이 필요합니다.

---

상태 저장 없이 화면을 회전시켜본다면 아래와 같은 일이 벌어집니다.

<table>
    <tr>
        <td>
            <center>
           		<img src="./README_photo/nonStatusSaveImage1.png"style="zoom:40%;" />			</center>
        </td>
        <td>
            <center>
            	<img src="./README_photo/nonStatusSaveImage2.png"style="zoom:60%;" />
            </center>
        </td>
    </tr>
</table>

화면 회전시 생명주기 로그를 찍어보면 다음과 같습니다.

```
// Activity 실행
D/[Lifecycle]MainActivity: onCreated
D/[Lifecycle]MainActivity: onStart
D/[Lifecycle]MainActivity: onResume

// 화면 회전
D/[Lifecycle]MainActivity: onPause
D/[Lifecycle]MainActivity: onStop
D/[Lifecycle]MainActivity: onSaveInstanceState
D/[Lifecycle]MainActivity: onDestroy
D/[Lifecycle]MainActivity: onCreated
D/[Lifecycle]MainActivity: onStart
D/[Lifecycle]MainActivity: onResume

```

위와 같이 Destroy가 호출됨으로 TextView에 노출되던 데이터 상태를 잃어버리게 됩니다.

그렇다면 우리는 이 데이터 상태를 잃지 않기 위해서는 데이터를 어떤 곳에

저장하고, 복원할 수 있어야 합니다.

---

위 생명주기 로그를 보면 신기한 이름이 하나 있습니다.

`onSaveInstanceState` 이름부터 어떤 상태를 저장하고 아래 그림을 보면

<img src="https://miro.medium.com/max/482/1*DCo7awxJ3KhnW88h365vhA.png" alt="img" style="zoom: 67%;" />

사진 출처:[The Android Lifecycle cheat sheet — part I: Single Activities | by Jose Alcérreca | Android Developers | Medium](https://medium.com/androiddevelopers/the-android-lifecycle-cheat-sheet-part-i-single-activities-e49fd3d202ab)

`onSaveInstanceState()` 라는 친구에서 무언가 저장하고
`onRestoreInstanceState()`라는 친구에서 무언가 복구할것이라 예측할 수 있습니다.

---

`onSaveInstanceState()` 라는 친구 아래와 같이 데이터를 저장할 수 있습니다.

![photo](/README_photo/onSaveINstanceState.png)

[코드 복사용 링크](https://carbon.now.sh/embed?bg=rgba(255%2C255%2C255%2C0)&t=one-dark&wt=none&l=auto&ds=true&dsyoff=20px&dsblur=68px&wc=true&wa=true&pv=56px&ph=56px&ln=false&fl=1&fm=Hack&fs=14px&lh=133%&si=false&es=2x&wm=false&code=%20%20%20%20%2F*%20%EC%97%91%ED%8B%B0%EB%B9%84%ED%8B%B0%EA%B0%80%20%ED%99%94%EB%A9%B4%20%ED%9A%8C%EC%A0%84%20%ED%98%B9%EC%9D%80%20%EC%9E%AC%EC%8B%A4%ED%96%89%EC%8B%9C%20%EC%96%B4%EB%96%A4%20%EB%8D%B0%EC%9D%B4%ED%84%B0%20%EC%83%81%ED%83%9C%EB%A5%BC%20%EC%A0%80%EC%9E%A5%ED%95%B4%EC%95%BC%ED%95%A0%EB%95%8C%20%ED%98%B8%EC%B6%9C%ED%95%98%EB%8A%94%20%EC%83%9D%EB%AA%85%EC%A3%BC%EA%B8%B0%0A%20%20%20%20*%20%EC%97%91%ED%8B%B0%EB%B9%84%ED%8B%B0%EA%B0%80%20Destroy%20%EB%90%98%EA%B8%B0%20%EC%A0%84%EC%97%90%20%ED%98%B8%EC%B6%9C%EB%90%9C%EB%8B%A4.*%2F%0A%20%20%20%20override%20fun%20onSaveInstanceState(outState%3A%20Bundle)%20%7B%0A%20%20%20%20%20%20%20%20%2F%2F%20bundle%20%EC%9D%B4%EB%9D%BC%EB%8A%94%20%EC%83%81%EC%9E%90%EC%97%90%20%EB%8D%B0%EC%9D%B4%ED%84%B0%EB%A5%BC%20%EC%A0%80%EC%9E%A5%0A%20%20%20%20%20%20%20%20%2F%2F%20intent%20%EA%B0%80%20%EB%8D%B0%EC%9D%B4%ED%84%B0%EB%A5%BC%20%EC%A0%84%EB%8B%AC%ED%95%98%EB%8A%94%20%EC%88%98%EB%8B%A8%EC%9D%B4%EB%9D%BC%EB%A9%B4%20bundle%20%EC%9D%80%20%EA%B7%B8%20%EB%8D%B0%EC%9D%B4%ED%84%B0%EB%A5%BC%20%EC%A0%80%EC%9E%A5%ED%95%98%EB%8A%94%20%EC%83%81%EC%9E%90%0A%20%20%20%20%20%20%20%20%2F%2F%20bundle%20%EC%9D%80%20%EC%97%AC%EB%9F%AC%EA%B0%80%EC%A7%80%20%ED%83%80%EC%9E%85%EC%9D%84%20%EC%A0%80%EC%9E%A5%ED%95%98%EB%8A%94%20MAP%20%ED%81%B4%EB%9E%98%EC%8A%A4%0A%20%20%20%20%20%20%20%20outState.putInt(%22score%22%2C%20score)%0A%20%20%20%20%20%20%20%20super.onSaveInstanceState(outState)%0A%20%20%20%20%7D)

데이터의 복구는 다음과 같이 할 수 있습니다. onCreate는 Activity의 첫 실행지점이면서 시스템에 의해 종료되 재성성시 시작되는 위치이므로 매개변수로 savedInstanceState를 가지고 있습니다.
해당 데이터가 있을 경우 받아오고 아닐경우 기존 값을 보내는 부분을 추가할 수 있습니다.

![photo](/README_photo/RestoreOnCreate.png)

[코드 복사용 링크](https://carbon.now.sh/embed?bg=rgba(171%2C+184%2C+195%2C+1)&t=one-dark&wt=none&l=auto&ds=true&dsyoff=20px&dsblur=68px&wc=true&wa=true&pv=56px&ph=56px&ln=false&fl=1&fm=Hack&fs=14px&lh=133%&si=false&es=2x&wm=false&code=%20%20%20%20override%20fun%20onCreate(savedInstanceState%3A%20Bundle%3F)%20%7B%0A%20%20%20%20%20%20%20%20super.onCreate(savedInstanceState)%0A%20%20%20%20%20%20%20%20setContentView(binding.root)%0A%20%20%20%20%20%20%20%20%2F%2F%20%ED%95%98%EB%8B%A8%EC%97%90%EC%84%9C%20%EC%A0%80%EC%9E%A5%ED%95%9C%20bundle%20%EC%9D%84%20onCreate%20%EC%97%90%EC%84%9C%20%EA%B0%80%EC%A0%B8%EC%98%AC%20%EC%88%98%20%EC%9E%88%EB%8B%A4.%0A%20%20%20%20%20%20%20%20%2F*%EB%A7%8C%EC%95%BD%20%EC%A0%80%EC%9E%A5%EB%90%9C%20bundle%20%EC%9D%B4%20%EC%9E%88%EB%8B%A4%EB%A9%B4%20%EA%B0%80%EC%A0%B8%EC%98%A4%EA%B3%A0%20%EC%97%86%EC%9C%BC%EB%A9%B4%20%EC%B4%88%EA%B8%B0%20score%20%EA%B0%92%EC%9D%84%20%EC%A0%84%EB%8B%AC%ED%95%9C%EB%8B%A4.*%2F%0A%20%20%20%20%20%20%20%20score%20%3D%20savedInstanceState%3F.getInt(%22score%22)%20%3F%3A%200%0A%20%20%20%20%20%20%20%20initView(score)%0A%20%20%20%20%20%20%20%20setClickEvent()%0A%20%20%20%20%7D)

![photo](/README_photo/RestoreInstanceState.png)

[코드 복사용 링크](https://carbon.now.sh/embed?bg=rgba(171%2C+184%2C+195%2C+1)&t=one-dark&wt=none&l=auto&ds=true&dsyoff=20px&dsblur=68px&wc=true&wa=true&pv=56px&ph=56px&ln=false&fl=1&fm=Hack&fs=14px&lh=133%&si=false&es=2x&wm=false&code=%0A%20%20%20%20%2F%2FonCreate%20%EC%97%90%EC%84%9C%20null%20check%20%EB%A5%BC%20%ED%95%98%EC%A7%80%20%EC%95%8A%EA%B3%A0%20%EC%8B%B6%EB%8B%A4%EB%A9%B4%20%EC%95%84%EB%9E%98%20%EB%B0%A9%EB%B2%95%EC%9C%BC%EB%A1%9C%20%EC%82%AC%EC%9A%A9%ED%95%98%EB%8A%94%20%EB%B0%A9%EB%B2%95%EB%8F%84%20%EC%9E%88%EB%8B%A4.%0A%0A%20%20%20%20override%20fun%20onRestoreInstanceState(savedInstanceState%3A%20Bundle)%20%7B%0A%20%20%20%20%20%20%20%20super.onRestoreInstanceState(savedInstanceState)%0A%20%20%20%20%20%20%20%20binding.scoreText.text%20%3D%20savedInstanceState.getInt(%22score%22).toString()%0A%20%20%20%20%7D%0A)

