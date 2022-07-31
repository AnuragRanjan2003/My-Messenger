package com.example.mymessenger

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.mymessenger.Models.UserModel
import com.example.mymessenger.databinding.ActivitySignUpBinding
import com.example.mymessenger.template.Complete
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var progressDialog: ProgressDialog
    private lateinit var storage: FirebaseStorage
    private lateinit var uri: Uri
    private lateinit var defUri: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        progressDialog = ProgressDialog(this@SignUpActivity)
        progressDialog.setTitle("please wait")
        progressDialog.setMessage("Signing you up")
        defUri = Uri.parse("android.resource://res/drawable/man.png")
        intent = Intent(this@SignUpActivity, MainActivity2::class.java)
        binding.imgProfile.setOnClickListener(android.view.View.OnClickListener {
            intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 100)
        })

        binding.btnSignUp.setOnClickListener {
            progressDialog.show()
            val a = binding.etSignUpInEmail.text.toString()
            val b = binding.etSignUpPassword.text.toString()
            val c = binding.etUserName.text.toString()
            val place = binding.etSignUpPlace.text.toString()
            val job = binding.etSignUpJob.text.toString()
            if (a.isEmpty()) {
                binding.etSignUpInEmail.error = "Email is Required"
                progressDialog.dismiss()
                return@setOnClickListener
            } else {
                if (c.isEmpty()) {
                    binding.etUserName.error = "Username required"
                    progressDialog.dismiss()
                    return@setOnClickListener
                } else {
                    if (b.isEmpty() || b.length < 6) {
                        binding.etSignUpPassword.error = "Password too short"
                        progressDialog.dismiss()
                        return@setOnClickListener
                    } else {
                        mAuth.createUserWithEmailAndPassword(a, b).addOnCompleteListener { it ->
                            if (it.isSuccessful) {
                                val task: Task<AuthResult> = it
                                val context: Context
                                context = this@SignUpActivity
                                val complete = Complete(context, task, progressDialog)
                                complete.setMsg(
                                    "Signed up successfully",
                                    task.exception?.message.toString()
                                )
                                val psRef = storage.getReference(
                                    "profileImages/" + it.result.user?.uid.toString() + "." + getFileExtension(
                                        uri
                                    )
                                )
                                psRef
                                    .putFile(uri)
                                    .addOnCompleteListener(OnCompleteListener {
                                        if (it.isSuccessful) {
                                            psRef.downloadUrl.addOnSuccessListener { it1 ->
                                                val user =
                                                    UserModel(
                                                        c,
                                                        task.result.user?.uid.toString(),
                                                        place,
                                                        job,
                                                        it1.toString()
                                                    )
                                                database.getReference("users")
                                                    .child(task.result.user?.uid.toString())
                                                    .setValue(user)
                                            }
                                            val intent1 = Intent(this, MainActivity2::class.java)
                                            complete.setIntent(intent1)
                                            complete.runComp()
                                            finishAffinity()
                                        } else{
                                            Toast.makeText(
                                                this,
                                                it.exception?.message,
                                                Toast.LENGTH_LONG
                                            ).show()
                                        progressDialog.dismiss()}
                                    })
                            } else {
                                progressDialog.dismiss()
                                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG)
                                    .show()
                            }
                        }

                    }
                }
            }
        }
    }

    private fun getFileExtension(uri: Uri): String? {
        val cr = contentResolver
        val mt = MimeTypeMap.getSingleton()
        return mt.getExtensionFromMimeType(cr.getType(uri))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK ) {
            uri = data?.data!!
            Glide.with(this).load(uri).into(binding.imgProfile)
        }
    }
}