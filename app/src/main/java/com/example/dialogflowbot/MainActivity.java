package com.example.dialogflowbot;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dialogflowbot.adapters.ChatAdapter;
import com.example.dialogflowbot.adapters.DB_Handler;
import com.example.dialogflowbot.helpers.SendMessageInBg;
import com.example.dialogflowbot.interfaces.BotReply;
import com.example.dialogflowbot.adapters.MenuAdapter;
import com.example.dialogflowbot.models.Message;
import com.example.dialogflowbot.rich_response.Card_Response;
import com.example.dialogflowbot.rich_response.Suggestion_Chips;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.Intent;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.SessionsSettings;
import com.google.cloud.dialogflow.v2.TextInput;
import com.google.common.collect.Lists;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements BotReply {

  RecyclerView chatView;
  ChatAdapter chatAdapter;
  List<Message> messageList = new ArrayList<>();
  EditText editMessage;
  ImageButton btnSend;

  private ImageView img_menu;
  LinearLayout menu;
  private LinearLayout menu_setting, menu_erase, menu_exit;

  private int[] menuXY = new int[2];
  private int[] tomenuXY;

  //dialogFlow
  private SessionsClient sessionsClient;
  private SessionName sessionName;
  private String uuid = UUID.randomUUID().toString();//식별번호 생성
  private String TAG = "mainactivity";

  private DB_Handler db_handler;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Log.i("onCreate", "start");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    chatView = findViewById(R.id.chatView);
    editMessage = findViewById(R.id.editMessage);
    btnSend = findViewById(R.id.btnSend);

    chatAdapter = new ChatAdapter(messageList, this, this);
    chatView.setAdapter(chatAdapter);

    img_menu = findViewById(R.id.img_menu);
    menu = findViewById(R.id.menu);
//    menu.setVisibility(View.GONE);

    menu_setting = findViewById(R.id.menu_setting);
    menu_erase = findViewById(R.id.menu_erase);
    menu_exit = findViewById(R.id.menu_exit);

    db_handler = DB_Handler.getInstance(this);

    findViewById(R.id.img_chatBackground).setAlpha(0.5f);
    setUpBot();
    set_Recorded_Chat();

    setButtons();
    Log.i("onCreate", "end");
  }

  @Override
  protected void onResume() {
    Log.i("onResume", "start");
    super.onResume();
//    menu.setVisibility(View.VISIBLE);
//    menu.getLocationOnScreen(menuXY);
//    tomenuXY = new int[]{menuXY[0] + menu.getWidth(), menuXY[1] + menu.getHeight()};
    menu.setVisibility(View.GONE);
//    Log.i("XXX", ""+(menuXY[0]));
//    Log.i("to", ""+(tomenuXY[0]));
//    Log.i("YYY", ""+(menuXY[1]));
//    Log.i("to", ""+(tomenuXY[1]));
    Log.i("onResume", "end");
  }

  private void setButtons(){
    btnSend.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        String message = editMessage.getText().toString();
        if (!message.isEmpty()) {
          messageList.add(new Message(message, false));
          db_handler.insert_Chat(messageList.get(messageList.size()-1));
          editMessage.setText("");
          sendMessageToBot(message);

          // null 체크 후 null이 아니면 그대로 반환, null이면 NPE 발생
          // notifyDataSetChanged() -> ListView 새로고침(업데이트)
          // RecyclerView.getAdapter() -> Retrieves the previously set adapter or null if no adapter is set.
          //                              현재 설정된 adapter(설정된 것이 없다면 null) 반환
          Objects.requireNonNull(chatView.getAdapter()).notifyDataSetChanged();
          Objects.requireNonNull(chatView.getLayoutManager())
                  .scrollToPosition(messageList.size() - 1);
        } else {
          Toast.makeText(MainActivity.this, "Please enter text!", Toast.LENGTH_SHORT).show();
        }
      }
    });


    MenuAdapter menuAdapter = new MenuAdapter(MainActivity.this, this, this.menu);
    img_menu.setOnClickListener(menuAdapter);
    menu_setting.setOnClickListener(menuAdapter);
    menu_erase.setOnClickListener(menuAdapter);
    menu_exit.setOnClickListener(menuAdapter);
  }

  private void setUpBot() {
    try {
//      InputStream stream = this.getResources().openRawResource(R.raw.credential);
      InputStream stream = this.getResources().openRawResource(R.raw.credential);
      GoogleCredentials credentials = GoogleCredentials.fromStream(stream)
              .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
      String projectId = ((ServiceAccountCredentials) credentials).getProjectId();

      SessionsSettings.Builder settingsBuilder = SessionsSettings.newBuilder();
      SessionsSettings sessionsSettings = settingsBuilder.setCredentialsProvider(
              FixedCredentialsProvider.create(credentials)).build();
      sessionsClient = SessionsClient.create(sessionsSettings);
      sessionName = SessionName.of(projectId, uuid);

      Log.d(TAG, "projectId : " + projectId);
    } catch (Exception e) {
      Log.d(TAG, "setUpBot: " + e.getMessage());
    }
  }

  public void sendMessageToBot(String message) {
    QueryInput input = QueryInput.newBuilder()
            .setText(TextInput.newBuilder().setText(message).setLanguageCode("en-US")).build();
    new SendMessageInBg(this, sessionName, sessionsClient, input).execute();
  }

  // chatbot의 답변 추가
  @Override
  public void callback(DetectIntentResponse returnResponse) {
    Log.i("callback", "hello");
    Log.i("callback returnResponse", returnResponse.getQueryResult().getFulfillmentText() + "");
    Log.i("callback getAction", returnResponse.getQueryResult().getAction() + "");
    Log.i("callback getAllFields", returnResponse.getQueryResult().getAllFields() + "");
    boolean flag = true;

    if(returnResponse!=null) {
      String botReply = returnResponse.getQueryResult().getFulfillmentText();
      Log.i("callback botReply", botReply + "");
      String action = returnResponse.getQueryResult().getAction();

      if(action.equals("input.test")){
        Intent.Message.Card card = returnResponse.getQueryResult().getFulfillmentMessages(1).getCard();
//        Intent.Message.QuickReplies replies = returnResponse.getQueryResult().getFulfillmentMessages(2).getQuickReplies();
//        Log.i("111", replies.getQuickReplies(0));
//        Log.i("2", replies.getQuickReplies(1));
        Log.i("card_Button_Count", card.getButtonsCount()+"");
        String title = card.getTitle();
        String text = card.getSubtitle();
        String image = card.getImageUri();
        String btnText = card.getButtons(0).getText();
        String btnUrl = card.getButtons(0).getPostback();
        Log.i("card", btnText);
        Log.i("card", btnUrl);
        Log.i("card", title);
        Log.i("card", text);
        Log.i("card", image);

        messageList.add(new Message(new Card_Response(title, text, image, btnText, btnUrl)));
        chatAdapter.notifyDataSetChanged();
        db_handler.insert_Chat(messageList.get(messageList.size()-1));
        Objects.requireNonNull(chatView.getLayoutManager()).scrollToPosition(messageList.size() - 1);
        flag = false;

      }else if(action.equals("input.tel")){
        Intent.Message.QuickReplies replies = returnResponse.getQueryResult().getFulfillmentMessages(1).getQuickReplies();
        String str_lst[] = replies.getQuickRepliesList().toArray(new String[0]);
        for (int i = 0; i < str_lst.length;i++){
          Log.i("input.tel", str_lst[i]);
        }
        messageList.add(new Message(new Suggestion_Chips(botReply, str_lst)));
        chatAdapter.notifyDataSetChanged();
        db_handler.insert_Chat(messageList.get(messageList.size()-1));
        Objects.requireNonNull(chatView.getLayoutManager()).scrollToPosition(messageList.size() - 1);
        flag = false;
      }

      if(!botReply.isEmpty() & flag){
        messageList.add(new Message(botReply, true));
        chatAdapter.notifyDataSetChanged();
        db_handler.insert_Chat(messageList.get(messageList.size()-1));
        Objects.requireNonNull(chatView.getLayoutManager()).scrollToPosition(messageList.size() - 1);
      }else {
        Toast.makeText(this, "something wrong", Toast.LENGTH_SHORT).show();
      }
    } else {
      Toast.makeText(this, "failed to connect!", Toast.LENGTH_SHORT).show();
    }
  }

  // 이전 기록 추가
  private void set_Recorded_Chat(){
    Message[] chatRecords = db_handler.get_Chat_Records();

    if (chatRecords.length > 0) {
      //messageList에 add
      for (int i = 0; i < chatRecords.length; i++) {
        messageList.add(chatRecords[i]);
      }
      //상태 업데이트
      chatAdapter.notifyDataSetChanged();
      Objects.requireNonNull(chatView.getLayoutManager()).scrollToPosition(messageList.size() - 1);
    }

  }

  // 다른 영역 클릭시 숨김
  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    View view = getCurrentFocus();

    float x = ev.getX();
    float y = ev.getY();
    int scrcoords[] = new int[2];

    menu.getLocationOnScreen(menuXY);
    tomenuXY = new int[]{menuXY[0] + menu.getWidth(), menuXY[1] + menu.getHeight()};

    if (view != null){

    }
    // 키보드 숨김
    if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
