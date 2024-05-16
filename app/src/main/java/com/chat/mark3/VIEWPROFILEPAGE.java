package com.chat.mark3;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
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

public class VIEWPROFILEPAGE extends AppCompatActivity {
    String UID,SETIMAGE,SETFID,SETFSTATUSNAME,SETFUSERNAME,SETSPECIALFRIEND,MUTE;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    EditText NAMEBOX,STATUSNAMEBOX,BIOBOX;
    CardView SAVE;
    CircleImageView PROFILE_VIEW;
    TextView NAMETEXT,STATUSNAMETEXT;
    ImageView BACK;
    Boolean PERMISSION=false;
    Uri setImageUri;
    int i=0;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewprofilepage);
        SAVE=findViewById(R.id.SAVE_BTN);
        PROFILE_VIEW=findViewById(R.id.PROFILE_VIEW);
        STATUSNAMETEXT=findViewById(R.id.STATUSNAME_TEXT);
        NAMETEXT=findViewById(R.id.USERNAME_TEXT);
        NAMEBOX=findViewById(R.id.USERNAMEBOX);
        STATUSNAMEBOX=findViewById(R.id.STATUSNAMEBOX);
        BIOBOX=findViewById(R.id.BIOBOX);
        SAVE=findViewById(R.id.SAVE_BTN);
        BACK=findViewById(R.id.BACK_BTN);
        SAVE.setVisibility(View.GONE);
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        progressDialog=new ProgressDialog(VIEWPROFILEPAGE.this);


        getWindow().setNavigationBarColor(getColor(R.color.bottomnav));
        getWindow().setStatusBarColor(getColor(R.color.purple));
        UID=getIntent().getStringExtra("UID").toString();
        databaseReference=database.getReference("FRIENDS").child(auth.getUid()).child("fid").child(UID);


        FirebaseDatabase.getInstance().getReference("FRIENDS").child(auth.getUid()).child("fid").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DBFRIENDS dbfriends=snapshot.getValue(DBFRIENDS.class);

                Picasso.get().load(dbfriends.getFphoto()).into(PROFILE_VIEW);
                STATUSNAMETEXT.setText(dbfriends.getFstatusname());
                NAMETEXT.setText(dbfriends.getFusername());
                STATUSNAMEBOX.setText(dbfriends.getFstatusname());
                NAMEBOX.setText(dbfriends.getFusername());
                FirebaseDatabase.getInstance().getReference("USERLOGIN").child(UID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        BIOBOX.setText(snapshot.child("bio").getValue(String.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                if(dbfriends.getSpecialfriend().equals("No"))
                {
                    FirebaseDatabase.getInstance().getReference("USERLOGIN").child(UID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            DBLOGIN dblogin=snapshot.getValue(DBLOGIN.class);
                            Picasso.get().load(dblogin.getPROFILEIMG()).into(PROFILE_VIEW);
                            STATUSNAMETEXT.setText(dblogin.getSTATUSNAME());
                            NAMETEXT.setText(dblogin.getUSERNAME());
                            STATUSNAMEBOX.setText(dblogin.getSTATUSNAME());
                            NAMEBOX.setText(dblogin.getUSERNAME());
                            BIOBOX.setText(snapshot.child("bio").getValue(String.class));
                            }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    NAMEBOX.setEnabled(false);
                    STATUSNAMEBOX.setEnabled(false);
                    BIOBOX.setEnabled(false);
                    Toast.makeText(VIEWPROFILEPAGE.this,"helo",Toast.LENGTH_LONG).show();
                    PROFILE_VIEW.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Dialog dialog=new Dialog(VIEWPROFILEPAGE.this);
                            dialog.setContentView(R.layout.dailog_profile);
                            dialog.setCancelable(true);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            ImageView imageView=dialog.findViewById(R.id.dialog_profilepic);
                            Glide.with(VIEWPROFILEPAGE.this).load(PROFILE_VIEW.getDrawable()).circleCrop().into(imageView);
                            dialog.show();
                        }
                    });
                }
                else
                {
                    BIOBOX.setEnabled(false);
                    PROFILE_VIEW.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SAVE.setVisibility(View.VISIBLE);
                            Intent intent=new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent,"Select Photo"),10);
                        }
                    });
                    NAMEBOX.setOnTouchListener((v, event) -> {
                        SAVE.setVisibility(View.VISIBLE);
                        return false;
                    });
                    STATUSNAMEBOX.setOnTouchListener((v, event) -> {
                        SAVE.setVisibility(View.VISIBLE);
                        return false;
                    });
                    BIOBOX.setOnTouchListener((v, event) -> {
                        SAVE.setVisibility(View.VISIBLE);
                        return false;
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if(PERMISSION==true)
        {

        }
        BACK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(VIEWPROFILEPAGE.this,CHATWINDOW.class);
                intent.putExtra("UID",UID);
                startActivity(intent);
                finish();
            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SETIMAGE=snapshot.child("fphoto").getValue(String.class);
                SETFID=snapshot.child("fid").getValue(String.class);
                SETSPECIALFRIEND=snapshot.child("specialfriend").getValue(String.class);
                MUTE=snapshot.child("mute").getValue(String.class);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        /************************************************[SAVE DATA ->]**********************************************************/
        SAVE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Saving.....");
                progressDialog.show();
                progressDialog.setCancelable(false);
                i=0;
                if(setImageUri!=null)
                {
                    String ImageID=auth.getUid()+UID;
                    StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("EDITIMAGES").child(ImageID);
                    storageReference.putFile(setImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isComplete())
                            {
                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                       SETIMAGE=uri.toString();
                                        SETFSTATUSNAME =STATUSNAMEBOX.getText().toString();
                                        SETFUSERNAME=NAMEBOX.getText().toString();
                                        DBFRIENDS dbfriends=new DBFRIENDS(SETFID,SETFUSERNAME,SETIMAGE,SETSPECIALFRIEND,SETFSTATUSNAME,MUTE);
                                        databaseReference.setValue(dbfriends).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isComplete())
                                                {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(VIEWPROFILEPAGE.this,"Saved Pic",Toast.LENGTH_LONG).show();
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
                        SETFSTATUSNAME =STATUSNAMEBOX.getText().toString();
                        SETFUSERNAME=NAMEBOX.getText().toString();
                        DBFRIENDS dbfriends=new DBFRIENDS(SETFID,SETFUSERNAME,SETIMAGE,SETSPECIALFRIEND,SETFSTATUSNAME,MUTE);
                        databaseReference.setValue(dbfriends).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isComplete())
                                {
                                    progressDialog.dismiss();
                                    Toast.makeText(VIEWPROFILEPAGE.this,"Saved data",Toast.LENGTH_LONG).show();
                                }
                            }
                        });


                }
            }
        });

        /************************************************[<-SAVE DATA]**********************************************************/
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10)
        {
            if(data!=null)
            {
                setImageUri=data.getData();
                PROFILE_VIEW.setImageURI(setImageUri);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(VIEWPROFILEPAGE.this,CHATWINDOW.class);
        intent.putExtra("UID",UID);
        startActivity(intent);
        finish();
    }
}