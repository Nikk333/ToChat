package com.chat.mark3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class LOGINPAGE extends AppCompatActivity {
    FirebaseAuth auth;
    ImageView img;
    GoogleSignInClient client;
    TextView tv;
    CardView cvLOGIN;
    EditText EMAILBOX,PASSWORDBOX;
    TextView FORGET;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);
        img=findViewById(R.id.LOGINIMG);
        cvLOGIN=findViewById(R.id.LOGINCVLOGINPAGE);
        tv=findViewById(R.id.NEWTEXTLOGINPAGE);
        EMAILBOX=findViewById(R.id.EMAILENTRYLOGINPAGE);
        PASSWORDBOX=findViewById(R.id.PASSWORDENTRYLOGINPAGE);
        FORGET=findViewById(R.id.FORGET);
        auth=FirebaseAuth.getInstance();
        getWindow().setStatusBarColor(getColor(R.color.white));
        cvLOGIN.setOnClickListener(v -> {
            String EMAIL1 = EMAILBOX.getText().toString();
            String PASS1 = PASSWORDBOX.getText().toString();

            String emailvalid = "^[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

            if ((TextUtils.isEmpty(EMAIL1))) {
                EMAILBOX.setError("Enter Email");
            } else if (TextUtils.isEmpty(PASS1)) {
                PASSWORDBOX.setError("Enter Password");
            } else if (!EMAIL1.matches(emailvalid)) {
                EMAILBOX.setError("Enter Valid Email");
            } else if (PASS1.length() < 6) {
                PASSWORDBOX.setError("Enter More Than Six Character");
            } else {
                ProgressDialog progressDialog;
                progressDialog=new ProgressDialog(LOGINPAGE.this);
                progressDialog.setMessage("Loading Data...");
                progressDialog.show();
                progressDialog.setCancelable(false);
                auth.signInWithEmailAndPassword(EMAIL1, PASS1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                                @Override
                                public void onComplete(@NonNull Task<String> task) {
                                    if (task.isSuccessful()) {
                                        String TOKEN = task.getResult();
                                        FirebaseDatabase.getInstance().getReference("USERLOGIN").child(FirebaseAuth.getInstance().getUid()).child("notificationtoken").setValue(TOKEN).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isComplete())
                                                {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(LOGINPAGE.this, "Welcome back", Toast.LENGTH_LONG).show();
                                                    Intent in = new Intent(getApplicationContext(), HOME.class);
                                                    startActivity(in);
                                                    finish();
                                                }
                                            }
                                        });

                                    }
                                }
                            });


                        } else {
                            Toast.makeText(LOGINPAGE.this, "Please Enter Right Email and Password", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(getApplicationContext(),FIRSTPAGE.class);
                startActivity(in);
                finish();
            }
        });
        FORGET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(EMAILBOX.getText().toString().isEmpty())
                {
                    Toast.makeText(LOGINPAGE.this,"please fill email ",Toast.LENGTH_LONG).show();
                }
                else
                {
                    String EMAIL=EMAILBOX.getText().toString();
                    auth.sendPasswordResetEmail(EMAIL).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isComplete())
                            {
                                Toast.makeText(LOGINPAGE.this,"email sended",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

            }
        });

    createRequest();
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
    }
    private void createRequest() {
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        client= GoogleSignIn.getClient(this,signInOptions);
    }
}

