package com.example.dialogflowbot.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dialogflowbot.R;
import com.example.dialogflowbot.models.Message;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

  private List<Message> messageList;
  private Activity activity;

  public ChatAdapter(List<Message> messageList, Activity activity) {
    this.messageList = messageList;
    this.activity = activity;
  }

  @NonNull @Override
  public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    //전개
    View view = LayoutInflater.from(activity).inflate(R.layout.adapter_message_one, parent, false);
    return new MyViewHolder(view);
  }

  // 전송한 msg인지, 받은 msg인지 체크해서 말풍선 추가
  @Override public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
    String message = messageList.get(position).getMessage();
    Log.i("position", ""+position);
    if (position == 0){
      add_DateLine(holder, messageList.get(position));
    }else if(position > 0){
      String pre_date = messageList.get(position-1).getDate();
      String cur_date = messageList.get(position).getDate();
      Log.i("pre_date", pre_date);
      Log.i("cur_date", cur_date);
      if (!pre_date.equals(cur_date)){
        Log.i("equals", ""+!pre_date.equals(cur_date));
        add_DateLine(holder, messageList.get(position));
      }else{
        holder.dateline.setVisibility(View.GONE);
      }
    }

    boolean isReceived = messageList.get(position).getIsReceived();
     if(isReceived){
       if (message.substring(0, 2).equals("id")){
         String[] msg_lst = message.split(" ");
         for(int i = 0; i < msg_lst.length; i++){
           Log.i("msg_lst", msg_lst[i]);
         } // "id 날씨 0 xx"
         int id = Integer.parseInt(msg_lst[2]);
         switch(id){
           case 1:
             message = "맑음";
             break;
           case 2:
             message = "흐림";
             break;
           case 3:
             message = "비";
             break;
           case 4:
             message = "눈";
             break;
           default:
             message = "정보 없음";
             break;
         }
       }
       Log.i("received message >>", message);

       holder.receive.setVisibility(View.VISIBLE);
//       holder.messageReceive.setVisibility(View.VISIBLE);
//       holder.tv_receiveTime.setVisibility(View.VISIBLE);
//       holder.messageSend.setVisibility(View.GONE);
       holder.send.setVisibility(View.GONE);

       holder.messageReceive.setText(message);
       holder.tv_receiveTime.setText(messageList.get(position).getTime());

     }else {
       Log.i("sended message >>", message);
       holder.send.setVisibility(View.VISIBLE);
//       holder.messageSend.setVisibility(View.VISIBLE);
       holder.receive.setVisibility(View.GONE);
//       holder.messageReceive.setVisibility(View.GONE);
       holder.messageSend.setText(message);
       holder.tv_sendTime.setText(messageList.get(position).getTime());
     }
  }

  private void add_DateLine(MyViewHolder holder, Message msg){
    String time = msg.getDate();
    holder.tv_date.setText(time);
    holder.dateline.setVisibility(View.VISIBLE);
  }
  public void clear() {
    int size = messageList.size();
    if (size > 0) {
      for (int i = 0; i < size; i++) {
        messageList.remove(0);
      }

      notifyItemRangeRemoved(0, size);
      notifyDataSetChanged();
    }
  }


  @Override public int getItemCount() {
    return messageList.size();
  }

  static class MyViewHolder extends RecyclerView.ViewHolder{

    LinearLayout chatAdapter;
    View dateline;
    TextView tv_date;
    LinearLayout send;
    RelativeLayout receive;
    TextView messageSend;
    TextView messageReceive;
    TextView tv_sendTime;
    TextView tv_receiveTime;

    MyViewHolder(@NonNull View itemView) {
      super(itemView);

      chatAdapter = itemView.findViewById(R.id.chatAdapter);

      dateline = itemView.findViewById(R.id.lyt_dateline);
      tv_date = dateline.findViewById(R.id.tv_date);
      send = itemView.findViewById(R.id.lyt_send);
      receive = itemView.findViewById(R.id.lyt_receive);
      messageSend = itemView.findViewById(R.id.message_send);
      messageReceive = itemView.findViewById(R.id.message_receive);
      tv_sendTime = itemView.findViewById(R.id.tv_sendTime);
      tv_receiveTime = itemView.findViewById(R.id.tv_receiveTime);
    }
  }

}
