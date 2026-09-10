package com.example.medilocker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class records extends AppCompatActivity {

    Button btn5;
    RecyclerView recyclerView;
    ArrayList<Record> records;
    RecordAdapter adapter;

    FirebaseFirestore db;
    FirebaseAuth auth;

    TextView btnback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        btn5=findViewById(R.id.btnaddrec);
        btnback = findViewById(R.id.btnback);

        recyclerView=findViewById(R.id.recordrev);

        records = new ArrayList<>();
        adapter= new RecordAdapter(records);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        loadRecords();

        btnback.setOnClickListener(view -> {
            finish();
        });

        btn5.setOnClickListener(view -> {
            Intent i = new Intent(records.this, uploadrecord.class);
            startActivity(i);
        });
    }

    private void loadRecords(){
        String uid = auth.getCurrentUser().getUid();
        db.collection("users").document(uid).collection("records").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    records.clear();
                    for(QueryDocumentSnapshot document: queryDocumentSnapshots){
                        Record record = document.toObject(Record.class);
                        records.add(record);
                    }
                    adapter.notifyDataSetChanged();
                });
    }

    protected void  onResume(){
        super.onResume();
        loadRecords();
    }
}