//    if ((ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
      view.getLocationOnScreen(scrcoords);
      float keyX = ev.getRawX() + view.getLeft() - scrcoords[0];
      float keyY = ev.getRawY() + view.getTop() - scrcoords[1];
      // btnSend가 눌렸다면 키보드 유지
      if (keyX > btnSend.getLeft() && keyX < btnSend.getRight() && keyY > btnSend.getTop() && keyY < btnSend.getBottom()){
        return btnSend.callOnClick();
      }
      if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
        ((InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
    }


    // 메뉴가 열려있을 때 다른 곳 누르면 숨김
    if (menu.getVisibility() == View.VISIBLE &&  (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE)){
      // 메뉴 버튼을 눌렀는지 확인
      if (!(x > img_menu.getLeft() && x < img_menu.getRight() && y > img_menu.getTop() && y < img_menu.getBottom())){
        // 메뉴버튼이 눌리지 않았다면 메뉴창의 내용이 눌린것인지 확인
        if (!(x > menuXY[0] && x < tomenuXY[0] && y > menuXY[1] && y < tomenuXY[1])){
          // 메뉴창의 공간에도 눌린 것이 없다면 메뉴 닫음
          return img_menu.callOnClick();
        }
      }
    }
    return super.dispatchTouchEvent(ev);
  }

  public void clearMessageList(){
    chatAdapter.clear();
    Objects.requireNonNull(chatView.getLayoutManager()).scrollToPosition(messageList.size());
  }
}
