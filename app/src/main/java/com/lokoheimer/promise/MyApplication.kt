package com.lokoheimer.promise
import android.app.Application
import android.content.Context
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import android.view.Window

// Not object class. AndroidManifest.xml error happen.
object MyApplication : Application() {
    private var instance: MyApplication? = null
    init {
        Log.d("Kenneth", "constructor")
        instance = this
    }
    fun applicationContext() : Context {
        Log.d("Kenneth", "application context")
        return instance!!.applicationContext
    }

    override fun onCreate() {
        super.onCreate()
        // initialize for any

        // Use ApplicationContext.
        // example: SharedPreferences etc...
        instance = this
        Log.d("Kenneth", "on create")
        val context: Context = MyApplication.applicationContext()

    }

}