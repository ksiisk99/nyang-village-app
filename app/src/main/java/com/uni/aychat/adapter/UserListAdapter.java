package com.uni.aychat.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uni.aychat.R;

import java.util.ArrayList;
import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter {
    List<String> userList=null;
    private LayoutInflater inflater;
    private String myNickName;

    public UserListAdapter(LayoutInflater inflater){this.inflater=inflater;}

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view=inflater.inflate(R.layout.user_list_item,parent,false);
        return new UserListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        UserListHolder uh=(UserListHolder) holder;
        if(!userList.get(position).equals(myNickName)){
            ((UserListHolder) holder).textView.setText(userList.get(position));
        }else{
            ((UserListHolder) holder).textView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class UserListHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public UserListHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.textView);
        }
    }

    public void setItem(String name){
        userList.add(name);
    }

    public void setItems(List<String> userList, String myNickName){
        this.userList=userList;
        this.myNickName=myNickName;
        notifyDataSetChanged();
    }
}
