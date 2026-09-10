package com.example.medilocker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class chatbot extends AppCompatActivity {

    EditText question;
    Button btnsend;

    TextView btnback;

    TextView answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        question=findViewById(R.id.question);
        btnsend=findViewById(R.id.btnsend);
        answer=findViewById(R.id.tvans);
        btnback = findViewById(R.id.btnback);

        btnback.setOnClickListener(view -> {
            finish();
        });

        btnsend.setOnClickListener(view -> {
            String Question = question.getText().toString().trim();

            if(Question.isEmpty()){
                Toast.makeText(chatbot.this, "Enter a question", Toast.LENGTH_SHORT).show();
                return;
            }

            answer.setText("Thinking...");
            askGemini(Question);
        });
    }

    private void askGemini(String Question){
        OkHttpClient client = new OkHttpClient();
        try{
            //creating json
            JSONObject part = new JSONObject();
            part.put("text", Question);

            JSONArray parts = new JSONArray();
            parts.put(part);

            JSONObject content = new JSONObject();
            content.put("parts",parts);

            JSONArray contents = new JSONArray();
            contents.put(content);

            JSONObject json = new JSONObject();
            json.put("contents",contents);

            // creating request body
            RequestBody body = RequestBody.create(
                    json.toString(),
                    MediaType.get("application/json")
            );

            //create request
            Request request = new Request.Builder()
                    .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash-lite:generateContent")
                    .header("x-goog-api-key", BuildConfig.GEMINI_API_KEY)
                    .post(body).build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    runOnUiThread(() ->
                            answer.setText("error: " + e.getMessage()));
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String responsetext = response.body().string();
                    try {

                        JSONObject result = new JSONObject(responsetext);

                        String ans = result
                                .getJSONArray("candidates")
                                .getJSONObject(0)
                                .getJSONObject("content")
                                .getJSONArray("parts")
                                .getJSONObject(0)
                                .getString("text");

                        runOnUiThread(() ->
                                answer.setText(ans)
                        );

                    } catch (Exception e) {

                        runOnUiThread(() ->
                                answer.setText(responsetext)
                        );
                    }


                }
            });

        } catch (Exception e){
            answer.setText("something went wrong");
        }
    }
}