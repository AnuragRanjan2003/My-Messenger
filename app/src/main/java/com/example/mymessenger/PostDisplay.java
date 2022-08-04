package com.example.mymessenger;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.mymessenger.Adapters.CommentAdapter;
import com.example.mymessenger.Models.CommentModel;
import com.example.mymessenger.Models.UserModel;
import com.example.mymessenger.Models.Vote;
import com.example.mymessenger.databinding.ActivityPostDisplayBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PostDisplay extends AppCompatActivity {
    ActivityPostDisplayBinding binding;
    ArrayList<String> pList;
    FirebaseDatabase database;
    FirebaseUser fUser;
    ArrayList<CommentModel> list;
    CommentAdapter adapter;
    int upCount;
    int DownCount;
    int myUp;
    int myDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityPostDisplayBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        database = FirebaseDatabase.getInstance();
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        Intent intent = getIntent();
        pList = (ArrayList<String>) intent.getStringArrayListExtra("post");
        binding.postPName.setText(pList.get(0));
        binding.postPPlace.setText(pList.get(1));
        binding.postPJob.setText(pList.get(2));
        binding.postText.setText(pList.get(3));
        Glide.with(this).load(pList.get(4)).into(binding.postPImg);
        Glide.with(this).load(pList.get(5)).into(binding.postImg);

        list = new ArrayList<>();
        binding.postDRec.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.postDRec.setHasFixedSize(true);

        if (fUser.getUid().equals(pList.get(6))) {
            binding.pdCmnt.setVisibility(View.GONE);
            binding.imgSend.setVisibility(View.GONE);
        }

        database.getReference("Comments").child(pList.get(3)).child("comments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    list.add(snap.getValue(CommentModel.class));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter = new CommentAdapter(list, PostDisplay.this);
        binding.postDRec.setAdapter(adapter);


        binding.imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = binding.pdCmnt.getText().toString();
                if (txt.isEmpty()) {
                    return;
                } else {
                    database.getReference("users").child(fUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            UserModel u = dataSnapshot.getValue(UserModel.class);
                            CommentModel cM = new CommentModel(u.getUsername(), u.getUrl(), u.getUid());
                            cM.setComment(txt);
                            SimpleDateFormat format = new SimpleDateFormat("dd:MM:yyyy_HH:mm:ss", Locale.getDefault());
                            String time = format.format(new Date());
                            database.getReference("Comments").child(pList.get(3)).child("comments").child(time).setValue(cM);
                            binding.pdCmnt.setText("");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PostDisplay.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        binding.vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myUp==0){
                Vote vote = new Vote(fUser.getUid(), 1, 0);
                database.getReference("Comments").child(pList.get(3)).child("votes").child(fUser.getUid()).setValue(vote);}
                else if(myUp==1){
                    Vote vote = new Vote(fUser.getUid(), 0, 0);
                    database.getReference("Comments").child(pList.get(3)).child("votes").child(fUser.getUid()).setValue(vote);
                }
            }
        });
        binding.down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myDown==0){
                Vote vote = new Vote(fUser.getUid(), 0, 1);
                database.getReference("Comments").child(pList.get(3)).child("votes").child(fUser.getUid()).setValue(vote);}
                else if(myDown==1){
                    Vote vote = new Vote(fUser.getUid(), 0, 0);
                    database.getReference("Comments").child(pList.get(3)).child("votes").child(fUser.getUid()).setValue(vote);
                }
            }
        });
        database.getReference("Comments").child(pList.get(3)).child("votes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                upCount = 0;
                DownCount = 0;
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Vote v = snap.getValue(Vote.class);
                    assert v != null;
                    upCount += v.getUpvote();
                    DownCount += v.getDownVote();
                    if (v.getUid().equals(fUser.getUid())) {
                        if (v.getUpvote() == 1) {
                            myUp = 1;
                            myDown = 0;
                        } else if (v.getDownVote() == 1) {
                            myDown = 1;
                            myUp = 0;
                        } else {
                            myUp = 0;
                            myDown = 0;
                        }
                    }
                }
                binding.upCount.setText(String.valueOf(upCount));
                binding.downCount.setText(String.valueOf(DownCount));
                if (myUp == 1) {
                    Glide.with(PostDisplay.this).load(R.drawable.uparrow).into(binding.vote);
                    Glide.with(PostDisplay.this).load(R.drawable.white_down).into(binding.down);
                } else if (myDown == 1) {
                    Glide.with(PostDisplay.this).load(R.drawable.down).into(binding.down);
                    Glide.with(PostDisplay.this).load(R.drawable.white_up).into(binding.vote);
                } else {
                    Glide.with(PostDisplay.this).load(R.drawable.white_up).into(binding.vote);
                    Glide.with(PostDisplay.this).load(R.drawable.white_down).into(binding.down);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}