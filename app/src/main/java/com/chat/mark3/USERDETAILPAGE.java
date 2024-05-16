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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;

public class USERDETAILPAGE extends AppCompatActivity {
    CardView CV1;
    FirebaseDatabase database;
    FirebaseStorage storage;
    FirebaseAuth auth;
    FirebaseUser user;
    EditText USERNAMEBOX,PASSWORDBOX, CONFIRMBOX;
    TextView tv1,tv22;
    GoogleSignInClient client;
    String GNAME,GID,EMAIL,USERNAME,PASSWORD,CONFIRMPASSWORD,PROFILEIMG,STATUSNAME,STATUSONOFF,BIO,GPHOTO;
    DatabaseReference databaseReference;
    Intent INDB;
    ImageView img2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userdetailpage);
        USERNAMEBOX =findViewById(R.id.USERNAME_ENTRY_USERDETAILPAGE);
        PASSWORDBOX=findViewById(R.id.PASSWORD_ENTRY_USERDETAILPAGE);
        CONFIRMBOX =findViewById(R.id.CONFIRMPASSWORD_ENTRY_USERDETAILPAGE);
        CV1=findViewById(R.id.SUBMIT_CV_USERDETAILPAGE);
        tv1=findViewById(R.id.TITLE_TEXT_USERDETAILPAGE);
        img2=findViewById(R.id.imageView10);
        INDB=getIntent();
        auth= FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        user=auth.getCurrentUser();
        storage=FirebaseStorage.getInstance();
        getWindow().setNavigationBarColor(getColor(R.color.lightpurple));
        getWindow().setStatusBarColor(getColor(R.color.white));
        try {
            GNAME=INDB.getStringExtra("GNAME");
            EMAIL=INDB.getStringExtra("EMAIL");
            GPHOTO=user.getPhotoUrl().toString();
        }
        catch (Exception e)
        {
            Toast.makeText(this,"Error Occurs"+e.getMessage().toString(),Toast.LENGTH_LONG).show();
        }

        tv1.setText("Welcome "+GNAME);
        STATUSNAME=GNAME;
        BIO="Hello";
        STATUSONOFF="online";

        CV1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernamepattern="^@[a-z0-9]+[a-z]+[0-9]+$";
                USERNAME = USERNAMEBOX.getText().toString();
                PASSWORD=PASSWORDBOX.getText().toString();
                 CONFIRMPASSWORD= CONFIRMBOX.getText().toString();
                if ((TextUtils.isEmpty(USERNAME))) {
                    USERNAMEBOX.setError("Enter Username");
                } else if (TextUtils.isEmpty(PASSWORD)) {
                    PASSWORDBOX.setError("Enter Password");
                } else if (TextUtils.isEmpty(CONFIRMPASSWORD)) {
                    CONFIRMBOX.setError("Enter Confirm Passsword");
                } else if (!USERNAME.matches(usernamepattern)) {
                    USERNAMEBOX.setError("Enter Valid Username");
                } else if (USERNAME.length() < 6) {
                    USERNAMEBOX.setError("Enter More Than Six Character");
                } else if (PASSWORD.length()<6) {
                    PASSWORDBOX.setError("Enter More Than Six Character");
                } else if (!CONFIRMPASSWORD.matches(PASSWORD)) {
                    CONFIRMBOX.setError("Please Check Confirm Password ");
                } else if (TextUtils.isEmpty(GPHOTO)||TextUtils.isEmpty(STATUSNAME)||TextUtils.isEmpty(STATUSONOFF)||TextUtils.isEmpty(BIO)) {
                    Toast.makeText(USERDETAILPAGE.this,"Something Went Wrong",Toast.LENGTH_LONG).show();
                } else {
                    ProgressDialog progressDialog;
                    progressDialog=new ProgressDialog(USERDETAILPAGE.this);
                    progressDialog.setMessage("Saving.....");
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    FirebaseAuth.getInstance().getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                client.signOut();
                                auth.createUserWithEmailAndPassword(EMAIL, PASSWORD).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                                                @Override
                                                public void onComplete(@NonNull Task<String> task) {
                                                    if (task.isSuccessful()) {
                                                        String TOKEN = task.getResult();

                                                        databaseReference = database.getReference("USERLOGIN").child(auth.getUid());
                                                        GID = auth.getUid();
                                                      //  Toast.makeText(USERDETAILPAGE.this, "" + auth.getUid(), Toast.LENGTH_LONG).show();
                                                        DBLOGIN DB = new DBLOGIN(GID, EMAIL, PASSWORD, USERNAME, GPHOTO.toString(), STATUSNAME, STATUSONOFF, BIO, TOKEN);
                                                        databaseReference.setValue(DB).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isComplete())
                                                                {
                                                                    progressDialog.dismiss();
                                                                    Intent intent = new Intent(getApplicationContext(), FRIENDSCHAT.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }
                                                            }
                                                        });

                                                    }
                                                }

                                            });


                                        } else {
                                            Toast.makeText(USERDETAILPAGE.this, "" + task.getException().getMessage().toString(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
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

    @Override
    public void onBackPressed() {

        FirebaseAuth.getInstance().getCurrentUser().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
           client.signOut();
            }
        });
        super.onBackPressed();
    }

}

