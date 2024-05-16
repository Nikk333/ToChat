package com.chat.mark3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class STATUS extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    CircleImageView OWNPIC;
    RecyclerView recyclerView;
    ArrayList<DBSTATUS> statusArrayList;
    StatusAdapter statusAdapter;
    TextView text;
    Uri imageURI;
    CardView STATUS_BTN,DELETE_MOMENT_BTN;
    String OWNUID,OWNPHOTO,OWNUSERNAME,OWNSTATUSNAME;
    Handler h = new Handler();
    int progress = 0,i=0;
    Resources rs;
    EditText SEARCHBOX;
    ActivityResultLauncher<String> message;
    String result;
    Boolean a=false;
    ImageView MENU_DOTS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        SEARCHBOX=findViewById(R.id.SEARCH_STATUS);
        getWindow().setStatusBarColor(getColor(R.color.white));
        getWindow().setNavigationBarColor(getColor(R.color.purple));
        ProgressDialog progressDialog;
        progressDialog=new ProgressDialog(STATUS.this);
        progressDialog.setCancelable(false);
        getWindow().setNavigationBarColor(getColor(R.color.whitelight));
        getWindow().setStatusBarColor(getColor(R.color.white));

        //imageURI=new ArrayList<>();
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();
        OWNPIC=findViewById(R.id.STATUSPICKER);
        text=findViewById(R.id.TEXT);
        DELETE_MOMENT_BTN=findViewById(R.id.MOMENT_DELETE);
        DELETE_MOMENT_BTN.setVisibility(View.GONE);
        MENU_DOTS=findViewById(R.id.MENU_DOTS);
        STATUS_BTN=findViewById(R.id.STATUSBTN);
        statusArrayList=new ArrayList<>();
        recyclerView=findViewById(R.id.RECYCLESTATUS);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        statusAdapter=new StatusAdapter(STATUS.this,statusArrayList);
        recyclerView.setAdapter(statusAdapter);
        DATA_FUN();
        OWNDATA_FUN();
        /********************************************[IMAGE EDITINING ->]**********************************************/
        OWNPIC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message.launch("image/*");
            }
        });
        message=registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri o) {
                Intent intent=new Intent(STATUS.this, CROP.class);
                intent.putExtra("DATA",o.toString());
                startActivityForResult(intent,101);

            }
        });
        /********************************************[<-IMAGE EDITINING]**********************************************/




        /*************************************************[DELETE MOMENT->]**************************************************************/
        MENU_DOTS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("STATUS").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            if(DELETE_MOMENT_BTN.getVisibility()==View.GONE) {
                                DELETE_MOMENT_BTN.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                DELETE_MOMENT_BTN.setVisibility(View.GONE);

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        DELETE_MOMENT_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DELETE_MOMENT_BTN.setVisibility(View.GONE);

                progressDialog.setMessage("Deleting.....");
                progressDialog.show();

                StorageReference storageReference = storage.getReference().child("STATUSDB").child(auth.getUid());
                storageReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isComplete())
                        {
                            DatabaseReference databaseReference3=database.getReference().child("STATUS").child(auth.getUid());
                            databaseReference3.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        progressDialog.dismiss();
                                        Toast.makeText(STATUS.this,"Deleted Successfully",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                    }
                });
            }
        });
        /*************************************************[DELETE MOMENT->]**************************************************************/

        /*************************************************[MOMENT UPLOADING->]**************************************************************/

       STATUS_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(imageURI!=null) {
                    progressDialog.setMessage("Uploading...");
                    progressDialog.show();
                    progressDialog.setCancelable(true);
                    StorageReference storageReference = storage.getReference().child("STATUSDB").child(auth.getUid());
                    storageReference.putFile(imageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isComplete()) {
                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String setimageuri = uri.toString();
                                        DBSTATUS dbstatus=new DBSTATUS(OWNUID,setimageuri,OWNPHOTO,OWNUSERNAME,OWNSTATUSNAME);
                                        DatabaseReference databaseReference3=database.getReference().child("STATUS").child(auth.getUid());
                                        databaseReference3.setValue(dbstatus).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(STATUS.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                                    a=false;
                                                    DATA_FUN();
                                                    OWNDATA_FUN();
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
                    Toast.makeText(STATUS.this, "noimg", Toast.LENGTH_SHORT).show();
                }

            }
        });
        /*************************************************[<-MOMENT UPLOADING]**************************************************************/
    SEARCHBOX.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            statusAdapter.filter(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    });


    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(STATUS.this,FRIENDSCHAT.class);
        startActivity(intent);
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       /* if(requestCode==10)
        {
            if(data!=null)
            {
                imageURI.add(data.getData());
                for(int i=0;i<imageURI.size();i++)
                {
                    OWNPIC.setImageURI(imageURI.get(i));
                }

            }
        }*/
        if(requestCode==101 && resultCode==-1)
        {

          result=data.getStringExtra("result");
            Uri uri=null;
            if(result!=null)
            {
                a=true;
                uri= Uri.parse(result);
            }
            else
            {
                Toast.makeText(STATUS.this,"Something went wrong",Toast.LENGTH_SHORT).show();
            }
            imageURI=uri;
            Picasso.get().load(uri).into(OWNPIC);

        }
        else
        {
        }
    }

  /*  public void com()
    {
        for(int i=0;i<imageURI.size();i++)
        {
            try {
                Bitmap imageBitmap= MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageURI.get(i));
                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.JPEG,15,stream);
                byte[] imagebyte=stream.toByteArray();
                Toast.makeText(STATUS.this,""+imagebyte.length,Toast.LENGTH_LONG).show();
                upd(imagebyte);

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
    public  void upd(byte [] imagebyte)
    {
        if(imageURI!=null) {
            StorageReference storageReference = storage.getReference().child("STATUSDB").child(auth.getUid());
            storageReference.putBytes(imagebyte).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isComplete()) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String setimageuri = uri.toString();
                                DBSTATUS dbstatus=new DBSTATUS(OWNUID,setimageuri,OWNPHOTO,OWNUSERNAME,OWNSTATUSNAME);
                                DatabaseReference databaseReference3=database.getReference().child("STATUS").child(auth.getUid());
                                databaseReference3.setValue(dbstatus).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(STATUS.this, "Done", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(STATUS.this, "noimg", Toast.LENGTH_SHORT).show();
        }
    }*/
    private void OWNDATA_FUN(){
        FirebaseDatabase.getInstance().getReference("USERLOGIN").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                OWNUID=snapshot.child("uid").getValue(String.class);
                OWNPHOTO=snapshot.child("profileimg").getValue(String.class);
                OWNUSERNAME=snapshot.child("username").getValue(String.class);
                OWNSTATUSNAME=snapshot.child("statusname").getValue(String.class);
                if(a==true)
                {

                }
                else
                {
                    Picasso.get().load(OWNPHOTO).into(OWNPIC);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void DATA_FUN() {
        FirebaseDatabase.getInstance().getReference("STATUS").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                statusArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DBSTATUS dbstatus = dataSnapshot.getValue(DBSTATUS.class);
                    statusArrayList.add(dbstatus);
                }
                statusAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
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