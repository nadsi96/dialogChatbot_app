package com.example.dialogflowbot.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.dialogflowbot.MainActivity;
import com.example.dialogflowbot.R;
import com.example.dialogflowbot.menu_operations.YesorNoAlert;
import com.example.dialogflowbot.menu_operations.eraseYes;
import com.example.dialogflowbot.menu_operations.exitYes;

public class MenuAdapter implements View.OnClickListener{

    Context context = null;
    MainActivity _main;
    LinearLayout menu;

    public MenuAdapter(Context context, MainActivity _main,  LinearLayout menu) {
        this.context = context;
        this._main = _main;
        this.menu = menu;

    }

    public void onClick(View view){
        Log.i("clicked", ""+view.getId());
        switch (view.getId()){
            case R.id.img_menu:
                menuClicked.onClick(view);
                break;
            case R.id.menu_setting:
                settingClicked.onClick(view);
                break;
            case R.id.menu_erase:
                eraseClicked.onClick(view);
                break;
            case R.id.menu_exit:
                exitClicked.onClick(view);
                break;
            default:
                break;
        }
    }
    private View.OnClickListener menuClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i("menu", "clicked");
            if(menu.getVisibility() == View.GONE)
                menu.setVisibility(View.VISIBLE);
            else
                menu.setVisibility(View.GONE);
        }
    };

    private View.OnClickListener settingClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(context, "setting", Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener eraseClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //채팅 기록 삭제
            YesorNoAlert alert_delete_record = new YesorNoAlert(context, "Delete Chat Record", "Are you sure to delete?", new eraseYes(context, _main));
            alert_delete_record.create();
        }
    };

    private View.OnClickListener exitClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //앱 종료
            YesorNoAlert alert_exit = new YesorNoAlert(context, "Exit", "Are you sure to exit?", new exitYes(context));
            Toast.makeText(context, "exit", Toast.LENGTH_SHORT).show();
        }
    };
}
