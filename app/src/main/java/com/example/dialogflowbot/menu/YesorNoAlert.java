package com.example.dialogflowbot.menu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

public class YesorNoAlert extends AlertDialog {


    Context context;
    String title;
    String content;

    public YesorNoAlert(Context context, String title, String content) {
        super(context);
        this.context = context;
        setBuilder();
        this.create();
    }

    private void setBuilder(){
        this.setMessage(this.content);
        this.setTitle(this.title);
        this.setButton(0, "yes", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                Toast.makeText(context, "yes Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        this.setButton(1, "cancel", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialogInterface, int id){
                Toast.makeText(context, "cancel Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
