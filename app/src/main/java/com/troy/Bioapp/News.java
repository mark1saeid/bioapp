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
import android.widget.ImageView;
import android.widget.TextView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class News extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    RecyclerView mPeopleRV;
    private FirebaseRecyclerAdapter<NewsModel, NewsViewHolder> mPeopleRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();




        Intent intent=getIntent();
        String s = intent.getStringExtra("class");



         mPeopleRV = findViewById(R.id.newsrecyclar);
         DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child(s).child("News");
         Query personsQuery = personsRef.orderByKey();
         mPeopleRV.setLayoutManager(new LinearLayoutManager(this));
         FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<NewsModel>().setQuery(personsQuery, NewsModel.class).build();
         mPeopleRVAdapter = new FirebaseRecyclerAdapter<NewsModel, News.NewsViewHolder>(personsOptions) {
            @Override
            protected void onBindViewHolder(News.NewsViewHolder holder, final int position, final NewsModel model) {


                holder.setTitle(model.getTitle());
                holder.setDesc(model.getDesc());
                holder.setImage(model.getImage());

            }

            @Override
            public News.NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.newspost, parent, false);

                return new News.NewsViewHolder(view);
            }
        };

        mPeopleRV.setAdapter(mPeopleRVAdapter);

        }




    public void back(View view) {
        Intent intent = new Intent(News.this, MainActivity.class);
        startActivity(intent);
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

        public void setTitle(String title) {
            TextView post_title = (TextView) mView.findViewById(R.id.post_title);
            post_title.setText(title);
        }

        public void setDesc(String desc) {
            TextView post_desc = (TextView) mView.findViewById(R.id.post_desc);
            post_desc.setText(desc);
        }

        public void setImage(String image) {
            if (image.equals("")){
                PhotoView post_image =  mView.findViewById(R.id.post_image);
                post_image.setBackgroundColor(Color.BLACK);
            }
            else {
            ImageView post_image = (ImageView) mView.findViewById(R.id.post_image);
            Picasso.get().load(image).into(post_image);
            }
        }
    }

}
