package com.example.mymessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mymessenger.databinding.ActivityMain2Binding
import com.example.mymessenger.template.userVerification
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class MainActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityMain2Binding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var firebaseUser: FirebaseUser
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMain2Binding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()
        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        firebaseUser = mAuth?.currentUser!!
        val verify = userVerification(firebaseUser, database, Intent(this, AskActivity::class.java))
        verify.setContext(this)
        verify.verifyUser()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@MainActivity2, AskActivity::class.java))
        finishAffinity()
    }
}