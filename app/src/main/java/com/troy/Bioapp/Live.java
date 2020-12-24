package com.troy.Bioapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.drm.DrmErrorEvent;
import android.drm.DrmManagerClient;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Live extends AppCompatActivity {

  //  private String videoPath ="https://bcovlive-a.akamaihd.net/87f7c114719b4646b7c4263c26515cf3/eu-central-1/6008340466001/profile_0/chunklist.m3u8?checkedby:hlscat.com";

    private static ProgressDialog progressDialog;
    String videourl;
    VideoView videoView ;

    FirebaseAuth auth;
    FirebaseUser user;
    RecyclerView mPeopleRV;
    private FirebaseRecyclerAdapter<ChatModel, Live.NewsViewHolder> mPeopleRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);

        Intent intent=getIntent();
        String c = intent.getStringExtra("class");
        String n = intent.getStringExtra("name");

        videoView = (VideoView) findViewById(R.id.videoView);


        progressDialog = ProgressDialog.show(Live.this, "", "Buffering video...", true);
        progressDialog.setCancelable(true);



        DatabaseReference QA = FirebaseDatabase.getInstance().getReference().child(c).child("Live");

        QA.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String l = dataSnapshot.child("link").getValue(String.class);

                PlayVideo(l);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();








        mPeopleRV = findViewById(R.id.newsrecyclar);
        DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child(c).child("Live").child("Chats");
        Query personsQuery = personsRef.orderByKey();
        mPeopleRV.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<ChatModel>().setQuery(personsQuery, ChatModel.class).build();
        mPeopleRVAdapter = new FirebaseRecyclerAdapter<ChatModel, Live.NewsViewHolder>(personsOptions) {
            @Override
            protected void onBindViewHolder(Live.NewsViewHolder holder, final int position, final ChatModel model) {


                holder.setName(model.getName());
                holder.setMesg(model.getMesg());


            }

            @Override
            public Live.NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chatmessage, parent, false);

                return new Live.NewsViewHolder(view);
            }
        };

        mPeopleRV.setAdapter(mPeopleRVAdapter);



    }
    private void PlayVideo(String videoPath)
    {
        try
        {
            getWindow().setFormat(PixelFormat.TRANSLUCENT);
            MediaController mediaController = new MediaController(Live.this);
            mediaController.setAnchorView(videoView);

            Uri video = Uri.parse(videoPath);
            videoView.setMediaController(mediaController);
            videoView.setVideoURI(video);
            videoView.requestFocus();
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
            {

                public void onPrepared(MediaPlayer mp)
                {
                    progressDialog.dismiss();
                    videoView.start();
                }
            });


        }
        catch(Exception e)
        {
            progressDialog.dismiss();
            System.out.println("Video Play Error :"+e.toString());
            finish();
        }

    }





    public void send(View view) {

        Intent intent=getIntent();
        String c = intent.getStringExtra("class");
        String n = intent.getStringExtra("name");

        EditText mess = findViewById(R.id.message);
        String name = n;

        DatabaseReference ss = FirebaseDatabase.getInstance().getReference().child(c).child("Live").child("Chats").push();
        ss.child("name").setValue(name);
        ss.child("mesg").setValue(mess.getText().toString());
        Toast.makeText(Live.this,"message sent",Toast.LENGTH_SHORT).show();

        mess.setText("");


    }


    @Override
    public void onStart() {
        super.onStart();
        mPeopleRVAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPeopleRVAdapter.stopListening();
    }







    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public NewsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setName(String names) {
            TextView name = (TextView) mView.findViewById(R.id.name);
            name.setText(names);
        }

        public void setMesg(String mesgs) {
            TextView mesg = (TextView) mView.findViewById(R.id.chatmessage);
            mesg.setText(mesgs);
        }


    }

    public void back(View view) {
        Intent intent = new Intent (Live.this, MainActivity.class);
        startActivity(intent);
    }
}
