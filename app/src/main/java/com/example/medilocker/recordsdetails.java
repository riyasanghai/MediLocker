package com.example.medilocker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class recordsdetails extends AppCompatActivity {

    TextView title, doctor, hospital, category, date, condition;

    Button btnview, btnshare;

    TextView btnback;

    String fileUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordsdetails);

        title=findViewById(R.id.vtitle);
        doctor=findViewById(R.id.vdrname);
        hospital=findViewById(R.id.vhospname);
        category=findViewById(R.id.vcategory);
        date=findViewById(R.id.vdate);
        condition=findViewById(R.id.vcond);
        btnshare=findViewById(R.id.btnshare);
        btnview=findViewById(R.id.btnview);
        btnback = findViewById(R.id.btnback);

        btnback.setOnClickListener(view -> {
            finish();
        });



        String titles = getIntent().getStringExtra("title");
        String doctors = getIntent().getStringExtra("doctor");
        String hospitals = getIntent().getStringExtra("hospital");
        String categorys = getIntent().getStringExtra("category");
        String dates = getIntent().getStringExtra("date");
        String conditions = getIntent().getStringExtra("condition");

        title.setText("Title : " + titles);
        category.setText("Category : " + categorys);
        doctor.setText("Doctor Name : " + doctors);
        hospital.setText("Hospital Name : " + hospitals);
        date.setText("Date : " + dates);
        condition.setText("Condition : " + conditions);
        fileUrl = getIntent().getStringExtra("fileUrl");

        btnview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fileUrl==null|| fileUrl.isEmpty()){
                    Toast.makeText(recordsdetails.this, "No file attached to this record", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(fileUrl));
                startActivity(intent);
            }
        });

        btnshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fileUrl==null|| fileUrl.isEmpty()){
                    Toast.makeText(recordsdetails.this, "No file to share", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");

                intent.putExtra(Intent.EXTRA_TEXT, "Medical Record:" + title.getText().toString() + "\n\n" + fileUrl);
                startActivity(Intent.createChooser(intent, "share record"));

            }
        });


    }
}