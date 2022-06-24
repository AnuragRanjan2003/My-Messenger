package com.example.mymessenger

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mymessenger.Models.UserModel
import com.example.mymessenger.databinding.ActivitySignUpBinding
import com.example.mymessenger.template.Complete
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this@SignUpActivity)
        progressDialog.setTitle("please wait")
        progressDialog.setMessage("Signing you up")
        intent = Intent(this@SignUpActivity, MainActivity2::class.java)
        binding.btnSignUp.setOnClickListener {
            progressDialog.show()
            val a = binding.etSignUpInEmail.text.toString()
            val b = binding.etSignUpPassword.text.toString()
            val c = binding.etUserName.text.toString()
            if (a.isEmpty()) {
                binding.etSignUpInEmail.setError("Email is Required")
                progressDialog.dismiss()
                return@setOnClickListener
            } else {
                if (c.isEmpty()) {
                    binding.etUserName.setError("Username required")
                    progressDialog.dismiss()
                    return@setOnClickListener
                } else {
                    if (b.isEmpty()) {
                        binding.etSignUpPassword.setError("Password is required")
                        progressDialog.dismiss()
                        return@setOnClickListener
                    } else {
                        mAuth.createUserWithEmailAndPassword(a, b).addOnCompleteListener {
                            val task: Task<AuthResult>
                            task = it
                            val context: Context
                            context = this@SignUpActivity
                            val complete = Complete(context, task, progressDialog)
                            complete.setMsg(
                                "Signed up successfully",
                                task.exception?.message.toString()
                            )
                            complete.setIntent(intent)
                            complete.runComp()
                            finishAffinity()
                            val user = UserModel(c, task.result.user?.uid.toString())
                            database.getReference("users").child(task.result.user?.uid.toString())
                                .setValue(user)
                        }

                    }
                }
            }
        }
    }
}