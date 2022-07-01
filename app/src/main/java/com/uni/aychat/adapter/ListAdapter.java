package com.uni.aychat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uni.aychat.etc.OnSingleClickListener;
import com.uni.aychat.R;
import com.uni.aychat.dto.RoomInfo;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>{
    ArrayList<RoomInfo> roomInfos=null;

    public interface OnItemClickListener{
        void OnItemClick(View view, int position);
    }
    private OnItemClickListener listener=null;
    public void setOnItemClickListener(OnItemClickListener listener){this.listener=listener;}

    @NonNull
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ViewHolder holder, int position) {
        holder.subejectTextView.setText(roomInfos.get(position).getRoomName());
        holder.partipantsTextView.setText(String.valueOf(roomInfos.get(position).getRoomInNames().size())+"명");
        holder.professorTextView.setText(roomInfos.get(position).getProfessorName()+"교수님");
    }

    @Override
    public int getItemCount() {
        if(roomInfos==null) {
            return 0;
        }else{
            return roomInfos.size();
        }
    }

    public void setRoomInfos(ArrayList<RoomInfo> list){
        roomInfos=list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView subejectTextView;
        TextView partipantsTextView;
        TextView professorTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subejectTextView=itemView.findViewById(R.id.subjectTextView);
            partipantsTextView=itemView.findViewById(R.id.participantsTextView);
            professorTextView=itemView.findViewById(R.id.professorTextView);
            itemView.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION){
                        if(listener!=null){
                            listener.OnItemClick(v,position);
                        }
                    }
                }
            });
        }
    }

}
