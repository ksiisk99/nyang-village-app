package com.uni.aychat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uni.aychat.R;
import com.uni.aychat.db.entity.ChatInfo;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter{
    List<ChatInfo> chatInfos=null;
    public static ChatAdapter instance=null;
    private static final int TYPE_SENT=3;
    private static final int TYPE_SENT2=5;
    private static final int TYPE_RECEIVED=2;
    private static final int TYPE_RECEIVED2=4;
    private static final int TYPE_ENTER=0;
    private static final int TYPE_EXIT=1;
    public static int connect=-1;
    private LayoutInflater inflater=null;
    public synchronized static ChatAdapter getInstance(){
        if(instance==null){
            instance=new ChatAdapter();
        }
        return instance;
    }

    public void initInstance(){
        instance=null;
    }

    public interface OnItemClickListener{
        void OnItemClick(View view, int position);
    }
    private ListAdapter.OnItemClickListener listener=null;
    public void setOnItemClickListener(ListAdapter.OnItemClickListener listener){this.listener=listener;}
    public void setInflater(LayoutInflater inflater){this.inflater=inflater;}

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch(viewType) {
            case 0:
            case 1:
                view=inflater.inflate(R.layout.enter_item,parent,false);
                return new EnterHolder(view);
            case 2:
                view=inflater.inflate(R.layout.receive_item,parent,false);
                return new ReceiveMessageHolder(view);
            case 3:
                view=inflater.inflate(R.layout.send_item,parent,false);
                return new SendMessageHolder(view);
            case 4:
                view=inflater.inflate(R.layout.receive2_item,parent,false);
                return new Receive2MessageHolder(view);
            case 5:
                view=inflater.inflate(R.layout.send2_item,parent,false);
                return new Send2MessageHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof EnterHolder){ //입장 및 퇴장
            ((EnterHolder) holder).enter.setText(chatInfos.get(position).getContent());
        }else if(holder instanceof ReceiveMessageHolder){
            ((ReceiveMessageHolder) holder).receiveMessage.setText(chatInfos.get(position).getContent());
            ((ReceiveMessageHolder) holder).receiveTime.setText(chatInfos.get(position).getTime());
            ((ReceiveMessageHolder) holder).receiveName.setText(chatInfos.get(position).getName());
        }else if(holder instanceof SendMessageHolder){
            ((SendMessageHolder) holder).sendMessage.setText(chatInfos.get(position).getContent());
            ((SendMessageHolder) holder).sendTime.setText(chatInfos.get(position).getTime());
            ((SendMessageHolder) holder).sendName.setText(chatInfos.get(position).getName());
        }else if(holder instanceof Receive2MessageHolder){
            ((Receive2MessageHolder) holder).receiveMessage.setText(chatInfos.get(position).getContent());
            ((Receive2MessageHolder) holder).receiveTime.setText(chatInfos.get(position).getTime());
        }else if(holder instanceof Send2MessageHolder){
            ((Send2MessageHolder) holder).sendMessage.setText(chatInfos.get(position).getContent());
            ((Send2MessageHolder) holder).sendTime.setText(chatInfos.get(position).getTime());
        }
    }

//    @Override
//    public long getItemId(int position) {
//        return chatInfos.get(position).getId();
//    }

    @Override
    public int getItemViewType(int position) {
        if(position>0){
            switch(chatInfos.get(position).getType()){
                case 0:
                case 1:
                    return 1;
                case 2:
                    if(chatInfos.get(position-1).getName().equals(chatInfos.get(position).getName())){
                        return 4;
                    }else
                        return 2;
                default:
                    if(chatInfos.get(position-1).getName().equals(chatInfos.get(position).getName())){
                        return 5;
                    }else
                        return 3;
            }
        }else
            return chatInfos.get(position).getType();
    }

    public class EnterHolder extends RecyclerView.ViewHolder{
        TextView enter;
        public EnterHolder(@NonNull View itemView) {
            super(itemView);
            enter=itemView.findViewById(R.id.enter);
        }
    }

    public class SendMessageHolder extends RecyclerView.ViewHolder{
        TextView sendMessage, sendTime, sendName;

        public SendMessageHolder(@NonNull View itemView) {
            super(itemView);
            sendMessage=itemView.findViewById(R.id.sendMessage);
            sendTime=itemView.findViewById(R.id.sendTime);
            sendName=itemView.findViewById(R.id.sendName);
        }
    }

    public class Send2MessageHolder extends RecyclerView.ViewHolder{
        TextView sendMessage, sendTime;

        public Send2MessageHolder(@NonNull View itemView) {
            super(itemView);
            sendMessage=itemView.findViewById(R.id.send2Message);
            sendTime=itemView.findViewById(R.id.send2Time);
        }
    }

    public class ReceiveMessageHolder extends RecyclerView.ViewHolder{
        TextView receiveName,receiveMessage,receiveTime;

        public ReceiveMessageHolder(@NonNull View itemView) {
            super(itemView);
            receiveMessage=itemView.findViewById(R.id.receiveMessage);
            receiveTime=itemView.findViewById(R.id.receiveTime);
            receiveName=itemView.findViewById(R.id.receiveName);

            itemView.setOnLongClickListener(new View.OnLongClickListener() { //신고
                @Override
                public boolean onLongClick(View view) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION){
                        if(listener!=null){
                            listener.OnItemClick(view,position);
                        }
                    }
                    return true;
                }
            });
        }
    }

    public class Receive2MessageHolder extends RecyclerView.ViewHolder{
        TextView receiveMessage,receiveTime;

        public Receive2MessageHolder(@NonNull View itemView) {
            super(itemView);
            receiveMessage=itemView.findViewById(R.id.receive2Message);
            receiveTime=itemView.findViewById(R.id.receive2Time);

            itemView.setOnLongClickListener(new View.OnLongClickListener() { //신고
                @Override
                public boolean onLongClick(View view) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION){
                        if(listener!=null){
                            listener.OnItemClick(view,position);
                        }
                    }
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(chatInfos==null)
            return 0;
        else return chatInfos.size();
    }

    public void setChatInfos(List<ChatInfo> chatInfos) {
        this.chatInfos = chatInfos;
        notifyDataSetChanged();
    }

    public void addChatInfos(ChatInfo chatInfo){
        chatInfos.add(chatInfo); //채팅데이터 받으면 저장.
        notifyItemInserted(chatInfos.size()-1);
    }

    public static int isConnect(){
        return connect;
    }
}
