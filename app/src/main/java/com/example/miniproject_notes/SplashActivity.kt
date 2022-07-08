@file:Suppress("DEPRECATION")

package com.example.miniproject_notes

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val appImage: ImageView = findViewById(R.id.splash_image)
        val subtitle: TextView = findViewById(R.id.txt_subtitle)
        val appImageAnimation = AnimationUtils.loadAnimation(this, R.anim.img_anim)
        val subtitleAnimation = AnimationUtils.loadAnimation(this,R.anim.sub_anim)
        appImage.startAnimation(appImageAnimation)
        subtitle.startAnimation(subtitleAnimation)

        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 2500)

    }
}