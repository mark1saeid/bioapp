package com.troy.Bioapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;


public class Share extends Fragment {




    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_share, container, false);




        Button button = rootView.findViewById(R.id.share);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Test");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Test");
                shareIntent.setType("text/plain");
                startActivity(shareIntent);
            }
        });




        return rootView;
    }









































}