package com.example.medilocker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class signup extends AppCompatActivity {

    TextView tv2;

    Button btn3, btng;

    EditText name, email, pass, conpass;

    FirebaseAuth auth;

    GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        tv2 = findViewById(R.id.tvlogin);
        btn3=findViewById(R.id.btncreateacc);
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        pass=findViewById(R.id.password);
        conpass=findViewById(R.id.conpass);
        btng=findViewById(R.id.btngoog);

        auth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder
                (GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().
                requestIdToken("469814325647-t2fi1j9b46736kf97deu4ufcvhfrdhqf.apps.googleusercontent.com").build();

        googleSignInClient = GoogleSignIn.getClient(this,gso);

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String em = email.getText().toString().trim();
                String password = pass.getText().toString().trim();
                String conpassword = conpass.getText().toString().trim();

                if(em.isEmpty() || password.isEmpty() || conpassword.isEmpty() ){
                    Toast.makeText(signup.this, "Fill all Fields",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!password.equals(conpassword)){
                    Toast.makeText(signup.this, "Passwords dont match", Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.createUserWithEmailAndPassword(em , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(signup.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(signup.this, home.class);
                            startActivity(i);
                        }
                        else{
                            Toast.makeText(signup.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(signup.this, login.class);
                startActivity(intent);
            }
        });

        btng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent signin = googleSignInClient.getSignInIntent();
                        startActivityForResult(signin, 100);
                    }
                });

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount acc = task.getResult(ApiException.class);
                Toast.makeText(this, acc.getEmail(), Toast.LENGTH_SHORT).show();
                String idtoken = acc.getIdToken();
                AuthCredential cred = GoogleAuthProvider.getCredential(idtoken, null);

            auth.signInWithCredential(cred).addOnCompleteListener(task1 -> {
                if(task1.isSuccessful()){
                    Toast.makeText(signup.this, "Google login successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(signup.this, home.class));
                    finish();
                }
                else{
                    Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (ApiException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        }
    }
}