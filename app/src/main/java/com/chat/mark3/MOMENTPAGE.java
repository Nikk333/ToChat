package com.chat.mark3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class MOMENTPAGE extends AppCompatActivity {
    ImageView MOMENTVIEW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_momentpage);
        MOMENTVIEW=findViewById(R.id.MOMENTS_VIEW);
        if(getIntent().getStringExtra("PHOTO")!=null)
        {
            Glide.with(MOMENTPAGE.this).load(getIntent().getStringExtra("PHOTO")).into(MOMENTVIEW);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(this, STATUS.class);
        startActivity(intent);
        finish();
    }
}