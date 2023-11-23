package com.example.snapcensus

import android.app.Application
import com.google.firebase.FirebaseApp

import timber.log.Timber.DebugTree
import timber.log.Timber


class MyApp : Application() {


    override fun onCreate() {
        super.onCreate()
        //jika tipe debug maka akfitkan timber

            Timber.plant(DebugTree())
        //firebase
        FirebaseApp.initializeApp(this)



    }
}