package com.example.dialogflowbot.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.dialogflowbot.models.Message;
import com.example.dialogflowbot.rich_response.Card_Response;
import com.example.dialogflowbot.rich_response.Suggestion_Chips;


public class DB_Handler {

    private static DB_Handler db_Handler = null;
    Context mycontext = null;

    private SQLiteDatabase database;

    private final String DB_dialogflow_chatbot = "dialogflow_chatbot.db";

    private final String TABLE_CHAT_RECORDS = "chat_records";
    private final String COLUMN_RECORDED_TIME = "recorded_time";
    private final String COLUMN_MESSAGE = "message";
    private final String COLUMN_IS_RECEIVED = "is_received";
    private final String COLUMN_CARD_RESPONSE_ID = "card_response_id";
    private final String COLUMN_SUGGESTION_CHIPS_ID = "suggestion_chips_id";

    private final String TABLE_CARD_RESPONSE = "card_responses";
    private final String COLUMN_CARD_ID = "card_id";
    private final String COLUMN_CARD_TITLE = "card_title";
    private final String COLUMN_CARD_TEXT = "card_text";
    private final String COLUMN_CARD_IMAGE = "card_image";
    private final String COLUMN_CARD_BTNTEXT = "card_btnText";
    private final String COLUMN_CARD_BTNURL = "card_btnUrl";

    private final String TABLE_SUGGESTION_CHIPS = "suggestion_chips";
    private final String COLUMN_SUGGESTION_ID = "suggestion_id";
    private final String COLUMN_SUGGESTION_TEXT = "suggestion_text";
    private final String COLUMN_SUGGESTION_ITEMS = "suggestion_items";

    private String tables[] = {TABLE_SUGGESTION_CHIPS, TABLE_CARD_RESPONSE, TABLE_CHAT_RECORDS};
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

        database.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_CARD_RESPONSE + "(" +
//                COLUMN_CARD_ID + " integer primary key autoincrement, "+
                COLUMN_CARD_ID + " integer primary key not null unique, "+
                COLUMN_CARD_TITLE + " text not null, " +
                COLUMN_CARD_TEXT + " text, " +
                COLUMN_CARD_IMAGE + " text, " +
                COLUMN_CARD_BTNTEXT + " text not null, " +
                COLUMN_CARD_BTNURL + " text not null)");

        database.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_SUGGESTION_CHIPS +  "(" +
                COLUMN_SUGGESTION_ID + " integer primary key not null unique, " +
                COLUMN_SUGGESTION_TEXT + " text not null, " +
                COLUMN_SUGGESTION_ITEMS + " text not null)");
        // card, suggestion_chips의 id를 기본키로 두지 않는 것이 메모리 면에서 더 효율적이지만 편의상 기본키로 지정

        database.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_CHAT_RECORDS + "(" +
                COLUMN_RECORDED_TIME + " text not null, "+
                COLUMN_MESSAGE + " text, " +
                COLUMN_IS_RECEIVED + " integer not null, " +
                COLUMN_CARD_RESPONSE_ID + " integer references "+TABLE_CARD_RESPONSE+" ("+COLUMN_CARD_ID+") on delete set null on update cascade, " +
                COLUMN_SUGGESTION_CHIPS_ID + " integer references " + TABLE_SUGGESTION_CHIPS + " (" + COLUMN_SUGGESTION_ID + ") on delete set null on update cascade)");


    }

    //db 생성
    //존재하는 db면 open, 없으면 create
