package com.example.mymessenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import Models.User;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        EditText Email, Password;
        TextView forgotPass;
        Button SignInBtn;
        Email = findViewById(R.id.et_signInEmail);
        Password = findViewById(R.id.et_SignInPassword);
        SignInBtn = findViewById(R.id.btn_SignIn);
        forgotPass=findViewById(R.id.tv_Forgot_password);
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Signing You In");
        progressDialog.setTitle("Please Wait");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        SignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                if (Email.getText().toString().isEmpty()) {
                    Email.setError("Email is required");
                    progressDialog.dismiss();
                    return;
                } else if (Password.getText().toString().isEmpty()) {
                    Password.setError("Password is required");
                    progressDialog.dismiss();
                    return;
                } else {
                    mAuth.signInWithEmailAndPassword(Email.getText().toString(), Password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.dismiss();
                                    if(!task.isSuccessful())
                                        Toast.makeText(MainActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                                    else{
                                        startActivity(new Intent(MainActivity.this,MainActivity2.class));
                                        finishAffinity();}
                                }
                            });
                }
            }
        });
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ForgotPasswordActivity.class));
            }
        });

    }
}