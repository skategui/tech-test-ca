package com.android.ca


import android.app.Application
import com.android.account.di.accountsModule
import com.android.banks.di.banksModule
import com.android.banks.di.networkModule

import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class CAApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // init koin, deps inject tool
        startKoin {
            androidLogger()
            androidContext(this@CAApplication)
            modules(
                listOf(networkModule, banksModule, accountsModule)
            )
        }
    }
}