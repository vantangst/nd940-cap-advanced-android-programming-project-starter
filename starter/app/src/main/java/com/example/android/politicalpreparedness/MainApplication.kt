package com.example.android.politicalpreparedness

import android.app.Application
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.election.ElectionsViewModel
import com.example.android.politicalpreparedness.election.VoterInfoViewModel
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.repository.ElectionRepository
import com.example.android.politicalpreparedness.repository.ElectionRepositoryImpl
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
            single { ElectionRepositoryImpl(get(), get()) }
            single { ElectionDatabase.createElectionDao(this@MainApplication) }
            single { CivicsApi.retrofitService }
        }

        startKoin {
            androidContext(this@MainApplication)
            modules(listOf(myModule))
        }
    }
}