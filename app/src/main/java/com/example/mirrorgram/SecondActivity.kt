package com.example.mirrorgram

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.mirrorgram.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson


class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

    }
    override fun onPause() {
        super.onPause()
        Log.d("mvvm", "onStart: 2")
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val reference = firebaseDatabase.getReference("users")
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        val user = User(
            currentUser?.email,
            currentUser?.displayName,
            currentUser?.phoneNumber,
            currentUser?.photoUrl.toString(),
            currentUser?.uid,
            false,
        )
        if (currentUser != null){
            reference.child(currentUser.uid).setValue(user)
        }
    }

}