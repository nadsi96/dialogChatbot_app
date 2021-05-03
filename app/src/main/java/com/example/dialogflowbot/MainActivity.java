package com.example.dialogflowbot;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dialogflowbot.adapters.ChatAdapter;
import com.example.dialogflowbot.adapters.DB_Handler;
import com.example.dialogflowbot.helpers.SendMessageInBg;
import com.example.dialogflowbot.interfaces.BotReply;
import com.example.dialogflowbot.menu.Option_Menu;
import com.example.dialogflowbot.menu.YesorNoAlert;
import com.example.dialogflowbot.models.Message;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
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

  //dialogFlow
  private SessionsClient sessionsClient;
  private SessionName sessionName;
  private String uuid = UUID.randomUUID().toString();//식별번호 생성
  private String TAG = "mainactivity";

  private DB_Handler db_handler;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    chatView = findViewById(R.id.chatView);
    editMessage = findViewById(R.id.editMessage);
    btnSend = findViewById(R.id.btnSend);

    chatAdapter = new ChatAdapter(messageList, this);
    chatView.setAdapter(chatAdapter);

    db_handler = DB_Handler.getInstance(this);

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

    setUpBot();
    set_Recorded_Chat();

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

  private void sendMessageToBot(String message) {
    QueryInput input = QueryInput.newBuilder()
        .setText(TextInput.newBuilder().setText(message).setLanguageCode("en-US")).build();
    new SendMessageInBg(this, sessionName, sessionsClient, input).execute();
  }

  // chatbot의 답변 추가
  @Override
  public void callback(DetectIntentResponse returnResponse) {
     if(returnResponse!=null) {
       String botReply = returnResponse.getQueryResult().getFulfillmentText();
       if(!botReply.isEmpty()){
         messageList.add(new Message(botReply, true));
         chatAdapter.notifyDataSetChanged();
         db_handler.insert_Chat(messageList.get(messageList.size()-1));
         Objects.requireNonNull(chatView.getLayoutManager()).scrollToPosition(messageList.size() - 1);
       }else {
         Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
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

  public boolean onCreateOptionsMenu(Menu menu){
    MenuInflater menuInflater = getMenuInflater();
    menuInflater.inflate(R.menu.option_menu, menu);
    Option_Menu option_menu = new Option_Menu(this);
    return true;
  }
  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    switch(item.getItemId()){
      case R.id.m_del_record:
        //채팅 기록 삭제
        YesorNoAlert alert_delete_record = new YesorNoAlert(MainActivity.this, "Delete Chat Record", "Are you sure to delete?");

        Toast.makeText(MainActivity.this, "delete record", Toast.LENGTH_SHORT).show();
        break;
      case R.id.m_setting:
        //설정화면
        Toast.makeText(MainActivity.this, "show settings", Toast.LENGTH_SHORT).show();
        break;
      case R.id.m_exit:
        //앱 종료
        YesorNoAlert alert_exit = new YesorNoAlert(MainActivity.this, "Exit", "Are you sure to exit?");
        Toast.makeText(MainActivity.this, "exit", Toast.LENGTH_SHORT).show();
        break;
    }
    return super.onOptionsItemSelected(item);
  }
}
