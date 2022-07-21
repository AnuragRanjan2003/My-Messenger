package com.example.mymessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.mymessenger.R.id.homeFragment
import com.example.mymessenger.databinding.ActivityMain2Binding
import com.example.mymessenger.template.userVerification
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.ismaeldivita.chipnavigation.ChipNavigationBar

class MainActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityMain2Binding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var firebaseUser: FirebaseUser
    private val homeFragment = HomeFragment()
    private val mainFragment = MainFragment()
    private val settingFragment = SettingFragment()
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
        replace(homeFragment)


        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> replace(homeFragment)
                R.id.mainFragment -> replace(mainFragment)
                R.id.settingFragment -> replace(settingFragment)
            }
            true
        }


    }

    fun replace(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@MainActivity2, AskActivity::class.java))
        finishAffinity()
    }
}