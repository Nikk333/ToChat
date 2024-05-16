package com.chat.mark3;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class INFOPAGE extends AppCompatActivity {
    CardView cv1,cv2,cv3,cv4,cv5;
    ConstraintLayout constraintLayout;
    SwipeListner swipe;
    int a=0,b1=0,z=0,c=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infopage);
        constraintLayout=findViewById(R.id.CONS);
        swipe=new SwipeListner(constraintLayout);
        cv1=findViewById(R.id.cv_LEFT);
        cv2=findViewById(R.id.cv_CENTER);
        cv3=findViewById(R.id.cv_RIGHT);
        cv4=findViewById(R.id.skipbtn);
        cv5=findViewById(R.id.startbtn);
        fragments(new FIRSTFRAGMENT(),true);
        cv1.setCardBackgroundColor(getColor(R.color.purple));
        cv5.setVisibility(View.GONE);

        getWindow().setNavigationBarColor(getColor(R.color.white));
        getWindow().setStatusBarColor(getColor(R.color.white));

        cv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(getApplicationContext(), FIRSTPAGE.class);
                startActivity(in);
                finish();
            }
        });
        cv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(getApplicationContext(), FIRSTPAGE.class);
                startActivity(in);
                finish();
            }
        });
    }
    public void fragments(Fragment fm1, boolean b)
    {
        FragmentManager fm= getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        if(b==true)
        {
            ft.add(R.id.ni,fm1);
        }
        else
        {
            ft.replace(R.id.ni,fm1);
        }
        ft.commit();
    }
    private class SwipeListner implements View.OnTouchListener{
        GestureDetector gd;
        SwipeListner(View view){
            int threshold=100;
            int velocity_threadshold=100;

            GestureDetector.SimpleOnGestureListener simpleOnGestureListener=new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onDown(@NonNull MotionEvent e) {
                    return true;
                }
                @Override
                public boolean onFling(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {

                    float xDiff=e2.getX()-e1.getX();
                    float yDiff=e2.getY()-e1.getY();
                    try {
                        if(Math.abs(xDiff)>Math.abs(yDiff))
                        {
                            if(Math.abs(xDiff)>threshold && Math.abs(velocityX)>velocity_threadshold)
                            {
                                if(xDiff>0)
                                {
                                    /*fragments(new SECONDFRAGMENT(),true);
                                    cv2.setCardBackgroundColor(getColor(R.color.red));
                                    cv3.setCardBackgroundColor(getColor(R.color.whitelight));
                                    cv1.setCardBackgroundColor(getColor(R.color.whitelight));
                                    cv5.setVisibility(View.GONE);
                                    cv4.setVisibility(View.VISIBLE);*/
                                        if(xDiff>0 && b1==1){
                                             fragments(new FIRSTFRAGMENT(),true);
                                            cv1.setCardBackgroundColor(getColor(R.color.purple));
                                            cv3.setCardBackgroundColor(getColor(R.color.whitelight));
                                            cv2.setCardBackgroundColor(getColor(R.color.whitelight));
                                            cv5.setVisibility(View.GONE);
                                            cv4.setVisibility(View.VISIBLE);
                                            b1=0;
                                            z=1;
                                        }
                                    b1=1;
                                    if(c==1)
                                    {
                                        fragments(new SECONDFRAGMENT(),true);
                                        cv2.setCardBackgroundColor(getColor(R.color.purple));
                                        cv1.setCardBackgroundColor(getColor(R.color.whitelight));
                                        cv3.setCardBackgroundColor(getColor(R.color.whitelight));
                                        c=0;
                                    }
                                }
                                else if (z==1) {
                                    fragments(new SECONDFRAGMENT(), true);
                                    cv2.setCardBackgroundColor(getColor(R.color.purple));
                                    cv3.setCardBackgroundColor(getColor(R.color.whitelight));
                                    cv1.setCardBackgroundColor(getColor(R.color.whitelight));
                                    z=0;
                                    cv5.setVisibility(View.GONE);
                                    cv4.setVisibility(View.VISIBLE);
                                }
                                else if (a==1 ) {
                                    fragments(new THIRDFRAGMENT(),true);
                                    cv3.setCardBackgroundColor(getColor(R.color.purple));
                                    cv2.setCardBackgroundColor(getColor(R.color.whitelight));
                                    cv1.setCardBackgroundColor(getColor(R.color.whitelight));
                                    cv4.setVisibility(View.GONE);
                                    cv5.setVisibility(View.VISIBLE);
                                    c=1;
                                } else {
                                    if(a==1){
                                    }
                                    else {
                                        fragments(new SECONDFRAGMENT(), true);
                                        cv2.setCardBackgroundColor(getColor(R.color.purple));
                                        cv3.setCardBackgroundColor(getColor(R.color.whitelight));
                                        cv1.setCardBackgroundColor(getColor(R.color.whitelight));
                                        a=1;
                                        cv5.setVisibility(View.GONE);
                                        cv4.setVisibility(View.VISIBLE);
                                    }
                                }
                                return true;
                            }
                        }
                        else {
                        }
                    }
                    catch (Exception e)
                    {
                    }
                    return false;
                }
            };
            gd=new GestureDetector(simpleOnGestureListener);
            view.setOnTouchListener(this);

        }
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gd.onTouchEvent(event);
        }
    }
}
