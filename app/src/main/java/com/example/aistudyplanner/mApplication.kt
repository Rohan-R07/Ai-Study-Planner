package com.example.aistudyplanner

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory

class mApplication: Application() {

    override fun onCreate() {
        super.onCreate()

//        FirebaseApp.initializeApp(this)
//
//        // Enable Debug App Check Provider for testing
//        val providerFactory = DebugAppCheckProviderFactory.getInstance()
//        FirebaseAppCheck.getInstance().installAppCheckProviderFactory(providerFactory)


        FirebaseApp.initializeApp(this)

        // Enable Debug provider for development
        val providerFactory = DebugAppCheckProviderFactory.getInstance()
        FirebaseAppCheck.getInstance().installAppCheckProviderFactory(providerFactory)
    }



}