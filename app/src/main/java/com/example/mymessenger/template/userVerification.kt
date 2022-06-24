package com.example.mymessenger.template

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class userVerification(
    var firebaseUser: FirebaseUser,
    var database: FirebaseDatabase,
    var intent: Intent
) {
    private lateinit var context: Context

    fun setContext(context: Context) {
        this.context = context
    }

    fun verifyUser() {
        if (firebaseUser.uid.isNullOrBlank()) {
            startActivity(context, intent, Bundle())
        }
    }
}