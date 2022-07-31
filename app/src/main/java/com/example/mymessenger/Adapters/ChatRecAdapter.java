package com.example.mymessenger.Adapters;

import static com.example.mymessenger.Models.ChatModel.Layout_One;
import static com.example.mymessenger.Models.ChatModel.Layout_Two;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymessenger.Models.ChatModel;
import com.example.mymessenger.R;

import java.util.ArrayList;

public class ChatRecAdapter extends RecyclerView.Adapter {
    ArrayList<ChatModel> chatList;
    Context context;

    public ChatRecAdapter(ArrayList<ChatModel> chatList, Context context) {
        this.chatList = chatList;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        switch (chatList.get(position).getViewType()) {
            case 1:
                return Layout_One;
            case 2:
                return Layout_Two;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case Layout_One: {
                View layout1 = LayoutInflater.from(context).inflate(R.layout.my_chat_vm, parent, false);
                return new MyChatViewHolder(layout1);
            }
            case Layout_Two: {
                View layout2 = LayoutInflater.from(context).inflate(R.layout.chat_vm_2, parent, false);
                return new SenderChatViewHolder(layout2);
            }
            default:
                View layout1 = LayoutInflater.from(context).inflate(R.layout.my_chat_vm, parent, false);
                return new MyChatViewHolder(layout1);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (chatList.get(position).getViewType()) {
            case Layout_One:
                String s = chatList.get(position).getText();
                ((MyChatViewHolder) holder).setView(s);
                break;
            case Layout_Two:
                String s1 = chatList.get(position).getText();
                String n = chatList.get(position).getSenderName();
                ((SenderChatViewHolder) holder).setView(n, s1);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    class MyChatViewHolder extends RecyclerView.ViewHolder {
        TextView You, MyMessage;
        public MyChatViewHolder(@NonNull View itemView) {
            super(itemView);
            You = itemView.findViewById(R.id.chat_rec_you);
            MyMessage = itemView.findViewById(R.id.chat_rec_mymsg);
        }

        private void setView(String text) {
            You.setText(R.string.You);
            MyMessage.setText(text);
        }
    }

    class SenderChatViewHolder extends RecyclerView.ViewHolder {
        TextView senderName, Message;

        public SenderChatViewHolder(@NonNull View itemView) {
            super(itemView);
           senderName = itemView.findViewById(R.id.chat_rec_Name);
            Message = itemView.findViewById(R.id.chat_rec_msg);
        }

        private void setView(String sender, String text) {
            senderName.setText(sender);
            Message.setText(text);
        }
    }
}
