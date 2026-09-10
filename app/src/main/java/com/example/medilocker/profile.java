package com.example.medilocker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class profile extends AppCompatActivity {

    Button btnlogout;

    TextView btnback, tvname, tvemail;

    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        btnlogout=findViewById(R.id.btnlogout);
        btnback = findViewById(R.id.btnback);
        auth = FirebaseAuth.getInstance();

        tvname = findViewById(R.id.tvname);
        tvemail = findViewById(R.id.tvemail);

        btnback.setOnClickListener(view -> {
            finish();
        });

        if (auth.getCurrentUser() != null) {

            String name = auth.getCurrentUser().getDisplayName();
            String email = auth.getCurrentUser().getEmail();

            if (name == null || name.isEmpty()) {
                name = "MediLocker User";
            }

            tvname.setText("Name : " + name);
            tvemail.setText("Email : " + email);
        }

        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(profile.this, login.class);
                startActivity(i);
                finish();
            }
        });
    }
}