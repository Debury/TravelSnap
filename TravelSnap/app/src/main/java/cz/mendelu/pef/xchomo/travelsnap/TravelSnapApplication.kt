package cz.mendelu.pef.xchomo.travelsnap

import android.app.Application
import android.content.Context

import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

const val useMockedValues: Boolean = true

@HiltAndroidApp
class TravelSnapApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        Timber.plant(object : Timber.DebugTree() {
            override fun createStackElementTag(element: StackTraceElement): String {
                return super.createStackElementTag(element) + ":" + element.lineNumber
            }
        })
    }

    companion object {
        lateinit var appContext: Context
            private set
    }

}