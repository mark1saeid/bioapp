package com.troy.Bioapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class chat extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    RecyclerView mPeopleRV;
    private FirebaseRecyclerAdapter<ChatModel, chat.NewsViewHolder> mPeopleRVAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();




        Intent intent=getIntent();
        String c = intent.getStringExtra("class");
        String n = intent.getStringExtra("name");



        mPeopleRV = findViewById(R.id.newsrecyclar);
        DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child(c).child("Chats");
        Query personsQuery = personsRef.orderByKey();
        mPeopleRV.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<ChatModel>().setQuery(personsQuery, ChatModel.class).build();
        mPeopleRVAdapter = new FirebaseRecyclerAdapter<ChatModel, chat.NewsViewHolder>(personsOptions) {
            @Override
            protected void onBindViewHolder(chat.NewsViewHolder holder, final int position, final ChatModel model) {


                holder.setName(model.getName());
                holder.setMesg(model.getMesg());


            }

            @Override
            public chat.NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chatmessage, parent, false);

                return new chat.NewsViewHolder(view);
            }
        };

        mPeopleRV.setAdapter(mPeopleRVAdapter);







    }
    public void back(View view) {
        Intent intent = new Intent (chat.this, MainActivity.class);
        startActivity(intent);
    }


    public void send(View view) {

        Intent intent=getIntent();
        String c = intent.getStringExtra("class");
        String n = intent.getStringExtra("name");

        EditText mess = findViewById(R.id.message);
        String name = n;

        DatabaseReference ss = FirebaseDatabase.getInstance().getReference().child(c).child("Chats").push();
        ss.child("name").setValue(name);
        ss.child("mesg").setValue(mess.getText().toString());
        Toast.makeText(chat.this,"message sent",Toast.LENGTH_SHORT).show();

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
}
