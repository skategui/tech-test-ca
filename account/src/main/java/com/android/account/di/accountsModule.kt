package com.android.account.di

import com.android.account.vm.AccountDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

// deps to inject related to the account detail
val accountsModule = module {

    factory<com.android.account.usecase.GetAccountUsecase> {
        com.android.account.usecase.GetAccountUsecaseImpl(
            datastore = get()
        )
    }
    viewModel { AccountDetailViewModel(get(), getAccountUsecase = get(), tracker = get()) }
}