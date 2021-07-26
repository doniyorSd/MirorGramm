package com.example.mirrorgram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.mirrorgram.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val handler = Handler(mainLooper)
        handler.postDelayed({
            val intent = Intent(this@MainActivity, SecondActivity::class.java)
            startActivity(intent)
            finish()
        },5000)
    }

}