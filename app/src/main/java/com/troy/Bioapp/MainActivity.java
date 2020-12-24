package com.troy.Bioapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialog;
import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialogListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    ImageView button;
    List<String> test=new ArrayList<String>();
    FirebaseAuth auth;
    FirebaseUser user;
    boolean connected = false;
    static MaterialSpinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();



        DatabaseReference QAss = FirebaseDatabase.getInstance().getReference().child("update");

        QAss.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
              String  vs = dataSnapshot.child("v").getValue(String.class);
              String vlink = dataSnapshot.child("link").getValue(String.class);
              update(vs , vlink);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        TextView studentid = findViewById(R.id.student_id);
        studentid.setText(user.getUid());
        DatabaseReference QA = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
        QA.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.child("name").getValue(String.class);
                TextView Studentname = findViewById(R.id.student_name);
                Studentname.setText("Hi Welcome "+value+" !");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Fragment mFragment = new Home();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, mFragment).commit();



         spinner = (MaterialSpinner) findViewById(R.id.nice_spinner);
        test.add("Menu");
        test.add("Home");
        spinner.setItems(test);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                if (item.equals("Home")){
                    Fragment mFragment = new Home();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, mFragment).commit();
                }
            }
        });




    }

    private void checkinternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        }
        else {
            connected = false;
            new TTFancyGifDialog.Builder(MainActivity.this)
                    .setTitle("No Internet!")
                    .setMessage("Please make sure to connect your phone to the internet.")
                    .setPositiveBtnText("Ok")
                    .setPositiveBtnBackground("#3490dc")
                    .setGifResource(R.drawable.nonetwork)      //pass your gif, png or jpg
                    .isCancellable(false)
                    .build();
        }
    }


    //Notification by Firebase

    private void addNotification() {
        // Builds your notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Test")
                .setContentText("TEEEEEEESSSSSSSSST");

        // Creates the intent needed to show the notification
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }



    public void openbarcode(View view) {
        Intent intent = new Intent (MainActivity.this, MyBarcode.class);
        startActivity(intent);
    }
    public void openchat(View view) {
        DatabaseReference QA = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
        QA.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String c = dataSnapshot.child("stcl").getValue(String.class);
                String n = dataSnapshot.child("name").getValue(String.class);
                Intent intent = new Intent(MainActivity.this, chat.class);
                intent.putExtra("class",c);
                intent.putExtra("name",n);
                startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }




    @Override
    protected void onResume() {

        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
        checkinternet();
        super.onResume();
    }








public void update(String v , final String link ){
        String cv = "3.2";
    if (v.equals(cv)){
        Toast.makeText(this,"App Is Up To Date",Toast.LENGTH_SHORT).show();
    }
    else {
        new TTFancyGifDialog.Builder(MainActivity.this)
                .setTitle("A New Version Released")
                .setMessage("Please Download Now")
                .setPositiveBtnText("Download")
                .setPositiveBtnBackground("#3490dc")
                .setGifResource(R.drawable.update)
                .isCancellable(false)
                .OnPositiveClicked(new TTFancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        Toast.makeText(MainActivity.this,"thanks",Toast.LENGTH_SHORT).show();
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                        startActivity(browserIntent);
                    }
                })
                .build();
    }
}



}


