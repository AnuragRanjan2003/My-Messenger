package com.example.mymessenger;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mymessenger.Adapters.ChatRecAdapter;
import com.example.mymessenger.Models.ChatModel;
import com.example.mymessenger.Models.UserModel;
import com.example.mymessenger.databinding.ActivityChatBinding;
import com.example.mymessenger.template.Animator;
import com.example.mymessenger.template.UidCat;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity {
    ActivityChatBinding binding;
    ArrayList<ChatModel> cArray = new ArrayList<>();
    ChatRecAdapter cAdapter;
    Animator animator;
    Animation fadeIn, fadeOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser fUser = mAuth.getCurrentUser();
        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        animator = new Animator(binding.chatPBar);
        animator.setPrimAnim(fadeIn, fadeOut);
        animator.loadPrimAnimation(View.VISIBLE);
        assert fUser != null;
        Objects.requireNonNull(getSupportActionBar()).setTitle(getRecName());
        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            UserModel uModel;

            @Override
            public void onClick(View v) {
                if (!binding.etChat.getText().toString().isEmpty()) {
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
                                            UidCat uCat = new UidCat(uModel.getUid(), user.getUid());
                                            database.getReference("chats").child(uCat.createUid()).child(date.toString()).setValue(cModel);
                                            binding.etChat.setText("");
                                            break;
                                        }
                                    }
                                }
                            });

                        }
                    });
                }


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
                                cArray.clear();
                                for (DataSnapshot snap : snapshot.getChildren()) {
                                    ChatModel cModel = snap.getValue(ChatModel.class);
                                    if (getRecName().equals(cModel.getReceiverName()))
                                        cModel.setViewType(1);
                                    else
                                        cModel.setViewType(2);
                                    cArray.add(cModel);
                                }
                                animator.loadPrimAnimation(View.INVISIBLE);
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