package com.example.mymessenger.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mymessenger.Models.PostModel;
import com.example.mymessenger.PostDisplay;
import com.example.mymessenger.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class PostRecAdapter extends RecyclerView.Adapter<PostRecAdapter.ViewHolder> {
    ArrayList<PostModel> list;
    Context context;

    public PostRecAdapter(ArrayList<PostModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_vm_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostModel pM = list.get(position);
        holder.Name.setText(pM.getSenderName());
        holder.Place.setText(pM.getPlace());
        holder.Job.setText(pM.getJob());
        holder.Text.setText(pM.getText());
        Glide.with(holder.itemView.getContext()).load(pM.getUrl()).into(holder.PostImg);
        Glide.with(holder.itemView.getContext()).load(pM.getProfileUrl()).into(holder.pImg);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, PostDisplay.class);
                ArrayList<String> list=new ArrayList<>();
                list.add(pM.getSenderName());//0
                list.add(pM.getPlace());//1
                list.add(pM.getJob());//2
                list.add(pM.getText());//3
                list.add(pM.getProfileUrl());//4
                list.add(pM.getUrl());//5
                list.add(pM.getSenderUid());//6
                intent.putExtra("post",list);//7
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Name, Place, Job, Text;
        ShapeableImageView pImg;
        ImageView PostImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.post_p_name);
            Place = itemView.findViewById(R.id.post_p_place);
            Job = itemView.findViewById(R.id.post_p_job);
            PostImg = itemView.findViewById(R.id.post_img);
            pImg = itemView.findViewById(R.id.post_p_img);
            Text=itemView.findViewById(R.id.post_text);

        }
    }
}
