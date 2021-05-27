package com.example.dialogflowbot.rich_response;

import android.graphics.drawable.Drawable;
import android.view.View;

public class Card_Response {

    private String title;
    private String text;
    private String image;
    private String btn_text;
    private String btn_url;

    public Card_Response(String title, String btn_text, String btn_url){
        this.title = title;
        this.btn_text = btn_text;
        this.btn_url = btn_url;
    }
    public Card_Response(String title, String text, String btn_text, String btn_url){
        this(title, btn_text, btn_url);
        this.text = text;
    }
    public Card_Response(String title, String text, String image, String btn_text, String btn_url){
        this(title, text, btn_text, btn_url);
        this.image = image;
    }
    public String getTitle(){ return this.title;}
    public String getText(){ return this.text;}
    public int getImage(){
        try{
            return Integer.parseInt(this.image);
        }catch (Exception e){
            return -1;
        }
    }
    public String getBtn_text(){return this.btn_text;}
    public String getBtn_url(){return this.btn_url;}
}


