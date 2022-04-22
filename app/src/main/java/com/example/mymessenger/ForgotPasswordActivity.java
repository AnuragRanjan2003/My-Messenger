package com.example.mymessenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText Email;
    Button ResetPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Email = findViewById(R.id.et_forgotEmail);
        ResetPass = findViewById(R.id.btn_resetPass);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending Email");
        ResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                if (Email.getText().toString().isEmpty()) {
                    Email.setError("Email is required");
                    progressDialog.dismiss();
                    return;
                } else {
                    mAuth.sendPasswordResetEmail(Email.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressDialog.dismiss();
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ForgotPasswordActivity.this, "Email is Sent", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(ForgotPasswordActivity.this, MainActivity.class));
                                        finishAffinity();
                                    } else {
                                        Toast.makeText(ForgotPasswordActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

    }
}