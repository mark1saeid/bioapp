package com.troy.Bioapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Home extends Fragment {
    FrameLayout button1,button2,button3,button4,button5,button6,button7,button8;
    FirebaseAuth auth;
    FirebaseUser user;

View view;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


        button7 =(FrameLayout)view.findViewById(R.id.news);
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference QA = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());

                QA.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String s = dataSnapshot.child("stcl").getValue(String.class);
                        Intent intent = new Intent(getActivity(), News.class);
                        intent.putExtra("class",s);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });


        button8 =(FrameLayout)view.findViewById(R.id.live);
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference QA = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());

                QA.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String c = dataSnapshot.child("stcl").getValue(String.class);
                        String n = dataSnapshot.child("name").getValue(String.class);
                        Intent intent = new Intent(getActivity(), Live.class);
                        intent.putExtra("class",c);
                        intent.putExtra("name",n);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });



        button1 =(FrameLayout)view.findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               Fragment mFragment = new Attendance();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, mFragment).commit();
            }
        });


        button2 =(FrameLayout)view.findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               Fragment mFragment = new Quizzfragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, mFragment).commit();
            }
        });


        button3 =(FrameLayout)view.findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (getActivity(), Map.class);
                startActivity(intent);
            }
        });

        button4 =(FrameLayout)view.findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Toast.makeText(getActivity(),"Activity Pressed",Toast.LENGTH_SHORT).show();
            }
        });

        button5 =(FrameLayout)view.findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference QA = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());

                QA.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String s = dataSnapshot.child("stcl").getValue(String.class);
                        Intent intent = new Intent(getActivity(), Homework.class);
                        intent.putExtra("class",s);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });


        button6 =(FrameLayout)view.findViewById(R.id.button6);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getActivity(),"Setting Pressed",Toast.LENGTH_SHORT).show();
            }
        });



        return view;
    }


    public static void closeall() {

    }

}
