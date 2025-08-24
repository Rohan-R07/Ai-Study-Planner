package com.example.aistudyplanner

import android.app.Application
import com.google.firebase.BuildConfig
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory

class mApplication: Application() {

    override fun onCreate() {
        super.onCreate()


//        FirebaseApp.initializeApp(this)
//
//
//        val providerFactory = DebugAppCheckProviderFactory.getInstance()
//        FirebaseAppCheck.getInstance().installAppCheckProviderFactory(providerFactory)
//
//
//
//        if (BuildConfig.DEBUG) {
//            FirebaseAppCheck.getInstance().installAppCheckProviderFactory(
//                DebugAppCheckProviderFactory.getInstance()
//            )
//        }

        FirebaseAppCheck.getInstance()
            .installAppCheckProviderFactory(DebugAppCheckProviderFactory.getInstance())
//        if (BuildConfig.DEBUG) {
//            FirebaseAppCheck.getInstance()
//                .installAppCheckProviderFactory(
//                    DebugAppCheckProviderFactory.getInstance()
        FirebaseAppCheck.getInstance().getToken(false)
            .addOnSuccessListener { tokenResult ->
                val token = tokenResult.token
                // Add token to your Gemini API header
            }
        FirebaseApp.initializeApp(this)
//                )
//        }
        val appCheck = FirebaseAppCheck.getInstance()
        appCheck.installAppCheckProviderFactory(
            DebugAppCheckProviderFactory.getInstance()
        )

        if (BuildConfig.DEBUG) {
            val providerFactory = DebugAppCheckProviderFactory.getInstance()
            FirebaseAppCheck.getInstance().installAppCheckProviderFactory(providerFactory)
        }
    }





}