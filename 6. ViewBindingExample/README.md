# ViewBinding 한 상 차림 in Fragment

- 여태까지 작성한 코드를 보면 binding 객체를 두 가지로 다음과 같이
    - 한 개는 onCreateView에서 inflate 시키고 onDestroyView에서 해제 시키기 위한 mutable variable
    - 또 다른 한 개는 위 변수의 getter로 작동하는 variable

구분할 수 있습니다.

- 그렇다면 View Binding을 사용할 때에는 이런 방법밖에 없을까요? 다양한 방법으로 ViewBinding을 가져와봅시다

0. 기존 방식(난이도: 하 ~ 중하)
```kotlin
class SampleFragment : Fragment() {
    private var _binding: FragmentSampleBinding? = null
    private val binding: FragmentSampleBinding
        get() = _binding ?: IllegalArgumentException("Binding is not initialized")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, resId, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
```

1. Binding 객체를 만들어주는 공통 클래스를 만들 수 있다(난이도: 중 ~ 중상)

```kotlin
/*
* BindingFragment
* Created by HyunWoo Lee
* at 6/12, 2021
*
*/
abstract class BindingFragment<V : ViewBinding>(
    @LayoutRes private val resId: Int
) : Fragment() {
    private var _binding: V? = null
    protected val binding: V
        get() = requireNotNull(_binding) { IllegalArgumentException("Binding is not initialized") }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, resId, container, false)
        return binding.root
    }

    // 뷰의 생명주기(viewLifeCycle)와 객체의 생명주기(lifeCycle)가 다르기 때문에 발생하는 Memory Leak을 해결하기 위한 수단
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
```

- 왜 Abstract Class(추상클래스)로 만드셨나요?
    - 이 클래스가 객체로 만들어질 필요가 없을 것 같아서, 오로지 상속을 하기 위한 클래스로 만들기 위해 abstract 키워드를 걸었습니다.
- @LayoutRes
    - Layout이든, Drawable이든 어쨌든 리소스를 넘겨줄 때에는 Int 형식으로 넘겨줄텐데 해당 리소스와 호환되지 않을대 경고 메시지를 주게하는 메타 어노테이션입니다.
    - 이와 같은 어노테이션을 활용하면 실수를 많이 줄일 수 있겠죠?
- requireNotNull
    - Not null assertion(!!)은 무조건 NullPointerException을 방출합니다. 이는 제가 커스텀을 할 수 없는 예외이기에
    - 개발자가 임의로 커스텀할 수 있는 Exception을 만들기 위해서, 그리고 문법상 조금 더 깔끔한 코드를 작성하기 위해 requireNotNull을 사용했습니다.
- View Binding 쓴다면서 왜 DataBindingUtil인가요?

```java

// ViewBinding File
/** A type which binds the views in a layout XML to fields. */
public interface ViewBinding {
    /**
     * Returns the outermost {@link View} in the associated layout file. If this binding is for a
     * {@code <merge>} layout, this will return the first view inside of the merge tag.
     */
    @NonNull
    View getRoot();
}


// Definition of ViewDataBinding
public abstract class ViewDataBinding extends BaseObservable implements ViewBinding

/**
 * Utility class to create {@link ViewDataBinding} from layouts.
 */
public class DataBindingUtil
```

    - DataBindingUtil은 View를 inflate해서 ViewDataBinding 클래스의 형태로 넘겨주는 유틸 클래스입니다.
    - 그런데 ViewBinding은 ViewDataBinidng의 부모 클래스이네요?
    - 결국 DataBindingUtil을 활용해서 저는 ViewBinding 클래스를 활용하기 위해서 DataBindingUtil을 사용한 것입니다.
    - 아 그리고 DataBinding과 ViewBinding의 주요 차이점은 저런 클래스에서 비롯된 것이 아니라 View XML의 루트 태그가 <layout>인지 아닌지에 따라 결정이 됩니다.
        - 루트 태그가 layout이면 데이터바인딩이 되고 아니면 뷰바인딩 형태로 바인딩 객체를 넘겨받습니다.
        - 만약에 gradle에서 dataBinding = true를 설정 안하면 View Binding밖에 설정이 안됩니다!

이를 상속시킵니다.

```kotlin
class InheritBindingFragment :
    BindingFragment<FragmentInheritBindingBinding>(R.layout.fragment_inherit_binding) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }
}
```

2. Delegate Pattern(난이도: 중상 ~ 상)

이...일단 코드를 보시죠 아 그 전에

```
    implementation "androidx.lifecycle:lifecycle-runtime-ktx:2.3.1"
    implementation "androidx.lifecycle:lifecycle-common-java8:2.3.1"
```

이걸 app 단위의 build.gradle에 추가해야합니다.

```kotlin
class AutoClearedValue<T : Any>(val fragment: Fragment) : ReadWriteProperty<Fragment, T> {
    private var _value: T? = null

    init {
        // Fragment의 생명주기를 단다는 것입니다.
        fragment.lifecycle.addObserver(object: DefaultLifecycleObserver {'
            // Fragment 객체가 생성(onCreate)될 때
            override fun onCreate(owner: LifecycleOwner) {
                // Fragment의 Viwe 생명주기를 관찰합니다.
                fragment.viewLifecycleOwnerLiveData.observe(fragment) { viewLifecycleOwner ->
                    // onDestroy(View가)할 때 해당 Value을 해제하는 것입니다.
                    viewLifecycleOwner?.lifecycle?.addObserver(object: DefaultLifecycleObserver {
                        override fun onDestroy(owner: LifecycleOwner) {
                            _value = null
                        }
                    })
                }
            }
        })
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        return _value ?: throw IllegalStateException(
            "should never call auto-cleared-value get when it might not be available"
        )
    }

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) {
        _value = value
    }
}
```

코드가 좀 어렵죠? 사실 이건 그냥 util 패키지에 그냥 ~~쳐~~박아두시면 됩니다. 가장 중요한 코드는

```kotlin
// ViewBinding 객체만 런타임에서 메모리 해제를 하고 싶다면?
fun <T : ViewBinding> Fragment.viewBinding() = AutoClearedValue<T>(this)

// 다른 객체도 위와 같이 해제하고 싶다면?
fun <T : Any> Fragment.autoCleared() = AutoClearedValue<T>(this)
```

이런 함수를 만듭니다.

그렇다면 Fragment에서는 다음과 같이 사용합니다.

```kotlin
class BindingDelegateFragment : Fragment() {
    private val binding by viewBinding<FragmentBindingDelegateBinding>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }
}
```

이와 같이 by를 통해 객체를 생성받는(것처럼 보이는 거긴 합니다만) 방식을 위임 방식(Delegate Pattern)이라고 합니다.
AutoClearedValue 객체를 실제 FragmentBindingDelegateBinding처럼 사용하는거죠!
