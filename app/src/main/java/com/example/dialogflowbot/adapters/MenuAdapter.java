package com.example.dialogflowbot.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.dialogflowbot.MainActivity;
import com.example.dialogflowbot.R;
import com.example.dialogflowbot.menu_operations.YesorNoAlert;
import com.example.dialogflowbot.menu_operations.eraseYes;
import com.example.dialogflowbot.menu_operations.exitYes;
import com.example.dialogflowbot.models.Settings;

public class MenuAdapter implements View.OnClickListener{

    Context context = null;
    MainActivity _main;
    LinearLayout menu;

//    LinearLayout colorMenu;
//    Settings setting;
//    ImageView color_set[][];

    public MenuAdapter(Context context, MainActivity _main, LinearLayout menu) {
        this.context = context;
        this._main = _main;
        this.menu = menu;
    }

//    public MenuAdapter(Context context, MainActivity _main, Settings setting, ImageView color_set[][], LinearLayout menu, LinearLayout colorMenu) {
//        this.context = context;
//        this._main = _main;
//        this.menu = menu;
//        this.colorMenu = colorMenu;
//        this.setting = setting;
//        this.color_set = color_set;
//    }


    public void onClick(View view){
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
//            case R.id.btn_apply_color:
//                colorMenu.setVisibility(View.VISIBLE);
//                ColorMenu cm = new ColorMenu(setting, color_set);
//                cm.applyColorClicked.onClick(view);
//                colorMenu.setVisibility(View.GONE);
//                break;
//            case R.id.btn_cancel_color:
//                colorMenu.setVisibility(View.GONE);
//                break;
            default:
                break;
        }
    }
    private View.OnClickListener menuClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(menu.getVisibility() == View.GONE)
                menu.setVisibility(View.VISIBLE);
            else
                menu.setVisibility(View.GONE);
        }
    };

    private View.OnClickListener settingClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            colorMenu.setVisibility(View.VISIBLE);
            Toast.makeText(context, "Setting Clicked", Toast.LENGTH_SHORT).show();
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
            alert_exit.create();
//            Toast.makeText(context, "exit", Toast.LENGTH_SHORT).show();
        }
    };
