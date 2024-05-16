package com.chat.mark3;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.zegocloud.uikit.prebuilt.call.invite.widget.ZegoSendCallInvitationButton;
import com.zegocloud.uikit.service.defines.ZegoUIKitUser;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CHATWINDOW extends AppCompatActivity {
    RelativeLayout relativeLayout;
    Handler h = new Handler();
    int progress = 0, i = 0, UIMODE;
    FirebaseUser user;
    String personid, personname, senderuid, getStatusname, getStatusActive, getProfileimg, senderRoom, reciverRoom, SETIMAGEURI, ID;
    CardView SENDBTN, TEXT_CARD, ADD_IMG, EDIT;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference databaseReference1;
    TextView PERSONNAME, PERSONACTIVE, SPECIAL_PERMISSION, EDIT_USER, VIEW_PROFILE, CLEAR_MY_CHAT2, CLEAR_EVERYONE_CHAT;
    EditText message;
    RecyclerView MESSAGE_RECYCLE;
    ArrayList<DBMESSAGE> DBMESSAGEArrayList;
    messagesAdapter messagesAdapter;
    CircleImageView PERSON_PROFILE, IMAGEPICKER, ONLINE_IMG;
    ZegoSendCallInvitationButton BTNVOICECALL, BTNVIDEOCALL;
    Uri imageURI;
    String MESSAGE, NOTIFICATIONNAME;
    LinearLayout toplayout, bottomlayout;
    ImageView BACK, SEND_IMG, DOTS;
    MediaPlayer mediaPlayer;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatwindow);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        user = auth.getCurrentUser();
        IMAGEPICKER = findViewById(R.id.CHATWINDOW_IMAGEPICKER);
        SENDBTN = findViewById(R.id.SENDBTN);
        message = findViewById(R.id.MESSAGEBOX);
        PERSONNAME = findViewById(R.id.personname);
        PERSONACTIVE = findViewById(R.id.personactive);
        PERSON_PROFILE = findViewById(R.id.CHATWINDOW_PROFILE);
        MESSAGE_RECYCLE = findViewById(R.id.msgadpter);
        BTNVOICECALL = findViewById(R.id.CHATWINDOW_VOICECALL);
        BTNVIDEOCALL = findViewById(R.id.CHATWINDOW_VIDEOCALL);
        BACK = findViewById(R.id.CHATWINDOW_BACK);
        DOTS = findViewById(R.id.CHATWINDOW_DOTS);
        EDIT = findViewById(R.id.CHATWINDOW_EDIT);
        EDIT_USER = findViewById(R.id.EDIT_USER);
        VIEW_PROFILE = findViewById(R.id.VIEW_PROFILE);
        CLEAR_MY_CHAT2 = findViewById(R.id.CLEAR_CHAT2);
        SPECIAL_PERMISSION = findViewById(R.id.SPECIAL_PERMISSION);
        CLEAR_MY_CHAT2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CHATWINDOW.this, "dfdsfsdfsf", Toast.LENGTH_SHORT).show();
            }
        });
        CLEAR_EVERYONE_CHAT = findViewById(R.id.CELAR_EVERYONE_CHAT);
        ONLINE_IMG = findViewById(R.id.CHATWINDOW_ONLINE);
        mediaPlayer = MediaPlayer.create(this, R.raw.dot);
        personid = getIntent().getStringExtra("UID");

        /************************************************************[PROFILE ANIMATION->]*************************************/
        PERSON_PROFILE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
              /*  final Dialog dialog=new Dialog(CHATWINDOW.this);
                dialog.setContentView(R.layout.dailog_profile);
                dialog.setCancelable(true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                ImageView imageView=dialog.findViewById(R.id.dialog_profilepic);
                Glide.with(CHATWINDOW.this).load(PERSON_PROFILE.getDrawable()).circleCrop().into(imageView);
                dialog.show();*/
            }
        });

        /************************************************************[<-PROFILE ANIMATION]*************************************/


        /*********************************[DARK LIGHT MODE->]*****************************************/
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            UIMODE = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            if (UIMODE == Configuration.UI_MODE_NIGHT_YES) {
                getWindow().setNavigationBarColor(getColor(R.color.blacklight));
                getWindow().setStatusBarColor(getColor(R.color.black));
            } else {
                toplayout = findViewById(R.id.TOPLAYOUT);
                bottomlayout = findViewById(R.id.BOTTOMLAYOUT);
                relativeLayout = findViewById(R.id.chatwindow);
                TEXT_CARD = findViewById(R.id.TEXT_CARD);
                ADD_IMG = findViewById(R.id.CHATWINDOW_IMAGEPICKER_CARDVIEW);
                SEND_IMG = findViewById(R.id.SEND_IMG);
                toplayout.setBackgroundColor(getColor(R.color.white));
                SEND_IMG.setImageResource(R.drawable.send);
                bottomlayout.setBackgroundColor(getColor(R.color.whitelight));
                // BTNVIDEOCALL.setForeground(getDrawable(R.drawable.videocallfull));
                //BTNVOICECALL.setForeground(getDrawable(R.drawable.voicecallfull));
                DOTS.setImageResource(R.drawable.dots);
                BACK.setImageResource(R.drawable.backk);
                relativeLayout.setBackgroundColor(getColor(R.color.white));
                PERSONNAME.setTextColor(getColor(R.color.black));
                PERSONACTIVE.setTextColor(getColor(R.color.black));
                MESSAGE_RECYCLE.setBackgroundColor(getColor(R.color.whitelight));
                TEXT_CARD.setCardBackgroundColor(getColor(R.color.white));
                TEXT_CARD.setOutlineSpotShadowColor(getColor(R.color.white));
                TEXT_CARD.setOutlineAmbientShadowColor(getColor(R.color.white));
                SENDBTN.setCardBackgroundColor(getColor(R.color.white));
                SENDBTN.setOutlineSpotShadowColor(getColor(R.color.white));
                SENDBTN.setOutlineAmbientShadowColor(getColor(R.color.white));
                message.setTextColor(getColor(R.color.black));
                ONLINE_IMG.setBorderColor(getColor(R.color.white));
                message.setHintTextColor(getColor(R.color.gray));
                ADD_IMG.setCardBackgroundColor(getColor(R.color.white));
                ADD_IMG.setOutlineAmbientShadowColor(getColor(R.color.white));
                ADD_IMG.setOutlineSpotShadowColor(getColor(R.color.white));
                EDIT.setCardBackgroundColor(getColor(R.color.white));
                EDIT.setOutlineSpotShadowColor(getColor(R.color.white));
                EDIT.setOutlineAmbientShadowColor(getColor(R.color.white));
                EDIT_USER.setTextColor(getColor(R.color.black));
                VIEW_PROFILE.setTextColor(getColor(R.color.black));
                CLEAR_MY_CHAT2.setTextColor(getColor(R.color.black));
                CLEAR_EVERYONE_CHAT.setTextColor(getColor(R.color.black));
                SPECIAL_PERMISSION.setTextColor(getColor(R.color.black));
                Glide.with(CHATWINDOW.this).load(R.drawable.addimg).into(IMAGEPICKER);
                getWindow().setNavigationBarColor(getColor(R.color.whitelight));
                getWindow().setStatusBarColor(getColor(R.color.white));

            }
        }
        getUserActivity();

        /*******************************************[<-DARK LIGHT MODE]**************************************************/


        /************************************************************[SPECIAL FRIEND || VIEW PROFILE->]*************************************/
        FirebaseDatabase.getInstance().getReference("USERLOGIN").child(personid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                personname = snapshot.child("username").getValue(String.class);
                getProfileimg = snapshot.child("profileimg").getValue(String.class);
                getStatusActive = snapshot.child("statusonoff").getValue(String.class);
                getStatusname = snapshot.child("statusname").getValue(String.class);
                ID = snapshot.child("uid").getValue(String.class);

                FirebaseDatabase.getInstance().getReference("FRIENDS").child(auth.getUid()).child("fid").child(personid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String SPECIALPERMISSION = snapshot.child("specialfriend").getValue(String.class);
                        if (SPECIALPERMISSION.equals("Yes")) {
                            getProfileimg = snapshot.child("fphoto").getValue(String.class);
                            getStatusname = snapshot.child("fstatusname").getValue(String.class);
                        }
                        PERSONACTIVE.setText("" + getStatusActive);
                        PERSONNAME.setText("" + getStatusname);
                        Picasso.get().load(getProfileimg).into(PERSON_PROFILE);
                        call(ID, getStatusname);
                        if (getStatusActive.equals("Online")) {
                            ONLINE_IMG.setVisibility(View.VISIBLE);
                        } else {
                            ONLINE_IMG.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseDatabase.getInstance().getReference("FRIENDS").child(personid).child("fid").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("specialfriend").getValue(String.class).equals("Yes")) {
                    SPECIAL_PERMISSION.setText("Remove Best Friend");
                } else {
                    SPECIAL_PERMISSION.setText("Make Best Friend");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseDatabase.getInstance().getReference("FRIENDS").child(auth.getUid()).child("fid").child(personid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("specialfriend").getValue(String.class).equals("Yes")) {
                    EDIT_USER.setText("Edit Profile");
                    EDIT_USER.setEnabled(true);
                } else {
                    EDIT_USER.setClickable(false);
                    EDIT_USER.setTextColor(getColor(R.color.whitehighlight));
                    EDIT_USER.setText("Can't Edit Profile");
                    EDIT_USER.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        EDIT_USER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CHATWINDOW.this, VIEWPROFILEPAGE.class);
                intent.putExtra("UID", personid);
                startActivity(intent);
            }
        });
        VIEW_PROFILE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CHATWINDOW.this, VIEWPROFILEPAGE.class);
                intent.putExtra("UID", personid);
                startActivity(intent);

            }
        });

        /************************************************************[<-SPECIAL FRIEND || VIEW PROFILE]*************************************/


        /***************************************************[EDIT WORK->]*************************************************/

        CLEAR_MY_CHAT2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("chats").child(senderRoom).child("message").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()) {
                            Toast.makeText(CHATWINDOW.this, "Deleted", Toast.LENGTH_SHORT).show();
                            HashMap<String, Object> lastMsg = new HashMap<>();
                            lastMsg.put("lastmsg", "Tap to chat");
                            lastMsg.put("lastmsgTime", " ");
                            database.getReference().child("chats").child(senderRoom).updateChildren(lastMsg);
                        }
                    }
                });
            }
        });
        CLEAR_EVERYONE_CHAT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase.getInstance().getReference().child("chats").child(reciverRoom).child("message").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()) {
                            FirebaseDatabase.getInstance().getReference().child("chats").child(senderRoom).child("message").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isComplete()) {
                                        Toast.makeText(CHATWINDOW.this, "Deleted From Everyone", Toast.LENGTH_SHORT).show();
                                        Toast.makeText(CHATWINDOW.this, "Deleted", Toast.LENGTH_SHORT).show();
                                        HashMap<String, Object> lastMsg = new HashMap<>();
                                        lastMsg.put("lastmsg", "Tap to chat");
                                        lastMsg.put("lastmsgTime", " ");
                                        database.getReference().child("chats").child(senderRoom).updateChildren(lastMsg);
                                        database.getReference().child("chats").child(reciverRoom).updateChildren(lastMsg);

                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
        SPECIAL_PERMISSION.setOnClickListener(v -> FirebaseDatabase.getInstance().getReference("FRIENDS").child(personid).child("fid").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String SPECIALPERMISSION = snapshot.child("specialfriend").getValue(String.class);
                final Dialog dialog = new Dialog(CHATWINDOW.this);
                dialog.setContentView(R.layout.dialog_layout);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Button YES = dialog.findViewById(R.id.yesbtn);
                TextView textView = dialog.findViewById(R.id.notify);
                Button NO = dialog.findViewById(R.id.nobtn);
                ImageView imageView = dialog.findViewById(R.id.Dialog_Profile);
                Glide.with(CHATWINDOW.this).load(PERSON_PROFILE.getDrawable()).circleCrop().into(imageView);

                if (SPECIALPERMISSION.equals("Yes")) {
                } else {
                    textView.setText("You Want To Add This User As A Best Friend After This He/She Can Edit Your Profile");
                }
                dialog.show();
                YES.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SPECIALPERMISSION.equals("Yes")) {

                            FirebaseDatabase.getInstance().getReference("USERLOGIN").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String CURRENT_NAME, CURRENT_PIC, CURRENT_UNAME;
                                    DBLOGIN dblogin = snapshot.getValue(DBLOGIN.class);
                                    CURRENT_NAME = dblogin.getSTATUSNAME();
                                    CURRENT_PIC = dblogin.getPROFILEIMG();
                                    CURRENT_UNAME = dblogin.getUSERNAME();


                                    DatabaseReference databaseReference = database.getReference("FRIENDS").child(personid).child("fid").child(auth.getUid());
                                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String MUTE = snapshot.child("mute").getValue(String.class);
                                            DBFRIENDS dbfriends = new DBFRIENDS(snapshot.child("fid").getValue(String.class), CURRENT_UNAME, CURRENT_PIC, "No", CURRENT_NAME, MUTE);
                                            databaseReference.setValue(dbfriends).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isComplete()) {
                                                        SPECIAL_PERMISSION.setText("Make Best Friend");
                                                        dialog.dismiss();
                                                    }
                                                }
                                            });
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        } else {
                            FirebaseDatabase.getInstance().getReference("FRIENDS").child(personid).child("fid").child(auth.getUid()).child("specialfriend").setValue("Yes").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    SPECIAL_PERMISSION.setText("Remove Best Friend");
                                    dialog.dismiss();
                                }
                            });

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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }));
        EDIT.setVisibility(View.GONE);
        EDIT.setClickable(true);
        DOTS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EDIT.getVisibility() == View.VISIBLE) {
                    EDIT.setVisibility(View.GONE);
                } else {
                    EDIT.setVisibility(View.VISIBLE);
                }
            }
        });
        findViewById(R.id.chatwindow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EDIT.getVisibility() == View.VISIBLE) {
                    EDIT.setVisibility(View.GONE);
                }
            }
        });

        /****************************************************[<-EDIT WORK]**************************************************/


        //Toast.makeText(CHATWINDOW.this,""+getStatusname,Toast.LENGTH_LONG).show();
        senderuid = auth.getUid();
        senderRoom = senderuid + personid;
        reciverRoom = personid + senderuid;
        DBMESSAGEArrayList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        MESSAGE_RECYCLE.setLayoutManager(linearLayoutManager);
        messagesAdapter = new messagesAdapter(CHATWINDOW.this, DBMESSAGEArrayList, senderRoom, reciverRoom, personid);
        MESSAGE_RECYCLE.setAdapter(messagesAdapter);
        databaseReference1 = database.getReference().child("chats").child(senderRoom).child("message");
        messagelist();


        /*****************************************[SEND FUNCTION->]******************************************/
        SENDBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UIMODE == Configuration.UI_MODE_NIGHT_YES) {
                    Glide.with(CHATWINDOW.this).load(R.drawable.addimgdark).into(IMAGEPICKER);
                } else {
                    Glide.with(CHATWINDOW.this).load(R.drawable.addimg).into(IMAGEPICKER);
                }
                MESSAGE = message.getText().toString();
                Date DatecurrentTime = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
                String currnetTime = simpleDateFormat.format(DatecurrentTime);
                String uniquekey = database.getReference().push().getKey();
                if (MESSAGE.isEmpty()) {
                    if (imageURI != null) {
                        FirebaseStorage.getInstance().getReference().child("CHATIMAGES").child(senderRoom + reciverRoom).child(uniquekey).putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                imageURI = null;
                                FirebaseStorage.getInstance().getReference().child("CHATIMAGES").child(senderRoom + reciverRoom).child(uniquekey).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        MESSAGE = "nullmessage";
                                        SETIMAGEURI = uri.toString();
                                        DBMESSAGE DBMESSAGE = new DBMESSAGE(MESSAGE, SETIMAGEURI, senderuid, currnetTime, DatecurrentTime.getTime());
                                        HashMap<String, Object> lastMsg = new HashMap<>();
                                        lastMsg.put("lastmsg", "\uD83C\uDFDE️ Photo");
                                        lastMsg.put("lastmsgTime", currnetTime);
                                        database.getReference().child("chats").child(senderRoom).updateChildren(lastMsg);
                                        database.getReference().child("chats").child(reciverRoom).updateChildren(lastMsg);
                                        database.getReference().child("chats").child(senderRoom).child("message").child(uniquekey).setValue(DBMESSAGE).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                database.getReference().child("chats").child(reciverRoom).child("message").child(uniquekey)
                                                        .setValue(DBMESSAGE).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (mediaPlayer != null) {
                                                                    mediaPlayer.start();
                                                                }
                                                                sendNotification("\uD83C\uDFDE Photo");
                                                            }
                                                        });
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    } else {
                        Toast.makeText(CHATWINDOW.this, "Please Write Something", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    message.setText("");
                    if (imageURI != null) {
                        FirebaseStorage.getInstance().getReference().child("CHATIMAGES").child(senderRoom + reciverRoom).child(uniquekey).putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                imageURI = null;
                                FirebaseStorage.getInstance().getReference().child("CHATIMAGES").child(senderRoom + reciverRoom).child(uniquekey).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        SETIMAGEURI = uri.toString();
                                        DBMESSAGE DBMESSAGE = new DBMESSAGE(MESSAGE, SETIMAGEURI, senderuid, currnetTime, DatecurrentTime.getTime());
                                        HashMap<String, Object> lastMsg = new HashMap<>();
                                        lastMsg.put("lastmsg", "\uD83C\uDFDE Photo");
                                        lastMsg.put("lastmsgTime", currnetTime);
                                        database.getReference().child("chats").child(senderRoom).updateChildren(lastMsg);
                                        database.getReference().child("chats").child(reciverRoom).updateChildren(lastMsg);

                                        database.getReference().child("chats").child(senderRoom).child("message").child(uniquekey).setValue(DBMESSAGE).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                database.getReference().child("chats").child(reciverRoom).child("message").child(uniquekey)
                                                        .setValue(DBMESSAGE).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (mediaPlayer != null) {
                                                                    mediaPlayer.start();
                                                                }
                                                                sendNotification(DBMESSAGE.getMessage() + "\uD83C\uDFDE Photo");
                                                            }
                                                        });
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    } else {
                        SETIMAGEURI = "nullimage";
                        DBMESSAGE DBMESSAGE = new DBMESSAGE(MESSAGE, SETIMAGEURI, senderuid, currnetTime, DatecurrentTime.getTime());
                        HashMap<String, Object> lastMsg = new HashMap<>();
                        lastMsg.put("lastmsg", DBMESSAGE.getMessage());
                        lastMsg.put("lastmsgTime", currnetTime);
                        database.getReference().child("chats").child(senderRoom).updateChildren(lastMsg);
                        database.getReference().child("chats").child(reciverRoom).updateChildren(lastMsg);
                        database.getReference().child("chats").child(senderRoom).child("message").child(uniquekey).setValue(DBMESSAGE).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                database.getReference().child("chats").child(reciverRoom).child("message").child(uniquekey)
                                        .setValue(DBMESSAGE).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (mediaPlayer != null) {
                                                    mediaPlayer.start();
                                                }
                                                sendNotification(DBMESSAGE.getMessage().toString());

                                            }
                                        });
                            }
                        });
                    }
                }
            }
        });

        /*****************************************[<-SEND FUNCTION]******************************************/
        Keyboard();
        IMAGEPICKER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Photo"), 10);
            }
        });
        BACK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /*************************************************************[FUNCTIONS]**********************************************************************************/
    @Override
    public void onBackPressed() {
        HashMap<String, Object> onin = new HashMap<>();
        onin.put("onin", "online");
        database.getReference().child("chats").child(reciverRoom).updateChildren(onin);
        Intent intent = new Intent(getApplicationContext(), FRIENDSCHAT.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    int a = 0;

    private void messagelist() {
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DBMESSAGEArrayList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DBMESSAGE DBMESSAGE = dataSnapshot.getValue(DBMESSAGE.class);
                    DBMESSAGE.setMessageid(dataSnapshot.getKey());
                    DBMESSAGEArrayList.add(DBMESSAGE);
                    a = ((int) snapshot.getChildrenCount());
                }
                //Toast.makeText(CHATWINDOW.this,""+a,Toast.LENGTH_LONG).show();
                messagesAdapter.notifyDataSetChanged();
                if (mediaPlayer != null) {
                    mediaPlayer.start();
                }
                //scrollToBottom(a);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void call(String id, String name) {
        try {
            String targetUserID = id;
            String targetUserName = name;
            BTNVOICECALL.setIsVideoCall(false);
            BTNVOICECALL.setResourceID("zego_uikit_call");
            BTNVOICECALL.setInvitees(Collections.singletonList(new ZegoUIKitUser(targetUserID, targetUserName)));
            BTNVIDEOCALL.setIsVideoCall(true);
            BTNVIDEOCALL.setResourceID("zego_uikit_call");
            BTNVIDEOCALL.setInvitees(Collections.singletonList(new ZegoUIKitUser(targetUserID, targetUserName)));
        } catch (Exception e) {
            PERSONACTIVE.setText("" + e);
        }

    }

    private void Keyboard() {
        final View rootview = findViewById(android.R.id.content);
        rootview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                rootview.getWindowVisibleDisplayFrame(r);
                int screenhight = rootview.getRootView().getHeight();
                int keypadehight = screenhight - r.bottom;
                HashMap<String, Object> onin = new HashMap<>();
                if (keypadehight > 200) {
                    scrollToBottom(a);
                    onin.put("onin", "Typing...");
                } else {
                    scrollToBottom(a);
                    onin.put("onin", "in the chat");

                }
                database.getReference().child("chats").child(reciverRoom).updateChildren(onin);
            }
        });
    }

    private void getUserActivity() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progress < 100) {
                    progress++;
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            if (progress == 100) {
                                FirebaseDatabase.getInstance().getReference().child("chats").child(senderRoom).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            String onin1 = snapshot.child("onin").getValue(String.class);
                                            if (onin1 != null) {
                                                PERSONACTIVE.setText(onin1);
                                            } else {
                                                PERSONACTIVE.setText(personname);
                                            }
                                        } else {
                                            PERSONACTIVE.setText("ofline");

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                                progress = 0;
                                i++;
                            }
                        }
                    });
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (data != null) {
                imageURI = data.getData();
                IMAGEPICKER.setImageURI(imageURI);
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
        HashMap<String, Object> onin = new HashMap<>();
        onin.put("onin", "ofline");
        database.getReference().child("chats").child(reciverRoom).updateChildren(onin);

        super.onPause();
    }

    private void scrollToBottom(int x) {
        MESSAGE_RECYCLE.post(new Runnable() {
            @Override
            public void run() {
                MESSAGE_RECYCLE.smoothScrollToPosition(x + 2);
            }
        });
    }

    void sendNotification(String message) {
        FirebaseDatabase.getInstance().getReference("USERLOGIN").child(personid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DBLOGIN usertoken = snapshot.getValue(DBLOGIN.class);
                FirebaseDatabase.getInstance().getReference("FRIENDS").child(personid).child("fid").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        DBFRIENDS dbfriends = snapshot.getValue(DBFRIENDS.class);
                        if (dbfriends.getMute().equals("No")) {

                            if (snapshot.child("specialfriend").getValue(String.class).equals("Yes")) {
                                NOTIFICATIONNAME = snapshot.child("fstatusname").getValue(String.class);
                                try {
                                    JSONObject jsonObject = new JSONObject();
                                    JSONObject notificationobj = new JSONObject();
                                    notificationobj.put("title", NOTIFICATIONNAME);
                                    notificationobj.put("body", message);
                                    JSONObject dataobj = new JSONObject();
                                    dataobj.put("userId", personid);
                                    jsonObject.put("notification", notificationobj);
                                    jsonObject.put("data", dataobj);
                                    jsonObject.put("to", usertoken.getNOTIFICATIONTOKEN());
                                    callApi(jsonObject);

                                } catch (Exception r) {

                                }
                            } else {
                                FirebaseDatabase.getInstance().getReference("USERLOGIN").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        try {
                                            DBLOGIN dblogin = snapshot.getValue(DBLOGIN.class);
                                            NOTIFICATIONNAME = dblogin.getSTATUSNAME();
                                            JSONObject jsonObject = new JSONObject();
                                            JSONObject notificationobj = new JSONObject();
                                            notificationobj.put("title", NOTIFICATIONNAME);
                                            notificationobj.put("body", message);
                                            JSONObject dataobj = new JSONObject();
                                            dataobj.put("userId", personid);
                                            jsonObject.put("notification", notificationobj);
                                            jsonObject.put("data", dataobj);
                                            jsonObject.put("to", usertoken.getNOTIFICATIONTOKEN());
                                            callApi(jsonObject);
                                        } catch (Exception e) {

                                        }


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void callApi(JSONObject jsonObject) {
        MediaType JSON = MediaType.get("application/json");
        OkHttpClient client = new OkHttpClient();
        String url = "https://fcm.googleapis.com/fcm/send";
        RequestBody body = RequestBody.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Authorization", "Bearer AAAAogMUOjc:APA91bH7prNMYCcPWS2ziMEVo4wzZFQLl1D9R6oD_u4tQx2IGmLWOrcYNNyWF3_UULd7Zzq4Na4yxKKGHSDaSH49kbK7froa-U4fb816oOlzCljnPtnH-8uOEEvFVimv7tOd8xWEpvAu")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

            }
        });
    }
}