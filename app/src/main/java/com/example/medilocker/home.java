package com.example.medilocker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class home extends AppCompatActivity {

    Button btn4;

    Button btnprofile, btnrecords, btnai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btn4=findViewById(R.id.btnaddrec);

        btnai=findViewById(R.id.btnassistant);
        btnprofile=findViewById(R.id.btnprofile);
        btnrecords=findViewById(R.id.btnrecords);

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(home.this, uploadrecord.class);
                startActivity(i);
            }
        });

        btnrecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent x = new Intent(home.this, records.class);
                startActivity(x);
            }
        });

        btnprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent y = new Intent(home.this, profile.class);
                startActivity(y);
            }
        });

        btnai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent z = new Intent(home.this, chatbot.class);
                startActivity(z);
            }
        });
    }
}