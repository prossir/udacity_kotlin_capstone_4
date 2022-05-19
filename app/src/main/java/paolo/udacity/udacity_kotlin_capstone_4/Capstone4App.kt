package paolo.udacity.udacity_kotlin_capstone_4

import androidx.multidex.BuildConfig
import androidx.multidex.MultiDexApplication
import com.google.firebase.FirebaseApp
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber


@HiltAndroidApp
class Capstone4App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        initLogging()
        initFirebase()
        initThreeTen()
    }

    private fun initLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initFirebase() {
        FirebaseApp.initializeApp(this)
    }

    private fun initThreeTen() {
        AndroidThreeTen.init(this)
    }

}