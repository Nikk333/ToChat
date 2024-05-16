package com.chat.mark3;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class USERACTIVE {
    public static void ONOFF_FUN(String getActivity)
    {
        FirebaseDatabase.getInstance().getReference().child("USERLOGIN").child(FirebaseAuth.getInstance().getUid().toString()).child("statusonoff").setValue(getActivity).addOnSuccessListener(new OnSuccessListener<Void>(){@Override public void onSuccess(Void unused){}});
    }
}
