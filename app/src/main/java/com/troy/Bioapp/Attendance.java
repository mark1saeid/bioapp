package com.troy.Bioapp;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class Attendance extends Fragment {
  View v;
  ListView listView;
  ArrayList<String> arrayList = new ArrayList<>();
  ArrayAdapter<String> arrayAdapter;
  FirebaseAuth auth;
  FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v =  inflater.inflate(R.layout.fragment_attendance, container, false);


      auth = FirebaseAuth.getInstance();
      user = auth.getCurrentUser();




        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child("users").child(user.getUid()).child("Attend");
        listView = (ListView) v.findViewById(R.id.attendance);
        ref.addChildEventListener(new ChildEventListener() {
          @Override
          public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
              String value = postSnapshot.getValue(String.class);
              String key = postSnapshot.getKey();
              arrayList.add(key + "  :  " + value);
              arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_view_item, arrayList);
              listView.setAdapter(arrayAdapter);
            }

          }

          @Override
          public void onChildChanged(DataSnapshot dataSnapshot, String s) {

          }

          @Override
          public void onChildRemoved(DataSnapshot dataSnapshot) {

          }

          @Override
          public void onChildMoved(DataSnapshot dataSnapshot, String s) {

          }

          @Override
          public void onCancelled(DatabaseError databaseError) {

          }
        });


      return v ;
    }

}
