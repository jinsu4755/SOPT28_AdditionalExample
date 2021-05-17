package org.sopt.examplescreenrotation.util

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log

class LifecycleLogger : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Log.d("$TAG${activity.localClassName}", "onCreated")
    }

    override fun onActivityStarted(activity: Activity) {
        Log.d("$TAG${activity.localClassName}", "onStart")
    }

    override fun onActivityResumed(activity: Activity) {
        Log.d("$TAG${activity.localClassName}", "onResume")
    }

    override fun onActivityPaused(activity: Activity) {
        Log.d("$TAG${activity.localClassName}", "onPause")
    }

    override fun onActivityStopped(activity: Activity) {
        Log.d("$TAG${activity.localClassName}", "onStop")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        Log.d("$TAG${activity.localClassName}", "onSaveInstanceState")
    }

    override fun onActivityDestroyed(activity: Activity) {
        Log.d("$TAG${activity.localClassName}", "onDestroy")
    }

    companion object {
        private const val TAG = "[Lifecycle]"
    }
}
