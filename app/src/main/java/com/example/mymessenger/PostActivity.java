package com.example.mymessenger;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.mymessenger.Models.PostModel;
import com.example.mymessenger.Models.UserModel;
import com.example.mymessenger.databinding.ActivityPostBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class PostActivity extends AppCompatActivity {
    ActivityPostBinding binding;
    static Uri uri;
    FirebaseDatabase database;
    FirebaseUser fUser;
    StorageReference storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityPostBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        database = FirebaseDatabase.getInstance();
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        binding.postActImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        binding.btnPost.setOnClickListener(new View.OnClickListener() {
            UserModel user;
            @Override
            public void onClick(View v) {
                if (binding.postActText.getText().toString().isEmpty()) {
                    binding.postActText.setError("Please write something");
                    return;
                } else {
                    binding.postPbar.setVisibility(View.VISIBLE);
                    database.getReference("users").child(fUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {
                                user = task.getResult().getValue(UserModel.class);
                                String text = binding.postActText.getText().toString();
                                storage = FirebaseStorage.getInstance().getReference("posts/" + user.getUid() + "/" + text + "." + getFileExtension(uri));
                                storage.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        binding.postPbar.setVisibility(View.GONE);
                                        binding.postPrg.setText("Uploaded");
                                        storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                PostModel post = new PostModel(user.getUsername(), text, user.getUid(), uri.toString(), user.getJob(), user.getPlace(), user.getUrl());
                                                database.getReference("posts").child(text).setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(PostActivity.this, "Posted Successfully", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        });

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        binding.postPbar.setVisibility(View.GONE);
                                        Toast.makeText(PostActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                        double progress = (snapshot.getBytesTransferred() * 100) / snapshot.getTotalByteCount();
                                        binding.postPrg.setVisibility(View.VISIBLE);
                                        binding.postPrg.setText(progress + "%");
                                        binding.postPbar.setMax((int) snapshot.getTotalByteCount());
                                        binding.postPbar.setProgress((int) progress);
                                    }
                                });
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PostActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data.getData() != null) {
            uri = data.getData();
            Glide.with(PostActivity.this).load(uri).into(binding.postActImg);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap map = MimeTypeMap.getSingleton();
        return map.getExtensionFromMimeType(cr.getType(uri));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(PostActivity.this, MainActivity2.class));
    }
}