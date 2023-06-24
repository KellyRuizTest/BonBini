package com.twenty.bonbini

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import com.airbnb.lottie.LottieAnimationView
import com.twenty.bonbini.Model.LoginNewActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    //var lottieAnimationView  : LottieAnimationView = findViewById(R.id.lottie)
    //var patillita : ImageView = findViewById(R.id.patilla)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val DURATION = lottie.duration + 1000

        Handler().postDelayed({
            startActivity(Intent(this, LoginNewActivity::class.java))
            finish()
        }, DURATION)

    }
}