package com.chat.mark3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class CHAT_PHOTO extends AppCompatActivity {
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_photo);
        imageView=findViewById(R.id.CHAT_PHOTO);
        Intent intent=getIntent();
        if(intent.getStringExtra("CHATPHOTO")!=null)
        {
            Glide.with(CHAT_PHOTO.this).load(intent.getStringExtra("CHATPHOTO").toString()).into(imageView);
        }
    }

}