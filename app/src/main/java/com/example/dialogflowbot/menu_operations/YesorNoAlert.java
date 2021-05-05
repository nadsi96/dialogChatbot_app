package com.example.dialogflowbot.menu_operations;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.example.dialogflowbot.interfaces.YesClicked;

public class YesorNoAlert extends AlertDialog {


    Context context;
    AlertDialog.Builder builder;
    private String title;
    private String content;
    YesClicked yesClicked;

    public YesorNoAlert(Context context, String title, String content, YesClicked yesClicked) {
        super(context);
        this.context = context;
        this.title = title;
        this.content = content;
        this.yesClicked = yesClicked;
        this.builder = new AlertDialog.Builder(context);
        setBuilder();
    }


    private void setBuilder(){
        this.builder.setMessage(this.content);
        this.builder.setTitle(this.title);
        this.builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                Toast.makeText(context, "cancel Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        this.builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                Toast.makeText(context, "yes Clicked", Toast.LENGTH_SHORT).show();
                yesClicked.yesClicked();
            }
        });
    }

    public void create(){
        this.builder.create().show();
    }
}
