package dev.demilab.cardholder_android

import android.app.Application
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import dev.demilab.cardholder_android.activity.MainActivity

class KaartholderApplication : Application(), LifecycleObserver {

    private var currentActivity: MainActivity? = null

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        if (this.currentActivity != null) {
            this.currentActivity?.finish()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() { }

    fun setCurrentActivity(mainActivity: MainActivity) {
        this.currentActivity = mainActivity;
    }
}
