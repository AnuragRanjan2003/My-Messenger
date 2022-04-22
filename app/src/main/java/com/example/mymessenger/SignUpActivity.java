package com.example.mymessenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import Models.User;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        EditText Email,userName,Password;
        Button SignUp;
        ProgressDialog progressDialog=new ProgressDialog(SignUpActivity.this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Signing you up");
        Email=findViewById(R.id.et_email);
        userName=findViewById(R.id.et_userName);
        Password=findViewById(R.id.et_Password);
        SignUp=findViewById(R.id.btn_sign_up2);
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                if(Email.getText().toString().isEmpty()){
                    Email.setError("Email is required");
                    progressDialog.dismiss();
                    return;
                } else if(userName.getText().toString().isEmpty()){
                    userName.setError("username is required");
                    progressDialog.dismiss();
                    return;
                }
                else if(Password.getText().toString().isEmpty()){
                    Password.setError("Please set a password");
                    progressDialog.dismiss();
                    return;
                } else {
                    mAuth.createUserWithEmailAndPassword(Email.getText().toString().trim(),Password.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.dismiss();
                                    if(task.isSuccessful()){
                                        User user=new User(userName.getText().toString().trim());
                                        database.getReference("users").child(userName.getText().toString()).setValue(user);
                                    } else {
                                        Toast.makeText(SignUpActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}