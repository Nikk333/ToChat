package com.chat.mark3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.UUID;

public class CROP extends AppCompatActivity {
    String result;
    Uri Fileuri;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        imageView=findViewById(R.id.IMG_EDIT);
        readintent();
        String desuri=new StringBuilder(UUID.randomUUID().toString()).append(".jpeg").toString();
        UCrop.Options options=new UCrop.Options();
        UCrop.of(Fileuri,Uri.fromFile(new File(getCacheDir(),desuri))).withOptions(options).start(CROP.this);

    }
    private void readintent()
    {

        Intent intent=getIntent();
        if(intent.getExtras()!=null)
        {
            result=intent.getStringExtra("DATA");
            Fileuri=Uri.parse(result);

        }
        else
        {
            Toast.makeText(CROP.this, "noimg", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode==UCrop.REQUEST_CROP)
        {
            final Uri resulturi=UCrop.getOutput(data);
            Intent intent=new Intent();
            intent.putExtra("result",resulturi+"");
            setResult(-1,intent);
            imageView.setImageURI(resulturi);

        } else if (resultCode==UCrop.RESULT_ERROR) {
           final Throwable croperr=UCrop.getError(data);
        }

    }
}