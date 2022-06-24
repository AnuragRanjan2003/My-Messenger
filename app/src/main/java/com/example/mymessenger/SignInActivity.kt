package com.example.mymessenger

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mymessenger.databinding.ActivitySignInBinding
import com.example.mymessenger.template.Complete
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var progressdig: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        progressdig = ProgressDialog(this@SignInActivity)
        progressdig.setMessage("Signing you in")
        progressdig.setTitle("Please Wait")
        val intent = Intent(this@SignInActivity, MainActivity2::class.java)
        binding.btnSignIn.setOnClickListener {
            progressdig.show()
            if (binding.etSignInEmail.text.toString().isEmpty()) {
                binding.etSignInEmail.setError("Email is required")
                progressdig.dismiss()
                return@setOnClickListener
            } else {
                if (binding.etSignInPassword.text.toString().isEmpty()) {
                    binding.etSignInPassword.setError("Password is required")
                    progressdig.dismiss()
                    return@setOnClickListener

                } else {
                    auth.signInWithEmailAndPassword(
                        binding.etSignInEmail.text.toString(),
                        binding.etSignInPassword.text.toString()
                    ).addOnCompleteListener {
                        progressdig.dismiss()
                        val comp = Complete(this@SignInActivity, it, progressdig)
                        comp.setIntent(intent)
                        comp.setMsg("Signed in successfully", it.exception?.message.toString())
                        comp.runComp()
                        finishAffinity()
                    }
                }
            }
        }


    }

}