package com.android.common.di


import com.android.common.tracker.Tracker
import com.android.common.tracker.TrackerImpl
import org.koin.dsl.module

// deps to inject related to the tracker
val trackerModule = module {

    factory<Tracker> { TrackerImpl() }

}