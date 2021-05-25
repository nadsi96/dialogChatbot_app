package com.example.dialogflowbot.models;

import android.util.Log;

import com.example.dialogflowbot.rich_response.Card_Response;
import com.example.dialogflowbot.rich_response.Suggestion_Chips;

import java.util.Date;
import java.util.TimeZone;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Message {

  private String message;
  private boolean isReceived;
  private String datetime;
  private String hour;
  private String minute;
  private Card_Response card_response;
  private Suggestion_Chips suggestion_chips;

  public Message(String message, boolean isReceived) {
    this.message = message;
    this.isReceived = isReceived;

    Date date_now = new Date(System.currentTimeMillis()); // 현재시간을 가져와 Date형으로 저장한다
    DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    TimeZone timeZone = TimeZone.getTimeZone("Asia/Seoul");
    dateFormat.setTimeZone(timeZone);
    // 년월일시분초 14자리 포멧
//    SimpleDateFormat fourteen_format = new SimpleDateFormat("yyyyMMddHHmmss");
//    String time = fourteen_format.format(timeZone);
    String time = dateFormat.format(date_now);
    this.datetime = time;
    Log.i("time", time);
    setTime();
  }
  public Message(String message, boolean isReceived, String datetime) {
    this.message = message;
    this.isReceived = isReceived;
    this.datetime = datetime;
    setTime();
  }

  //Card_Response 입력
  public Message(Card_Response card_response){
    this((String)null, true);
    this.card_response = card_response;
  }
  public Message(Card_Response card_response, String datetime){
    this(card_response);
    this.datetime = datetime;
  }
  //Suggestion_Chips 입력
  public Message(Suggestion_Chips suggestion_chips){
    this((String) null, true);
    this.suggestion_chips = suggestion_chips;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Card_Response getCard_response(){
    return this.card_response;
  }

  public boolean getIsReceived() {
    return isReceived;
  }

  public void setIsReceived(boolean isReceived) {
    this.isReceived = isReceived;
  }

  public String getDatetime(){
    return this.datetime;
  }

  public String getHour(){
    return this.hour;
  }
  public String getMinute(){
    return this.minute;
  }

  public int getIsReceived_flag(){
    if (isReceived)
      return 1;
    else
      return 0;
  }

  public Suggestion_Chips getSuggestion_chips(){
    return this.suggestion_chips;
  }
  public String getSuggestion_chips_str(){
    String temp = "";
    String temp_items[] =getSuggestion_chips().getSuggestions();
    temp += temp_items[0];
    for (int i = 1; i < temp_items.length; i++){
      temp += ","+temp_items[i];
    }
    return temp;
  }
  private void setTime(){
    this.hour = this.datetime.substring(8, 10);
    this.minute = this.datetime.substring(10, 12);
  }

  public String getTime(){
    return hour+ " : " + minute;
  }
  public String getDate(){
    String year = this.datetime.substring(0, 4) + "년 ";
    String month = this.datetime.substring(4, 6) + "월 ";
    String day = this.datetime.substring(6, 8) + "일";
    return year + month + day;
  }
}
