package com.example.mymessenger.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymessenger.ChatActivity;
import com.example.mymessenger.Models.UserModel;
import com.example.mymessenger.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeRecAdapter extends RecyclerView.Adapter<HomeRecAdapter.viewHolder> {
    ArrayList<UserModel> arrayList;
    Context context;
    FirebaseDatabase database;


    public HomeRecAdapter(ArrayList<UserModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeRecAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_rec_vm, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeRecAdapter.viewHolder holder, int position) {
        UserModel user = arrayList.get(position);
        holder.name.setText(user.getUsername());
        holder.place.setText(user.getPlace());
        holder.job.setText(user.getJob());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView name,place,job;
        ImageView Image;


        public viewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.home_rec_name);
            Image = itemView.findViewById(R.id.home_rec_img);
            place=itemView.findViewById(R.id.home_rec_place);
            job=itemView.findViewById(R.id.home_rec_job);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, ChatActivity.class);

                    intent.putExtra("recName",name.getText().toString());
                    context.startActivity(intent);
                }
            });
        }
    }



}
