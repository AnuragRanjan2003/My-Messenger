package com.example.mymessenger.template

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

class Complete(
    private var context: Context,
    private var task: Task<AuthResult>,
    private var pD: ProgressDialog
) {
    private lateinit var sMsg: String
    private lateinit var fMsg: String
    private lateinit var intent: Intent



    fun setMsg(sMsg: String, fMsg: String) {
        this.sMsg = sMsg
        this.fMsg = fMsg
    }

    fun setIntent(intent: Intent) {
        this.intent = intent
    }

    fun runComp() {
        pD.dismiss()
        if (task.isSuccessful) {
            startActivity(context, intent, Bundle())
            Toast.makeText(context, sMsg, Toast.LENGTH_LONG).show()

        } else
            Toast.makeText(context, fMsg, Toast.LENGTH_LONG).show()
    }

}