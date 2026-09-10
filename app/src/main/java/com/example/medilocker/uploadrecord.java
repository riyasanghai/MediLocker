package com.example.medilocker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class uploadrecord extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseFirestore db;

    EditText condition, title, doc, hosp, dates;
    Spinner category;

    Button btnupload, btnchoose;

    TextView btnback;

    Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadrecord);

        btnupload = findViewById(R.id.btnupload);
        btnchoose = findViewById(R.id.btnchoose);

        category = findViewById(R.id.category);
        condition = findViewById(R.id.condition);
        title = findViewById(R.id.title);
        doc = findViewById(R.id.doc);
        hosp = findViewById(R.id.hosp);
        dates = findViewById(R.id.dates);
        btnback = findViewById(R.id.btnback);

        btnback.setOnClickListener(view -> {
            finish();
        });


        dates.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            DatePickerDialog dialog = new DatePickerDialog(
                    uploadrecord.this,
                    (view, year, month, day) -> {
                        dates.setText(day + "/" + (month + 1) + "/" + year);
                    },
                    c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH)
            );

            dialog.show();
        });

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        HashMap<String, String> config = new HashMap<>();
        config.put("cloud_name", "dehrpfsx0");

        MediaManager.init(this, config);

        // Choose File
        btnchoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String[] options = {"Choose Photo", "Choose File"};

                new AlertDialog.Builder(uploadrecord.this).setTitle("Select Document").setItems(options, (dialog, which) -> {
                    if(which==0){
                        Intent photointent = new Intent(Intent.ACTION_PICK);
                        photointent.setType("image/*");
                        startActivityForResult(photointent, 101);
                    }
                    else{
                        Intent fileintent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        fileintent.setType("*/*");
                        fileintent.addCategory(Intent.CATEGORY_OPENABLE);
                        startActivityForResult(fileintent, 102);
                    }
                }).show();

            }
        });

        // Upload Record
        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String titles = title.getText().toString().trim();
                String doctor = doc.getText().toString().trim();
                String hospital = hosp.getText().toString().trim();
                String cond = condition.getText().toString().trim();
                String date = dates.getText().toString().trim();
                String cat = category.getSelectedItem().toString();

                if(fileUri == null){
                    Toast.makeText(uploadrecord.this, "Please select a document", Toast.LENGTH_SHORT).show();
                    return;
                }

                HashMap<String, Object> record = new HashMap<>();
                record.put("title", titles);
                record.put("doctor", doctor);
                record.put("hospital", hospital);
                record.put("condition", cond);
                record.put("date", date);
                record.put("category", cat);

                if (auth.getCurrentUser() == null) {
                    Toast.makeText(uploadrecord.this, "No user logged in", Toast.LENGTH_SHORT).show();
                    return;
                }

                uploadtoCloudinary(record);

            }
        });
    }

    private void uploadtoCloudinary(HashMap<String, Object> record){
        Toast.makeText(this, "Uploading file", Toast.LENGTH_SHORT).show();
        MediaManager.get()
                .upload(fileUri).unsigned("medilocker_upload")
                .callback(new UploadCallback() {
            @Override
            public void onStart(String requestId) {
            }

            @Override
            public void onProgress(String requestId, long bytes, long totalBytes) {
            }

            @Override
            public void onSuccess(String requestId, Map resultData) {
                String fileUrl = resultData.get("secure_url").toString();
                record.put("fileUrl",fileUrl);
                String uid = auth.getCurrentUser().getUid();

                db.collection("users")
                                .document(uid).collection("records")
                                .add(record)
                                .addOnSuccessListener(documentReference -> {
                                            Toast.makeText(uploadrecord.this, "File uploaded Successfully", Toast.LENGTH_SHORT).show();
                                            finish();

                                }).addOnFailureListener(e -> {
                            Toast.makeText(uploadrecord.this, "Record save failed", Toast.LENGTH_SHORT).show();
                        });
            }

            @Override
            public void onError(String requestId, ErrorInfo error) {
                Toast.makeText(uploadrecord.this, "Upload failed: " + error.getDescription(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onReschedule(String requestId, ErrorInfo error) {

            }
        }).dispatch();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == 101 || requestCode==102) && resultCode==RESULT_OK && data!= null){

            fileUri = data.getData();

            Toast.makeText(this,
                    "Document Selected",
                    Toast.LENGTH_SHORT).show();
        }
    }
}