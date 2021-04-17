package com.example.dialogflowbot.models;

import android.util.Log;

import java.util.Date;
import java.util.TimeZone;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Message {

  private String message;
  private boolean isReceived;
  private String datetime;

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
  }
  public Message(String message, boolean isReceived, String datetime) {
    this.message = message;
    this.isReceived = isReceived;
    this.datetime = datetime;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
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
  public int getIsReceived_flag(){
    if (isReceived)
      return 1;
    else
      return 0;
  }
  public String getTime(){
    String hour = this.datetime.substring(8, 10) + "시";
    String min = this.datetime.substring(10, 12) + "분";
    return hour+min;
  }
}
