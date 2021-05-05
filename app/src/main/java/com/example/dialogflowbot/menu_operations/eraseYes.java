package com.example.dialogflowbot.menu_operations;

import android.content.Context;
import android.widget.Toast;

import com.example.dialogflowbot.MainActivity;
import com.example.dialogflowbot.adapters.DB_Handler;
import com.example.dialogflowbot.interfaces.YesClicked;

public class eraseYes implements YesClicked {
    MainActivity _main;
    Context context;
    DB_Handler db_handler;

    public eraseYes(Context context, MainActivity _main){
        this._main = _main;
        this.context = context;
        db_handler = DB_Handler.getInstance(context);
    }
    @Override
    public void yesClicked() {
        db_handler.delete_Chat_Record();
        Toast.makeText(this.context, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
        _main.setEmptyMessageList();
    }
}
