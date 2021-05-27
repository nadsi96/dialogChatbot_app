package com.example.dialogflowbot.models;

import android.content.Context;
import android.widget.Toast;

import com.example.dialogflowbot.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class Settings {
    private final String filename = "TrustMe_Setting.txt";
    private int msg_send_Color;
    private int msg_receive_Color;
    private int txt_send_Color;
    private int txt_receive_Color;

    private final int[] colors = {R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent,R.color.blue,
            R.color.darker_gray, R.color.light_green, R.color.light_yellow, R.color.orange,
            R.color.black, R.color.white};

    public Settings(){

    }
    public int getMsg_send_Color(){
        return this.colors[this.msg_send_Color];
    }
    public int getMsg_receive_Color(){
        return this.colors[this.msg_receive_Color];
    }
    public int getTxt_receive_Color() {
        return this.colors[txt_receive_Color];
    }
    public int getTxt_send_Color() {
        return this.colors[txt_send_Color];
    }

    public void setMsg_receive_Color(int msg_receive_Color) {
        this.msg_receive_Color = msg_receive_Color;
    }
    public void setMsg_send_Color(int msg_send_Color) {
        this.msg_send_Color = msg_send_Color;
    }
    public void setColors(int sbg, int stxt, int rbg, int rtxt){
        this.msg_send_Color = sbg;
        this.msg_receive_Color = rbg;
        this.txt_send_Color = stxt;
        this.txt_receive_Color = rtxt;

        update_Setting();
    }

    private void read_Setting(){
        try {
            FileInputStream fis = new FileInputStream(this.filename);
            byte[] txt = new byte[128];
            fis.read(txt);
            String contents[] = (new String(txt)).split(" ");
            this.msg_send_Color = Integer.parseInt(contents[0]);
            this.txt_send_Color = Integer.parseInt(contents[1]);
            this.msg_receive_Color = Integer.parseInt(contents[2]);
            this.txt_receive_Color = Integer.parseInt(contents[3]);
        } catch (FileNotFoundException e) {
            setDefault();
        } catch (IOException e) {
            setDefault();
        }
    }

    private void setDefault(){
        this.msg_send_Color = 5;
        this.msg_receive_Color = 4;
        this.txt_receive_Color=8;
        this.txt_send_Color=8;
        update_Setting();
    }
    private void update_Setting(){
        try{
            FileOutputStream fos = new FileOutputStream(this.filename, false);
            String contents = this.msg_send_Color + " "+ this.txt_send_Color  +" "+ this.msg_receive_Color + " " + this.txt_receive_Color;
            fos.write(contents.getBytes());
            fos.close();
        }catch (FileNotFoundException e){

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
