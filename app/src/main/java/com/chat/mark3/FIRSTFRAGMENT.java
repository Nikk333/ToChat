package com.chat.mark3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
public class FIRSTFRAGMENT extends Fragment {
    public FIRSTFRAGMENT() {
    }      // Required empty public constructor
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.firstfragment, container, false);
    }
}