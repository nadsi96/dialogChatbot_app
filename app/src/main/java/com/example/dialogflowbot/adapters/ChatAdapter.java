package com.example.dialogflowbot.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dialogflowbot.MainActivity;
import com.example.dialogflowbot.R;
import com.example.dialogflowbot.models.Message;
import com.example.dialogflowbot.rich_response.Card_Response;
import com.example.dialogflowbot.rich_response.Suggestion_Chips;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

  private List<Message> messageList;
  private Activity activity;
  private MainActivity _main;

  public ChatAdapter(List<Message> messageList, Activity activity, MainActivity _main) {
    this.messageList = messageList;
    this.activity = activity;
    this._main = _main;
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

    //dateline 입력 여부 확인하여 처리
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
      //수신한 메시지인 경우
      if(messageList.get(position).getCard_response() != null){
        // card message인 경우
        visible_Card_Response(holder, messageList.get(position).getCard_response(), position);
      }
      else if(messageList.get(position).getSuggestion_chips() != null){
        //suggest chips message인 경우
        visible_Suggestion_Chips(holder, messageList.get(position).getSuggestion_chips(), position);
      }
      else if(message != null){
        // text message인 경우
        visible_Received(holder, message, position);
//         holder.receive.setVisibility(View.VISIBLE);
//         holder.messageReceive.setText(message);
//         holder.tv_receiveTime.setText(messageList.get(position).getTime());
        Log.i("received message >>", message);
      }

      holder.send.setVisibility(View.GONE);


    }else {
      Log.i("sended message >>", message);
      visible_Send(holder, message, position);
//       holder.send.setVisibility(View.VISIBLE);
////       holder.messageSend.setVisibility(View.VISIBLE);
//       holder.receive.setVisibility(View.GONE);
////       holder.messageReceive.setVisibility(View.GONE);
//       holder.messageSend.setText(message);
//       holder.tv_sendTime.setText(messageList.get(position).getTime());
    }
  }

  private void visible_Send(MyViewHolder holder, String message, int position){
    holder.send.setVisibility(View.VISIBLE);
    holder.messageSend.setText(message);
    holder.tv_sendTime.setText(messageList.get(position).getTime());

    holder.receive.setVisibility(View.GONE);
    holder.card_response.setVisibility(View.GONE);
    holder.lyt_suggestion_chips.setVisibility(View.GONE);
  }
  private void visible_Received(MyViewHolder holder, String message, int position){
    holder.receive.setVisibility(View.VISIBLE);
    holder.messageReceive.setText(message);
    holder.tv_receiveTime.setText(messageList.get(position).getTime());

    holder.send.setVisibility(View.GONE);
    holder.card_response.setVisibility(View.GONE);
    holder.lyt_suggestion_chips.setVisibility(View.GONE);
  }
  private void visible_Card_Response(MyViewHolder holder, Card_Response card_response, int position){
    holder.setCard(card_response);
    holder.card.setVisibility(View.VISIBLE);
    holder.card_response.setVisibility(View.VISIBLE);
    holder.tv_receiveTime_card.setVisibility(View.VISIBLE);

    holder.send.setVisibility(View.GONE);
    holder.receive.setVisibility(View.GONE);
    holder.lyt_suggestion_chips.setVisibility(View.GONE);
  }
  private void visible_Suggestion_Chips(MyViewHolder holder, Suggestion_Chips sg, int position){
    holder.setSuggestion_chips(sg);
    holder.lyt_suggestion_chips.setVisibility(View.VISIBLE);
    holder.suggestion_chips.setVisibility(View.VISIBLE);

    holder.send.setVisibility(View.GONE);
    holder.receive.setVisibility(View.GONE);
    holder.card_response.setVisibility(View.GONE);
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

  class MyViewHolder extends RecyclerView.ViewHolder{

    LinearLayout chatAdapter;
    View dateline;
    TextView tv_date;
    LinearLayout send;
    RelativeLayout receive;
    TextView messageSend;
    TextView messageReceive;
    TextView tv_sendTime;
    TextView tv_receiveTime;

    //Card Response
    RelativeLayout card_response;
    TextView tv_receiveTime_card;
    View card;

    //Suggestion Chips
    RelativeLayout lyt_suggestion_chips;
    View suggestion_chips;
    RecyclerView r_v;

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

      card_response = itemView.findViewById(R.id.lyt_receive_card);
      tv_receiveTime_card = itemView.findViewById(R.id.tv_receiveTime_card);
      card = itemView.findViewById(R.id.card_received);

      lyt_suggestion_chips = itemView.findViewById(R.id.lyt_receive_suggestion_chips);
      suggestion_chips = itemView.findViewById(R.id.suggestions_chips);
    }

    protected void setCard(final Card_Response card_res){
      TextView card_Title =  card.findViewById(R.id.tv_card_title);
      card_Title.setText(card_res.getTitle());

      TextView card_Text = card.findViewById(R.id.tv_card_text);
      card_Text.setText(card_res.getText());

      ImageView card_Image = card.findViewById(R.id.img_card_img);
      card_Image.setVisibility(View.GONE);
      if (card_res.getImage() == null){
        card_Image.setVisibility(View.GONE);
      }else{
//        card_Image.;
//        card_Image.setVisibility(View.VISIBLE);
      }

      Button card_Button = card.findViewById(R.id.btn_card_btn);
      card_Button.setText(card_res.getBtn_text());
      card_Button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(card_res.getBtn_url()));
          activity.startActivity(intent);
        }
      });

    }

    protected void setSuggestion_chips(final Suggestion_Chips sg){
      TextView s_tv = suggestion_chips.findViewById(R.id.tv_suggestions_text);
      s_tv.setText(sg.getText());
      String items[] = sg.getSuggestions();

      LinearLayout suggestion_items_root = (LinearLayout) suggestion_chips.findViewById(R.id.suggestion_chips_items);
//      LinearLayout lin = (LinearLayout)View.inflate(activity, R.layout.suggestion_item, suggestion_items_root);
      for(final String item : items){
        LinearLayout lin = new LinearLayout(activity);
        lin.setPadding(5,5,5,5);

        Button btn = new Button(activity);
        btn.setText(item);
        btn.setBackgroundResource(R.drawable.suggestion_item_background);
        btn.setTextColor(Color.WHITE);
        btn.setTextSize(16);
        btn.setPadding(5,5,5,5);
        btn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            _main.sendMessageToBot(item);
          }
        });
        lin.addView(btn);
        suggestion_items_root.addView(lin);
      }
//      r_v = suggestion_chips.findViewById(R.id.suggestions_view);
//      Suggestion_Chips_Adapter adapter = new Suggestion_Chips_Adapter(sg.getText(), sg.getSuggestions(),suggestion_chips);
//      r_v.setAdapter(adapter);
//      adapter.notifyDataSetChanged();
    }
  }

}
