package com.android.banks.di


import com.android.banks.datastore.BanksDatastoreImpl
import com.android.banks.network.BanksService
import com.android.banks.repository.BanksRepository
import com.android.banks.repository.BanksRepositoryImpl
import com.android.banks.vm.BanksViewModel
import com.android.common.datastore.BanksDatastore
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

// deps to inject related to the banks
val banksModule = module {

    factory<BanksRepository> {
        BanksRepositoryImpl(
            service = get<Retrofit>()
                .create(BanksService::class.java)
        )
    }

    single<BanksDatastore> { BanksDatastoreImpl() }
    factory<com.android.banks.usecase.GetBanksUsecase> {
        com.android.banks.usecase.GetBanksUsecaseImpl(
            datastore = get(),
            repository = get()
        )
    }
    viewModel { BanksViewModel(banksUsecase = get()) }
}