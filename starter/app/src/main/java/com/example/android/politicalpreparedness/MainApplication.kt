package com.example.android.politicalpreparedness

import android.app.Application
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.election.ElectionsViewModel
import com.example.android.politicalpreparedness.election.VoterInfoViewModel
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.repository.ElectionRepositoryImpl
import com.example.android.politicalpreparedness.repository.RepresentativeRepositoryImpl
import com.example.android.politicalpreparedness.representative.RepresentativeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        val myModule = module {
            viewModel {
                ElectionsViewModel(get() as ElectionRepositoryImpl)
            }
            viewModel {
                VoterInfoViewModel(get() as ElectionRepositoryImpl)
            }
            viewModel {
                RepresentativeViewModel(get() as RepresentativeRepositoryImpl)
            }
            single { ElectionRepositoryImpl(get(), get()) }
            single { RepresentativeRepositoryImpl(get()) }
            single { ElectionDatabase.createElectionDao(this@MainApplication) }
            single { CivicsApi.retrofitService }
        }

        startKoin {
            androidContext(this@MainApplication)
            modules(listOf(myModule))
        }
    }
}