package com.example.dialogflowbot.menu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import com.example.dialogflowbot.R;

public class YesorNoAlert extends AlertDialog {


    Context context;
    AlertDialog.Builder builder;
    private String title;
    private String content;

    public YesorNoAlert(Context context, String title, String content) {
        super(context);
        this.context = context;
        this.title = title;
        this.content = content;
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
            }
        });
    }

    public void create(){
        this.builder.create().show();
    }
}
