package com.app.sauruspay.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.app.sauruspay.R
import com.app.sauruspay.utils.Navigation
import com.app.sauruspay.utils.Preferences

class SplashActivity : AppCompatActivity() {
    private var auth = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        verifyAuth()
    }

    fun verifyAuth(){
        Handler(Looper.getMainLooper()).postDelayed({
            auth = Preferences.verifyCrendentials(this@SplashActivity)
            if(auth){ Navigation.startActivity(OrderActivity::class.java, this) }
            else{ Navigation.startActivity(LoginActivity::class.java, this) }
        },2000)
    }
}