package com.troy.Bioapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HWanswer extends AppCompatActivity {
    ListView listView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    List<String> test=new ArrayList<String>();
    String qnums;
    String qmark;
    ImageView image;
    String fullmark;
    String Q;
    String A;
    String i;
    String time;
    String a;
    String b;
    String c;
    String d;
    public int studentmark =0;
    int qnum;
    int full;
    FirebaseAuth auth;
    FirebaseUser user;
    int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hwanswer);


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();





        DatabaseReference QA = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
        QA.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String stclass = dataSnapshot.child("stcl").getValue(String.class);
                ss(stclass);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






    }
    public void isanswertrue (final String qs, final String qmarks,final String fullmarkss ,String stclass){


        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child(stclass).child("Homework").child("Test").child(qs);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                final String Answer1 = snapshot.child("An").getValue().toString();
                final TextView textView1 = findViewById(R.id.text1);
                final TextView textView2 = findViewById(R.id.text2);
                final TextView textView3 = findViewById(R.id.text3);
                final TextView textView4 = findViewById(R.id.text4);

                if (Answer1.equals("a")){
                    textView1.setBackgroundResource(R.drawable.t);
                    textView2.setBackgroundResource(R.drawable.f);
                    textView3.setBackgroundResource(R.drawable.f);
                    textView4.setBackgroundResource(R.drawable.f);
                }
                if (Answer1.equals("b")){
                    textView1.setBackgroundResource(R.drawable.f);
                    textView2.setBackgroundResource(R.drawable.t);
                    textView3.setBackgroundResource(R.drawable.f);
                    textView4.setBackgroundResource(R.drawable.f);
                }
                if (Answer1.equals("c")){
                    textView1.setBackgroundResource(R.drawable.f);
                    textView2.setBackgroundResource(R.drawable.f);
                    textView3.setBackgroundResource(R.drawable.t);
                    textView4.setBackgroundResource(R.drawable.f);
                }
                if (Answer1.equals("d")){
                    textView1.setBackgroundResource(R.drawable.f);
                    textView2.setBackgroundResource(R.drawable.f);
                    textView3.setBackgroundResource(R.drawable.f);
                    textView4.setBackgroundResource(R.drawable.t);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }

    public void back(View view) {
        Intent intent = new Intent (HWanswer.this, MainActivity.class);
        startActivity(intent);
    }


    public void answermethod(String qn , String stclass){



        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child(stclass).child("Homework").child("Test").child(qn);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                a = snapshot.child("a").getValue().toString();
                b = snapshot.child("b").getValue().toString();
                c = snapshot.child("c").getValue().toString();
                d = snapshot.child("d").getValue().toString();
                TextView textView1 = findViewById(R.id.textviewahwa);
                textView1.setText(a);
                TextView textView2 = findViewById(R.id.textViewbhwa);
                textView2.setText(b);
                TextView textView3 = findViewById(R.id.textViewchwa);
                textView3.setText(c);
                TextView textView4 = findViewById(R.id.textViewdhwa);
                textView4.setText(d);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }


    public void ss(final String stclass){

        DatabaseReference Qdetails = FirebaseDatabase.getInstance().getReference().child(stclass).child("Homework");
        Qdetails.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fullmark = dataSnapshot.child("fullmark").getValue(String.class);
                qnums = dataSnapshot.child("qnum").getValue(String.class);
                qnum = Integer.parseInt(qnums);
                full = Integer.parseInt(fullmark);
                qmark = String.valueOf(full/qnum);





                test.add("Questions");
                for (int i=1;i<=qnum;i++){
                    test.add(String.valueOf(i));
                }
                TextView fullmarkText = findViewById(R.id.fullmarkhwa);
                fullmarkText.setText(fullmark);
                MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.nice_spinner1hwa);
                spinner.setItems(test);
                spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

                    @Override public void onItemSelected(MaterialSpinner view, int position, long id, final String item) {



                        DatabaseReference QA = FirebaseDatabase.getInstance().getReference().child(stclass).child("Homework").child("Test");
                        QA.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (int j=1;j<=qnum;j++){
                                    if (item.equals(String.valueOf(j))){
                                        Q = dataSnapshot.child(String.valueOf(j)).child("Q").getValue(String.class);
                                        TextView Qtex = findViewById(R.id.Qtexthwa);
                                        Qtex.setText(Q);

                                        i = dataSnapshot.child(String.valueOf(j)).child("image").getValue(String.class);
                                        image = findViewById(R.id.answerimagehwa);

                                        if (i.equals("")){image.setPadding(0,0,0,0);
                                            image.layout(0,0,0,0);}


                                        else {
                                            image.setPadding(12,12,12,12);
                                            Picasso.get()
                                                    .load(i)
                                                    .resize(1000, 520)
                                                    .onlyScaleDown()
                                                    .into(image);
                                        }

                                        A = dataSnapshot.child(String.valueOf(j)).child("A").getValue(String.class);
                                        answermethod(String.valueOf(j),stclass);
                                        isanswertrue(String.valueOf(j),qmark,fullmark,stclass );




                                    }
                                    if (item.equals("Questions")){
                                        TextView Qtex = findViewById(R.id.Qtexthwa);
                                        ImageView Qim = findViewById(R.id.answerimagehwa);
                                        TextView atex1 = findViewById(R.id.textviewahwa);
                                        TextView atex2 = findViewById(R.id.textViewbhwa);
                                        TextView atex3 = findViewById(R.id.textViewchwa);
                                        TextView atex4 = findViewById(R.id.textViewdhwa);
                                        final TextView textView1 = findViewById(R.id.text1);
                                        final TextView textView2 = findViewById(R.id.text2);
                                        final TextView textView3 = findViewById(R.id.text3);
                                        final TextView textView4 = findViewById(R.id.text4);
                                        textView1.setBackgroundResource(R.drawable.rc);
                                        textView2.setBackgroundResource(R.drawable.rc);
                                        textView3.setBackgroundResource(R.drawable.rc);
                                        textView4.setBackgroundResource(R.drawable.rc);
                                        Qtex.setText("Questions");
                                        atex1.setText("Answer");
                                        atex2.setText("Answer");
                                        atex3.setText("Answer");
                                        atex4.setText("Answer");
                                        Qim.setPadding(0,0,0,0);
                                        Qim.layout(0,0,0,0);
                                        Qim.setImageResource(R.color.colorPrimary);
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}
