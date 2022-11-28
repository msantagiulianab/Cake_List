package com.example.cakelist

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

const val TAG = "CakeListLogTag"
const val cake_id = "cake_id"

@HiltAndroidApp
class CakeListApplication : Application() {
}