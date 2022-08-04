package com.example.mymessenger.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mymessenger.Models.CommentModel;
import com.example.mymessenger.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    ArrayList<CommentModel> list;
    Context context;

    public CommentAdapter(ArrayList<CommentModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.comment_vm, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CommentModel cM = list.get(position);
        holder.Name.setText(cM.getName());
        holder.Comment.setText(cM.getComment());
        Glide.with(holder.itemView.getContext()).load(cM.getPurl()).into(holder.Img);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView Name, Comment;
        ShapeableImageView Img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.cmt_name);
            Comment = itemView.findViewById(R.id.cmt_cmt);
            Img = itemView.findViewById(R.id.cmt_img);
        }
    }
}
