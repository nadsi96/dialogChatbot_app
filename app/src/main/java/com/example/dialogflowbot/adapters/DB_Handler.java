package com.example.dialogflowbot.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.dialogflowbot.models.Message;


public class DB_Handler {

    private static DB_Handler db_Handler = null;
    Context mycontext = null;

    private SQLiteDatabase database;

    private final String DB_dialogflow_chatbot = "dialogflow_chatbot.db";
    private final String TABLE_CHAT_RECORDS = "chat_records";
    private final String COLUMN_RECORDED_TIME = "recorded_time";
    private final String COLUMN_MESSAGE = "message";
    private final String COLUMN_IS_RECEIVED = "is_received";

    public static DB_Handler getInstance(Context context)
    {
        if(db_Handler == null)
        {
            db_Handler = new DB_Handler(context);
        }

        return db_Handler;
    }

    private DB_Handler(Context context)
    {
//        createDatabase(context);
        database = context.openOrCreateDatabase(DB_dialogflow_chatbot, Context.MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_CHAT_RECORDS + "(" + COLUMN_RECORDED_TIME + " text, "+ COLUMN_MESSAGE + "text, " + COLUMN_IS_RECEIVED + " text)");
    }

    //db 생성
    //존재하는 db면 open, 없으면 create
//    private void createDatabase(Context context){
//        database = context.openOrCreateDatabase(DB_dialogflow_chatbot, Context.MODE_PRIVATE, null);
//    }

    public Message[] get_Chat_Records(){
        Cursor cursor = database.rawQuery("select * from " + TABLE_CHAT_RECORDS + " order by recorded_time", null);
        int cnt = cursor.getCount();
        Message[] chatRecords = new Message[cnt];
        for(int i = 0; i < cnt; i++){
            cursor.moveToNext();
            String recorded_time = cursor.getString(0);
            String message = cursor.getString(1);
            int is_received_flag = cursor.getInt(2);
            boolean is_received = false;
            if (is_received_flag == 1){
                is_received = true;
            }
            chatRecords[i] = new Message(message, is_received, recorded_time);
        }
        return chatRecords;
    }

    public void insert_Chat(Message message){

        database.execSQL("INSERT INTO " + TABLE_CHAT_RECORDS + " ("+COLUMN_RECORDED_TIME +", " +COLUMN_MESSAGE +", "+COLUMN_IS_RECEIVED +")"
                + "VALUES('" +message.getDatetime()+"', '" +message.getMessage()+"', '" +message.getIsReceived_flag()+"')");

    }


}