//    private void createDatabase(Context context){
//        database = context.openOrCreateDatabase(DB_dialogflow_chatbot, Context.MODE_PRIVATE, null);
//    }

    public Message[] get_Chat_Records(){
        Cursor card_cursors = database.rawQuery("select * from " + TABLE_CARD_RESPONSE + " order by " + COLUMN_CARD_ID, null);

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
            if (message != null)
                chatRecords[i] = new Message(message, is_received, recorded_time);
            else{
                if(!cursor.isNull(3)){ // card_response_id가 null이 아닌지 확인
                    int card_id = cursor.getInt(3);
                    Cursor card_cursor = database.rawQuery("select * from " + TABLE_CARD_RESPONSE + " where " + COLUMN_CARD_ID + " = " + card_id, null);
                    card_cursor.moveToNext();
                    String card_title = card_cursor.getString(1);
                    String card_text = card_cursor.getString(2);
                    String card_image = card_cursor.getString(3);
                    String card_btnText = card_cursor.getString(4);
                    String card_btnUrl = card_cursor.getString(5);

                    Card_Response cr = new Card_Response(card_title, card_text, card_image, card_btnText, card_btnUrl);

                    chatRecords[i] = new Message(cr, recorded_time);
                }
                else if(!cursor.isNull(4)){ // suggestion_chips_id가 null이 아닌지 확인
                    int suggestion_id = cursor.getInt(4);
                    Cursor suggestion_cursor = database.rawQuery("select * from " + TABLE_SUGGESTION_CHIPS + " where " + COLUMN_SUGGESTION_ID + " = " + suggestion_id, null);
                    suggestion_cursor.moveToNext();
                    String text = suggestion_cursor.getString(1);
                    String temp_items = suggestion_cursor.getString(2);
                    String items[] = temp_items.split(",");
                    Suggestion_Chips sg = new Suggestion_Chips(text, items);
                    chatRecords[i] = new Message(sg);
                }
            }

        }
        return chatRecords;
    }

    public void insert_Chat(Message message){
        if (message.getCard_response() != null){
            Cursor cursor = database.rawQuery("select * from " + TABLE_CARD_RESPONSE, null);
            int count = cursor.getCount();

            database.execSQL("INSERT INTO " + TABLE_CARD_RESPONSE + " (" +
                    COLUMN_CARD_ID + ", " + COLUMN_CARD_TITLE + ", " + COLUMN_CARD_TEXT + ", " + COLUMN_CARD_IMAGE +
                    ", " + COLUMN_CARD_BTNTEXT + ", " + COLUMN_CARD_BTNURL + ") " +
                    "VALUES('" + count + "', '" +
                    message.getCard_response().getTitle() + "', '" +
                    message.getCard_response().getText() + "', '" +
                    message.getCard_response().getImage() + "', '" +
                    message.getCard_response().getBtn_text() + "', '" +
                    message.getCard_response().getBtn_url() + "')");
            database.execSQL("INSERT INTO " + TABLE_CHAT_RECORDS + " (" +
                    COLUMN_RECORDED_TIME + ", " + COLUMN_IS_RECEIVED + ", " + COLUMN_CARD_RESPONSE_ID + ")" +
                    "VALUES('" + message.getDatetime() + "', '" +
                    message.getIsReceived() + "', '" +
                    count + "')");

        }else if(message.getSuggestion_chips() != null){
            Cursor cursor = database.rawQuery("select * from " + TABLE_SUGGESTION_CHIPS, null);
            int count = cursor.getCount();
            database.execSQL("INSERT INTO " + TABLE_SUGGESTION_CHIPS + " ("+
                    COLUMN_SUGGESTION_ID + ", " + COLUMN_SUGGESTION_TEXT + ", " + COLUMN_SUGGESTION_ITEMS + ") " +
                    "values('" + count + "', '" +
                    message.getSuggestion_chips().getText() + "', '" +
                    message.getSuggestion_chips_str() + "')");

            database.execSQL("INSERT INTO " + TABLE_CHAT_RECORDS + " (" +
                    COLUMN_RECORDED_TIME + ", " + COLUMN_IS_RECEIVED + ", " + COLUMN_SUGGESTION_CHIPS_ID + ")" +
                    "VALUES('" + message.getDatetime() + "', '" +
                    message.getIsReceived() + "', '" +
                    count + "')");
        }else
            database.execSQL("INSERT INTO " + TABLE_CHAT_RECORDS + " ("+COLUMN_RECORDED_TIME +", " +COLUMN_MESSAGE +", "+COLUMN_IS_RECEIVED +")" +
                    "VALUES('" +message.getDatetime()+"', '" +message.getMessage()+"', '" +message.getIsReceived_flag()+"')");

    }

    public void delete_Chat_Record(){
        for (String table : this.tables){
            String sql = "Delete from " + table;
            database.execSQL(sql);
        }
//        String sql = "Delete from " + TABLE_CHAT_RECORDS;
//        database.execSQL(sql);
//        sql = "Delete from " + TABLE_CARD_RESPONSE;
//        database.execSQL(sql);
//        sql = "Delete from " + TABLE_SUGGESTION_CHIPS;
//        database.execSQL(sql);
    }
}
