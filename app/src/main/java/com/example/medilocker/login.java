package com.example.medilocker;

import androidx.annotation.NonNull;
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
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class login extends AppCompatActivity {

    Button btn2, btngoogle;

    TextView tv1;

    EditText em, pass;

    FirebaseAuth auth;

    GoogleSignInClient googleSignInClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn2=findViewById(R.id.btnlogin);
        tv1=findViewById(R.id.tvsignup);
        em=findViewById(R.id.lemail);
        pass=findViewById(R.id.lpass);
        btngoogle=findViewById(R.id.btngoogle);

        auth=FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
        ).requestEmail().requestIdToken("469814325647-t2fi1j9b46736kf97deu4ufcvhfrdhqf.apps.googleusercontent.com").build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        if(auth.getCurrentUser()!=null){
            Intent x = new Intent(login.this, home.class);
            startActivity(x);
            finish();
        }


        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = em.getText().toString().trim();
                String password = pass.getText().toString().trim();

                if(email.isEmpty() || password.isEmpty()){
                    Toast.makeText(login.this, "Fill all details ", Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(login.this, "Successfully logged in ", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(login.this, home.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(login.this, signup.class);
                startActivity(i);
            }
        });

        btngoogle.setOnClickListener(new View.OnClickListener() {
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
    protected void onActivityResult(int requestcode, int resultcode, Intent datas){
        super.onActivityResult(requestcode, resultcode, datas);
        if(requestcode==100){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(datas);
            try{
                GoogleSignInAccount acc = task.getResult(ApiException.class);
                Toast.makeText(this, acc.getEmail(), Toast.LENGTH_SHORT).show();
                String idtoken = acc.getIdToken();
                AuthCredential cred = GoogleAuthProvider.getCredential(idtoken, null);
                auth.signInWithCredential(cred).addOnCompleteListener(task1 -> {
                    if(task1.isSuccessful()){
                        Toast.makeText(login.this, "Google login successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(login.this, home.class));
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

    };
}