//
//
//    class ColorMenu{
//
//        ImageView color_set[][];
//
//        int sbg, stxt, rbg, rtxt;
//
//        Settings setting;
//        public ColorMenu(Settings setting, ImageView color_set[][]){
//
//            this.color_set = color_set;
//
//            this.rbg = setting.getMsg_receive_Color();
//            this.rtxt = setting.getTxt_receive_Color();
//            this.sbg = setting.getMsg_send_Color();
//            this.stxt = setting.getTxt_send_Color();
//
//            int color = context.getResources().getColor(R.color.colorAccent);
//            color_set[0][sbg].setBackgroundColor(color);
//            color_set[1][stxt].setBackgroundColor(color);
//            color_set[2][rbg].setBackgroundColor(color);
//            color_set[3][rtxt].setBackgroundColor(color);
//
//            this.setting = setting;
//        }
//
//        View.OnClickListener applyColorClicked = new View.OnClickListener(){
//            public void onClick(View view){
//                applyClick();
//            }
//        };
//
//        private void applyClick(){
//            this.setting.setColors(sbg, stxt, rbg, rtxt);
//        }
//
//        private void setbgColorDefault(int i, int j){
//            int color = Color.TRANSPARENT;
//            for(int idx_1 = 0; idx_1 < 4; idx_1++){
//                for(int idx_2 = 0; idx_2 < 10; idx_2++){
//                    this.color_set[idx_1][idx_2].setBackgroundColor(color);
//                }
//            }
//
//            color = context.getResources().getColor(R.color.colorAccent);
//            this.color_set[i][j].setBackgroundColor(color);
//            switch(i){
//                case 0:
//                    sbg = j;
//                    break;
//                case 1:
//                    stxt = j;
//                    break;
//                case 2:
//                    rbg = j;
//                    break;
//                case 3:
//                    rtxt = j;
//                    break;
//            }
//        }
//
//        View.OnClickListener colorIconClicked = new View.OnClickListener(){
//            public void onClick(View view){
//                switch(view.getId()){
//                    case R.id.color_send_bg_1:
//                        setbgColorDefault(0,0);
//                        break;
//                    case R.id.color_send_bg_2:
//                        setbgColorDefault(0,1);
//                        break;
//                    case R.id.color_send_bg_3:
//                        setbgColorDefault(0,2);
//                        break;
//                    case R.id.color_send_bg_4:
//                        setbgColorDefault(0,3);
//                        break;
//                    case R.id.color_send_bg_5:
//                        setbgColorDefault(0,4);
//                        break;
//                    case R.id.color_send_bg_6:
//                        setbgColorDefault(0,5);
//                        break;
//                    case R.id.color_send_bg_7:
//                        setbgColorDefault(0,6);
//                        break;
//                    case R.id.color_send_bg_8:
//                        setbgColorDefault(0,7);
//                        break;
//                    case R.id.color_send_bg_9:
//                        setbgColorDefault(0,8);
//                        break;
//                    case R.id.color_send_bg_10:
//                        setbgColorDefault(0,9);
//                        break;
//
//                    case R.id.color_send_txt_1:
//                        setbgColorDefault(1,0);
//                        break;
//                    case R.id.color_send_txt_2:
//                        setbgColorDefault(1,1);
//                        break;
//                    case R.id.color_send_txt_3:
//                        setbgColorDefault(1,2);
//                        break;
//                    case R.id.color_send_txt_4:
//                        setbgColorDefault(1,3);
//                        break;
//                    case R.id.color_send_txt_5:
//                        setbgColorDefault(1,4);
//                        break;
//                    case R.id.color_send_txt_6:
//                        setbgColorDefault(1,5);
//                        break;
//                    case R.id.color_send_txt_7:
//                        setbgColorDefault(1,6);
//                        break;
//                    case R.id.color_send_txt_8:
//                        setbgColorDefault(1,7);
//                        break;
//                    case R.id.color_send_txt_9:
//                        setbgColorDefault(1,8);
//                        break;
//                    case R.id.color_send_txt_10:
//                        setbgColorDefault(1,9);
//                        break;
//
//                    case R.id.color_receive_bg_1:
//                        setbgColorDefault(2,0);
//                        break;
//                    case R.id.color_receive_bg_2:
//                        setbgColorDefault(2,1);
//                        break;
//                    case R.id.color_receive_bg_3:
//                        setbgColorDefault(2,2);
//                        break;
//                    case R.id.color_receive_bg_4:
//                        setbgColorDefault(2,3);
//                        break;
//                    case R.id.color_receive_bg_5:
//                        setbgColorDefault(2,4);
//                        break;
//                    case R.id.color_receive_bg_6:
//                        setbgColorDefault(2,5);
//                        break;
//                    case R.id.color_receive_bg_7:
//                        setbgColorDefault(2,6);
//                        break;
//                    case R.id.color_receive_bg_8:
//                        setbgColorDefault(2,7);
//                        break;
//                    case R.id.color_receive_bg_9:
//                        setbgColorDefault(2,8);
//                        break;
//                    case R.id.color_receive_bg_10:
//                        setbgColorDefault(2,9);
//                        break;
//
//                    case R.id.color_receive_txt_1:
//                        setbgColorDefault(3,0);
//                        break;
//                    case R.id.color_receive_txt_2:
//                        setbgColorDefault(3,1);
//                        break;
//                    case R.id.color_receive_txt_3:
//                        setbgColorDefault(3,2);
//                        break;
//                    case R.id.color_receive_txt_4:
//                        setbgColorDefault(3,3);
//                        break;
//                    case R.id.color_receive_txt_5:
//                        setbgColorDefault(3,4);
//                        break;
//                    case R.id.color_receive_txt_6:
//                        setbgColorDefault(3,5);
//                        break;
//                    case R.id.color_receive_txt_7:
//                        setbgColorDefault(3,6);
//                        break;
//                    case R.id.color_receive_txt_8:
//                        setbgColorDefault(3,7);
//                        break;
//                    case R.id.color_receive_txt_9:
//                        setbgColorDefault(3,8);
//                        break;
//                    case R.id.color_receive_txt_10:
//                        setbgColorDefault(3,9);
//                        break;
//
//                }
//            }
//        };
//    }
}
