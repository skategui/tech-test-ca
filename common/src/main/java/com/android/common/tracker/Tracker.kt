package com.android.common.tracker

import android.util.Log
import com.android.common.BuildConfig

class TrackerImpl : Tracker {

    override fun debug(origin: String, payload: Any) {
        if (BuildConfig.DEBUG) {
            Log.d(origin, payload.toString())
        }
        // log into mixpanel, amplitude, firebase etc.. in order to track the user interactions within the app
    }

    override fun error(origin: String, payload: Any?) {
        if (BuildConfig.DEBUG) {
            Log.e(origin, payload.toString())
        }
        // log into mixpanel, amplitude, firebase etc.. to crash errors that does not lead to a crash
    }
}

interface Tracker {
    fun debug(origin: String, payload: Any)
    fun error(origin: String, payload: Any?)
}