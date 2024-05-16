package com.chat.mark3;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
public class FIRSTPAGE extends AppCompatActivity {
    CardView cv_FIRSTPAGE;
    TextView tv1_FIRSTPAGE,tv;
    FirebaseAuth auth;
    GoogleSignInClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_firstpage);
        cv_FIRSTPAGE=findViewById(R.id.GOOGLECVFIRSTPAGE);
        tv1_FIRSTPAGE=findViewById(R.id.LOGINTXTFIRSTPAGE);
        tv=findViewById(R.id.LETSTEXTFIRSTPAGE);
        /////////////////////////////////////////////////////////////////
        auth=FirebaseAuth.getInstance();
        getWindow().setNavigationBarColor(getColor(R.color.lightpurple));
        getWindow().setStatusBarColor(getColor(R.color.white));
        cv_FIRSTPAGE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LOGINFIRSTPAGE();
            }
                private void LOGINFIRSTPAGE() {
                    Intent intent=client.getSignInIntent();
                    activityResultLauncher.launch(intent);
                }
                ActivityResultLauncher<Intent> activityResultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result ->{
                    if (result.getResultCode()==Activity.RESULT_OK)
                    {
                        Intent data=result.getData();
                        Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
                        try {
                            GoogleSignInAccount account=task.getResult(ApiException.class);
                            AUTHOFIRSTPAGE(account.getIdToken());
                        }
                        catch (Exception e)
                        {
                            throw new RuntimeException(e);
                        }
                    }
                    else {
                        tv.setText(""+result.getResultCode());
                    }
                });
                private void AUTHOFIRSTPAGE(String Token) {
                    AuthCredential credential= GoogleAuthProvider.getCredential(Token,null);
                    Task<AuthResult> task1 = auth.signInWithCredential(credential)
                            .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task1) {

                                    if (task1.isSuccessful()) {
                                        Log.d(TAG, "signInWithCredential:success");
                                        FirebaseUser user = auth.getCurrentUser();
                                        Toast.makeText(FIRSTPAGE.this,"Let's GO",Toast.LENGTH_LONG).show();
                                        Intent in=new Intent(getApplicationContext(),USERDETAILPAGE.class);
                                        in.putExtra("GNAME",user.getDisplayName());
                                        in.putExtra("EMAIL",user.getEmail());
                                        in.putExtra("GID",user.getUid());
                                        startActivity(in);
                                        finish();

                                    }
                                    else {
                                        Toast.makeText(FIRSTPAGE.this, "sorry", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }
        });

        tv1_FIRSTPAGE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(getApplicationContext(), LOGINPAGE.class);
                startActivity(in);
                finish();
            }
        });
    CREATEREQUESTFIRSTPAGE();
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
    }
    private void CREATEREQUESTFIRSTPAGE() {
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        client= GoogleSignIn.getClient(this,signInOptions);
    }
}