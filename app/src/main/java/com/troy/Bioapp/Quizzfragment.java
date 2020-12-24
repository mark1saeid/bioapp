package com.troy.Bioapp;


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;


public class Quizzfragment extends Fragment {
  View view;
  FrameLayout button1,button2;
  FirebaseAuth auth;
  FirebaseUser user;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      view = inflater.inflate(R.layout.fragment_quizzfragment, container, false);

      auth = FirebaseAuth.getInstance();
      user = auth.getCurrentUser();






      final DatabaseReference Q = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
      Q.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
          String qlocked = dataSnapshot.child("qlocked").getValue().toString();
          if (qlocked.equals("0")){
            button2 =(FrameLayout)view.findViewById(R.id.button21);
            button2.setVisibility(View.INVISIBLE);
          }
          else {
            button2 =(FrameLayout)view.findViewById(R.id.button21);
            button2.setVisibility(View.VISIBLE);
          }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
      });





      button1 =(FrameLayout)view.findViewById(R.id.button11);
      button1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {


          Fragment mFragment = new quizscore();

          FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
          fragmentManager.beginTransaction()
                  .replace(R.id.content_frame, mFragment).commit();
        }
      });





      button2 =(FrameLayout)view.findViewById(R.id.button21);
      button2.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

          DatabaseReference QA = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
          QA.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

              String s = dataSnapshot.child("stcl").getValue(String.class);
              Intent intent = new Intent(getActivity(), Quiz.class);
              intent.putExtra("class",s);
              startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
          });
        }
      });



    return view;
    }

}
