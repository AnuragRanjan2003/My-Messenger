package com.example.mymessenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mymessenger.Adapters.ChatRecAdapter;
import com.example.mymessenger.Models.ChatModel;
import com.example.mymessenger.Models.UserModel;
import com.example.mymessenger.databinding.ActivityChatBinding;
import com.example.mymessenger.template.UidCat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity {
    ActivityChatBinding binding;
    ArrayList<ChatModel> cArray = new ArrayList<>();
    ChatRecAdapter cAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser fUser = mAuth.getCurrentUser();
        assert fUser != null;
        Objects.requireNonNull(getSupportActionBar()).setTitle(getRecName());
        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            UserModel uModel;

            @Override
            public void onClick(View v) {
                database.getReference("users").child(fUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        uModel = dataSnapshot.getValue(UserModel.class);
                        assert uModel != null;

                        database.getReference("users").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                                    UserModel user = snap.getValue(UserModel.class);
                                    if (user.getUsername().equals(getRecName())) {
                                        Date date = Calendar.getInstance().getTime();
                                        String d = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                                        String t = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().getTime());
                                        ChatModel cModel = new ChatModel(uModel.getUsername(), getRecName(), binding.etChat.getText().toString(), d, t);
                                        cModel.setViewType(1);
                                        UidCat uCat = new UidCat(uModel.getUid(), user.getUid());
                                        database.getReference("chats").child(uCat.createUid()).child(date.toString()).setValue(cModel);
                                        break;
                                    }
                                }
                            }
                        });

                    }
                });

            }
        });
        binding.chatRec.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.chatRec.setHasFixedSize(true);

        database.getReference("users").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    UserModel user = snap.getValue(UserModel.class);
                    if (user.getUsername().equals(getRecName())) {
                        UidCat uCat = new UidCat(fUser.getUid(), user.getUid());
                        database.getReference("chats").child(uCat.createUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot snap : snapshot.getChildren()) {
                                    ChatModel cModel = snap.getValue(ChatModel.class);
                                    cArray.add(cModel);
                                }
                                cAdapter.notifyDataSetChanged();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        break;
                    }
                }
            }
        });
        cAdapter = new ChatRecAdapter(cArray, ChatActivity.this);
        binding.chatRec.setAdapter(cAdapter);
    }


    public String getRecName() {
        Intent intent = getIntent();
        return intent.getStringExtra("recName");
    }


}