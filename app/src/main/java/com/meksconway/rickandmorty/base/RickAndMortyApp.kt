package com.meksconway.rickandmorty.base

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RickAndMortyApp: Application() {

    override fun onCreate() {
        super.onCreate()
    }
}