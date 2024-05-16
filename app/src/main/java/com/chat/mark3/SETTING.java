package com.chat.mark3;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SETTING extends AppCompatActivity {
    CircleImageView imageView;
    CardView SAVE_BTN;
    EditText EMAILBOX,NAMEBOX,USERNAMEBOX,BIOBOX;
    TextView USERNAMETEXT,STATUSNAMETEXT;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    String getEmail,getPassword,getUserName,getUid,getStatusName,getBio,getProfile,getActive,getNotificationToken;
    String setUname,setPassword,setBio,setProfile,setStatusName;
    Uri  setImageUri;
    LinearLayout PASSWORD_BTN,LOGOUT_BTN;
    GoogleSignInClient client;
    ImageView BACK;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        SAVE_BTN=findViewById(R.id.SAVE_BTN);
        imageView=findViewById(R.id.IMGSETTING);
        EMAILBOX=findViewById(R.id.EMAILBOX);
        NAMEBOX=findViewById(R.id.STATUSNAMEBOX);
        USERNAMEBOX=findViewById(R.id.USERNAMEBOX);
        STATUSNAMETEXT=findViewById(R.id.STATUSNAMETEXT);
        USERNAMETEXT=findViewById(R.id.USERNAMETEXT);
        BIOBOX=findViewById(R.id.BIOBOX);
        PASSWORD_BTN=findViewById(R.id.CHANGEPASSWORD_BTN);
        LOGOUT_BTN=findViewById(R.id.LOGOUT_BTN);
        BACK=findViewById(R.id.BACK_BTN);
        EMAILBOX.setText("hello");
        EMAILBOX.setEnabled(false);

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();
        getWindow().setNavigationBarColor(getColor(R.color.bottomnav));
        getWindow().setStatusBarColor(getColor(R.color.purple));

       DatabaseReference databaseReference=database.getReference().child("USERLOGIN").child(auth.getUid());
       databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    getEmail=snapshot.child("email").getValue(String.class);
                    getPassword=snapshot.child("password").getValue(String.class);
                    getUid=snapshot.child("uid").getValue(String.class);
                    getUserName=snapshot.child("username").getValue(String.class);
                    getProfile=snapshot.child("profileimg").getValue(String.class);
                    getStatusName=snapshot.child("statusname").getValue(String.class);
                    getActive=snapshot.child("statusonoff").getValue(String.class);
                    getBio=snapshot.child("bio").getValue(String.class);
                    getNotificationToken=snapshot.child("notificationtoken").getValue(String.class);
                    USERNAMETEXT.setText(""+getUserName);
                    STATUSNAMETEXT.setText(""+getStatusName);
                    EMAILBOX.setText(""+getEmail);
                    NAMEBOX.setText(""+getStatusName);
                    USERNAMEBOX.setText(""+getUserName);
                    BIOBOX.setText(""+getBio);
                    EMAILBOX.setTextColor(getColor(R.color.textcolor));
                    NAMEBOX.setTextColor(getColor(R.color.textcolor));
                USERNAMEBOX.setTextColor(getColor(R.color.textcolor));
                BIOBOX.setTextColor(getColor(R.color.textcolor));

                    Picasso.get().load(getProfile).into(imageView);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        BACK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Intent intent=new Intent(SETTING.this, FRIENDSCHAT.class);
             startActivity(intent);
             finish();
            }
        });
       imageView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               SAVE_BTN.setVisibility(View.VISIBLE);
               Intent intent=new Intent();
               intent.setType("image/*");
               intent.setAction(Intent.ACTION_GET_CONTENT);
               startActivityForResult(Intent.createChooser(intent,"Select Photo"),10);
           }
       });
       SAVE_BTN.setVisibility(View.GONE);
       NAMEBOX.setOnTouchListener(new View.OnTouchListener() {
           @Override
           public boolean onTouch(View v, MotionEvent event) {
               SAVE_BTN.setVisibility(View.VISIBLE);
               return false;
           }
       });
       BIOBOX.setOnTouchListener(new View.OnTouchListener() {
           @Override
           public boolean onTouch(View v, MotionEvent event) {
               SAVE_BTN.setVisibility(View.VISIBLE);
               return false;
           }
       });
       USERNAMEBOX.setOnTouchListener(new View.OnTouchListener() {
           @Override
           public boolean onTouch(View v, MotionEvent event) {
               SAVE_BTN.setVisibility(View.VISIBLE);
               return false;
           }
       });
      SAVE_BTN.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               setUname = USERNAMEBOX.getText().toString();
               setPassword = getPassword;
               setStatusName = NAMEBOX.getText().toString();
               setBio = BIOBOX.getText().toString();
               if (setImageUri != null) {
                   StorageReference storageReference=FirebaseStorage.getInstance().getReference().child("IMAGES").child(auth.getUid());
                   storageReference.putFile(setImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isComplete())
                            {
                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageuri=uri.toString();
                                        DBLOGIN dblogin = new DBLOGIN(getUid, getEmail, setPassword, setUname, imageuri, setStatusName, getActive, setBio,getNotificationToken);
                                        databaseReference.setValue(dblogin).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(SETTING.this, "Saved", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                       }
                   });

               }
               else {

                   DBLOGIN dblogin = new DBLOGIN(getUid, getEmail, setPassword, setUname, getProfile, setStatusName, getActive, setBio,getNotificationToken);
                   databaseReference.setValue(dblogin).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if (task.isSuccessful()) {

                               Toast.makeText(SETTING.this, "Saved", Toast.LENGTH_SHORT).show();
                           }
                       }
                   });
               }
           }

       });
      LOGOUT_BTN.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              final Dialog dialog=new Dialog(SETTING.this);
              dialog.setContentView(R.layout.dialog_layout);
              dialog.setCancelable(false);
              dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
              Button YES=dialog.findViewById(R.id.yesbtn);
              TextView textView=dialog.findViewById(R.id.notify);
              Button NO=dialog.findViewById(R.id.nobtn);
              ImageView imageView=dialog.findViewById(R.id.Dialog_Profile);
              Glide.with(SETTING.this).load(getProfile).circleCrop().into(imageView);
              textView.setText("Are You Sure You Want To LogOut?");
              dialog.show();
              YES.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                   FirebaseAuth.getInstance().signOut();
                        if(auth.getCurrentUser()==null) {
                            Intent intent = new Intent(SETTING.this, FIRSTPAGE.class);
                            startActivity(intent);
                            finish();
                        }


                  }
              });
              NO.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      dialog.dismiss();
                  }
              });


          }
      });
      PASSWORD_BTN.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              final Dialog dialog=new Dialog(SETTING.this);
              dialog.setContentView(R.layout.dialog_layout);
              dialog.setCancelable(false);
              dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
              Button YES=dialog.findViewById(R.id.yesbtn);
              TextView textView=dialog.findViewById(R.id.notify);
              textView.setText("Are you sure want to change password");
              Button NO=dialog.findViewById(R.id.nobtn);
              ImageView imageView=dialog.findViewById(R.id.Dialog_Profile);
              Glide.with(SETTING.this).load(getProfile).circleCrop().into(imageView);
              textView.setText("Are You Sure You Want To LogOut?");
              dialog.show();
              YES.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      String EMAIL=EMAILBOX.getText().toString();
                      auth.sendPasswordResetEmail(EMAIL).addOnCompleteListener(new OnCompleteListener<Void>() {
                          @Override
                          public void onComplete(@NonNull Task<Void> task) {
                              if(task.isComplete())
                              {
                                  Toast.makeText(SETTING.this,"Email sended successfully",Toast.LENGTH_LONG).show();
                                  dialog.dismiss();
                              }
                              else
                              {

                                  Toast.makeText(SETTING.this,task.getException()+"",Toast.LENGTH_LONG).show();
                              }
                          }
                      });

                  }
              });
              NO.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      dialog.dismiss();
                  }
              });

          }
      });
      createRequest();
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(SETTING.this,FRIENDSCHAT.class);
        startActivity(intent);
        super.onBackPressed();
    }

    private void createRequest() {
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        client= GoogleSignIn.getClient(this,signInOptions);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10)
        {
            if(data!=null)
            {
                setImageUri=data.getData();
                imageView.setImageURI(setImageUri);
            }
        }
    }
    @Override
    protected void onStart() {
        USERACTIVE.ONOFF_FUN("Online");
        super.onStart();
    }
    @Override
    protected void onPause() {
        USERACTIVE.ONOFF_FUN("Ofline");
        super.onPause();
    }
}