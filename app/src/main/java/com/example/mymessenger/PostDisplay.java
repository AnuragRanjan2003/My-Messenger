package com.example.mymessenger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.mymessenger.databinding.ActivityPostDisplayBinding;

import java.util.ArrayList;

public class PostDisplay extends AppCompatActivity {
    ActivityPostDisplayBinding binding;
    ArrayList<String> pList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding= ActivityPostDisplayBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        Intent intent=getIntent();
        pList =(ArrayList<String>)intent.getStringArrayListExtra("post");
        binding.postPName.setText(pList.get(0));
        binding.postPPlace.setText(pList.get(1));
        binding.postPJob.setText(pList.get(2));
        binding.postText.setText(pList.get(3));
        Glide.with(this).load(pList.get(4)).into(binding.postPImg);
        Glide.with(this).load(pList.get(5)).into(binding.postImg);


    }
}