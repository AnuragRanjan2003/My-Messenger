package com.example.mymessenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthWebException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseUser fUser=mAuth.getCurrentUser();
        assert fUser != null;
        Objects.requireNonNull(getSupportActionBar()).setTitle(fUser.getDisplayName());


    }
}