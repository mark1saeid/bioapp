package com.troy.Bioapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.Date;
import java.util.Random;


public class Quiz extends AppCompatActivity {
    static Float [] arr;
    FirebaseAuth auth;
    FirebaseUser user;
    RecyclerView mPeopleRV;
    private FirebaseRecyclerAdapter<QModel, Quiz.NewsViewHolder> mPeopleRVAdapter;
    static float studentmark ;
    private static int qnump;
    private static int fullmarkp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        studentmark = 0;

        Intent intent=getIntent();
        final String classname = intent.getStringExtra("class");

        SharedPreferences.Editor editor = getSharedPreferences("saveqh", MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();



        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child(classname).child("Quizz");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

String data1 = snapshot.child("timem").getValue(String.class);
String data2 =snapshot.child("qnum").getValue(String.class);
String data3 =snapshot.child("fullmark").getValue(String.class);
if (data1 != null || data2 != null|| data3 != null) {
    int qnum = Integer.parseInt(data2);
    int timem = Integer.parseInt(data1);
    timer(timem);
    arr = new Float[qnum];
}else {
    Intent intent = new Intent(Quiz.this, MainActivity.class);
    startActivity(intent);
}


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


        DatabaseReference databases = FirebaseDatabase.getInstance().getReference();
        DatabaseReference refs = databases.child(classname).child("Quizz");
        refs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int fullmark = Integer.parseInt(snapshot.child("fullmark").getValue().toString());
                int qnum = Integer.parseInt(snapshot.child("qnum").getValue().toString());
                TextView fullm = findViewById(R.id.fullmark);
                fullm.setText(String.valueOf(fullmark));
                fullmarkp = fullmark;
                qnump = qnum;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


        mPeopleRV = findViewById(R.id.newsrecyclar);

        DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child(classname).child("Quizz").child("Test");
        Query personsQuery = personsRef.orderByKey();
        mPeopleRV.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<QModel>().setQuery(personsQuery, QModel.class).build();
        mPeopleRVAdapter = new FirebaseRecyclerAdapter<QModel, Quiz.NewsViewHolder>(personsOptions) {
            @Override
            protected void onBindViewHolder(final Quiz.NewsViewHolder holder, final int position, final QModel model) {
                holder.seta(model.getA());
                holder.setb(model.getB());
                holder.setc(model.getC());
                holder.setd(model.getD());
                holder.setimage(model.getImage());
                holder.setQ(model.getQ());
                holder.qn(model.getId());
                holder.answercheck(model.getAn(),fullmarkp,qnump,model.getId(),retrivedata(model.getId()));

            }

            @Override
            public Quiz.NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.qpost, parent, false);
                return new Quiz.NewsViewHolder(view);
            }
        };

        mPeopleRV.setAdapter(mPeopleRVAdapter);
        mPeopleRV.addItemDecoration(new DividerItemDecoration(this, 0));


        Button clickButton = (Button) findViewById(R.id.finishs);
        clickButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auto();
                reason("finished");
            }
        });



    }

    private void auto() {
        Date d = new Date();
        CharSequence s  = DateFormat.format("MMMM d, yyyy ", d.getTime());
        for (int i =0 ; i <arr.length ; i++){
            if (arr[i] == null){
                studentmark = studentmark + 0;
            }else {
            studentmark = studentmark + arr[i];
            }
        }
        final TextView fullm = findViewById(R.id.fullmark);
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child("users").child(user.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                if (name != null) {
                    yaaaa(studentmark, fullm.getText().toString(), name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference submit = mDatabase.getReference().child("users").child(user.getUid());
        submit.child("qlocked").setValue("0");
        DatabaseReference mDbRef = mDatabase.getReference().child("users").child(user.getUid()).child("quizz").child("scores");
        writemodel postcreated = new writemodel(String.valueOf(studentmark),String.valueOf(s));
        mDbRef.push().setValue(postcreated);
    }


    public void back(View view) {
        Intent intent = new Intent(Quiz.this, MainActivity.class);
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







    public  class NewsViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public NewsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setQ(String data){
            TextView Q = mView.findViewById(R.id.Qtext);
            Q.setText(data);

        }
        public void setimage(String data){
            PhotoView image = mView.findViewById(R.id.answerimage);
            if (data.equals("")){  image.setPadding(0,0,0,0);
                image.layout(0,0,0,0);}
            else {  image.setPadding(12,12,12,12);
                Picasso.get()
                        .load(data)
                        .resize(1000,520)
                        .onlyScaleDown()
                        .into(image);
            }

        }
        public void qn(String data){

            TextView qn = mView.findViewById(R.id.qsnum);
            qn.setText(data);
        }
        public void seta(String data){

            TextView a = mView.findViewById(R.id.textviewa);
            a.setText(data);
        }
        public void setb(String data){
            TextView b = mView.findViewById(R.id.textViewb);
            b.setText(data);

        }
        public void setc(String data){

            TextView c = mView.findViewById(R.id.textViewc);

            c.setText(data);
        }
        public void setd(String data){
            TextView d = mView.findViewById(R.id.textViewd);
            d.setText(data);

        }

        public void answercheck(final String data, float fullmark , int qnum , final String q ,String answershared ){
            final Float qmark = fullmark/qnum ;
            final TextView textView1 = mView.findViewById(R.id.text1);
            final TextView textView2 = mView.findViewById(R.id.text2);
            final TextView textView3 = mView.findViewById(R.id.text3);
            final TextView textView4 = mView.findViewById(R.id.text4);
            final  TextView y  = mView.findViewById(R.id.qsnum);
            textView1.setBackgroundResource(R.drawable.rc);
            textView2.setBackgroundResource(R.drawable.rc);
            textView3.setBackgroundResource(R.drawable.rc);
            textView4.setBackgroundResource(R.drawable.rc);
            final String A ="a";
            textView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    textView1.setBackgroundResource(R.drawable.rb);
                    textView2.setBackgroundResource(R.drawable.rc);
                    textView3.setBackgroundResource(R.drawable.rc);
                    textView4.setBackgroundResource(R.drawable.rc);
                    y.setBackgroundResource(R.drawable.f);
                    if (A.equals(data)){
                        arr[Integer.parseInt(q)-1] = qmark;

                    }else {
                        arr[Integer.parseInt(q)-1] = Float.parseFloat("0");
                    }
                    savedata(q, A);
                }
            });
            final String B ="b";
            textView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    textView1.setBackgroundResource(R.drawable.rc);
                    textView2.setBackgroundResource(R.drawable.rb);
                    textView3.setBackgroundResource(R.drawable.rc);
                    textView4.setBackgroundResource(R.drawable.rc);
                    y.setBackgroundResource(R.drawable.f);
                    if (B.equals(data)){

                        arr[Integer.parseInt(q)-1] = qmark;

                    }else {
                        arr[Integer.parseInt(q)-1] = Float.parseFloat("0");
                    }

                    savedata(q, B);        }
            });
            final String C ="c";
            textView3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    textView1.setBackgroundResource(R.drawable.rc);
                    textView2.setBackgroundResource(R.drawable.rc);
                    textView3.setBackgroundResource(R.drawable.rb);
                    textView4.setBackgroundResource(R.drawable.rc);
                    y.setBackgroundResource(R.drawable.f);
                    if (C.equals(data)){

                        arr[Integer.parseInt(q)-1] = qmark;

                    }else {
                        arr[Integer.parseInt(q)-1] = Float.parseFloat("0");
                    }

                    savedata(q, C);     }
            });
            final String D ="d";
            textView4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    textView1.setBackgroundResource(R.drawable.rc);
                    textView2.setBackgroundResource(R.drawable.rc);
                    textView3.setBackgroundResource(R.drawable.rc);
                    textView4.setBackgroundResource(R.drawable.rb);
                    y.setBackgroundResource(R.drawable.f);
                    if (D.equals(data)){
                        arr[Integer.parseInt(q)-1] = qmark;

                    }else {
                        arr[Integer.parseInt(q)-1] = Float.parseFloat("0");
                    }
                    savedata(q, D);
                }
            });

            if (answershared != null) {
                if (answershared.equals("a")) {
                    textView1.setBackgroundResource(R.drawable.rb);
                    textView2.setBackgroundResource(R.drawable.rc);
                    textView3.setBackgroundResource(R.drawable.rc);
                    textView4.setBackgroundResource(R.drawable.rc);
                    if (A.equals(data)) {

                        arr[Integer.parseInt(q)-1] = qmark;

                    }else {
                        arr[Integer.parseInt(q)-1] = Float.parseFloat("0");
                    }
                    savedata(q, A);
                }
                if (answershared.equals("b")) {
                    textView1.setBackgroundResource(R.drawable.rc);
                    textView2.setBackgroundResource(R.drawable.rb);
                    textView3.setBackgroundResource(R.drawable.rc);
                    textView4.setBackgroundResource(R.drawable.rc);
                    if (B.equals(data)) {

                        arr[Integer.parseInt(q)-1] = qmark;

                    }else {
                        arr[Integer.parseInt(q)-1] = Float.parseFloat("0");
                    }
                    savedata(q, B);
                }
                if (answershared.equals("c")) {
                    textView1.setBackgroundResource(R.drawable.rc);
                    textView2.setBackgroundResource(R.drawable.rc);
                    textView3.setBackgroundResource(R.drawable.rb);
                    textView4.setBackgroundResource(R.drawable.rc);
                    if (C.equals(data)) {

                        arr[Integer.parseInt(q)-1] = qmark;

                    }else {
                        arr[Integer.parseInt(q)-1] = Float.parseFloat("0");
                    }
                    savedata(q, C);
                }
                if (answershared.equals("d")) {
                    textView1.setBackgroundResource(R.drawable.rc);
                    textView2.setBackgroundResource(R.drawable.rc);
                    textView3.setBackgroundResource(R.drawable.rc);
                    textView4.setBackgroundResource(R.drawable.rb);
                    if (D.equals(data)) {

                        arr[Integer.parseInt(q)-1] = qmark;

                    }else {
                        arr[Integer.parseInt(q)-1] = Float.parseFloat("0");
                    }
                    savedata(q, D);
                }
            }
            else {
                y.setBackgroundResource(R.drawable.xx);
            }

        }
    }

























public int counter;



    public void timer(final int time) {
       final TextView timmers = findViewById(R.id.timer);
        new CountDownTimer(time*1000*60, 60000) {
            public void onTick(long millisUntilFinished) {
                timmers.setText(String.valueOf(counter) + "/" + time);
                counter++;

            }
            public  void onFinish(){
                reason("timer finished");
                timmers.setText("FINISH!!");
                FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
                DatabaseReference submit = mDatabase.getReference().child("users").child(user.getUid());
                submit.child("qlocked").setValue("0");
                auto();
                Intent intent = new Intent(Quiz.this, MainActivity.class);
                startActivity(intent);
            }
        }.start();
    }



@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public void yaaaa(float studentmarkss, final String fullmark , String name){
    float midmark = Float.parseFloat(fullmark)/2;
    float midmid = midmark/2;
    if (studentmarkss >= (midmid+midmark)){
        videodialog(getRandom(moremid),"You GOT "+String.valueOf(studentmarkss)+" from "+fullmark,String.valueOf(studentmarkss),fullmark,name);
    }
    if (studentmarkss >= midmark && studentmarkss < (midmid+midmark)){
        videodialog(getRandom(mid),"You GOT "+String.valueOf(studentmarkss)+" from "+fullmark,String.valueOf(studentmarkss),fullmark,name);
    }
    if (studentmarkss < midmark){
        videodialog(getRandom(lessmid),"You GOT "+String.valueOf(studentmarkss)+" from "+fullmark,String.valueOf(studentmarkss),fullmark,name);
    }
}


    @Override
    public void onDestroy()
    {
        super.onDestroy();
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference qlocked = mDatabase.getReference().child("users").child(user.getUid());
        qlocked.child("qlocked").setValue("0");
        reason("App Is Closed");
    }

public void reason (final String data){
    Intent intent=getIntent();
    final String classname = intent.getStringExtra("class");
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    DatabaseReference ref = database.child("users").child(user.getUid());
    ref.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot snapshot) {
            String name = snapshot.child("name").getValue().toString();
            FirebaseDatabase ss = FirebaseDatabase.getInstance();
            DatabaseReference mm = ss.getReference().child(classname).child("Events").push();
            mm.child("name").setValue(name);
            mm.child("reason").setValue(data);
            mm.child("marks").setValue(String.valueOf(studentmark));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }

    });
}

    public AlertDialog dialog;
    int count =0;
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public void videodialog(final String video_uri , final String msg , final String studentmarkss , final String fullmarks, final String name){
    if (count == 0) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Quiz.this);
        builder.setView(R.layout.dialog_layout);
        dialog = builder.create();
        dialog.setCancelable(false);

        dialog.show();
        final VideoView videoview = (VideoView) dialog.findViewById(R.id.dialog_videoview);
        final TextView msfg = (TextView) dialog.findViewById(R.id.msg);
        msfg.setText(msg);
        Uri uri = Uri.parse(video_uri);
        videoview.setVideoURI(uri);
        videoview.start();
        videoview.resume();
        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
        final Button ok = dialog.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Float.parseFloat(studentmarkss) == Float.parseFloat(fullmarks)) {
                    Intent intent = new Intent(Quiz.this, Fullmarks.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(Quiz.this, MainActivity.class);
                    startActivity(intent);
                }
                dialog.dismiss();
            }
        });
    }
    count++;
}


 public  String mid[] = new String[]{"android.resource://" + "com.troy.Bioapp" + "/" +
            R.raw.comm1 ,"android.resource://" + "com.troy.Bioapp" + "/" +
            R.raw.comm2 ,"android.resource://" + "com.troy.Bioapp" + "/" +
            R.raw.comm3 ,"android.resource://" + "com.troy.Bioapp" + "/" +
            R.raw.comm4 ,"android.resource://" + "com.troy.Bioapp" + "/" +
            R.raw.comm5 ,"android.resource://" + "com.troy.Bioapp" + "/" +
            R.raw.comm6 ,"android.resource://" + "com.troy.Bioapp" + "/" +
            R.raw.comm9 ,"android.resource://" + "com.troy.Bioapp" + "/" +
            R.raw.comm10,"android.resource://" + "com.troy.Bioapp" + "/" +
            R.raw.comm11,"android.resource://" + "com.troy.Bioapp" + "/" +
            R.raw.comm12 ,"android.resource://" + "com.troy.Bioapp" + "/" +
            R.raw.comm13 ,"android.resource://" + "com.troy.Bioapp" + "/" +
            R.raw.comm14
    };
  public   String lessmid[] = new String[]{"android.resource://" + "com.troy.Bioapp" + "/" +
            R.raw.less1 ,"android.resource://" + "com.troy.Bioapp" + "/" +
            R.raw.less6 ,"android.resource://" + "com.troy.Bioapp" + "/" +
            R.raw.less13 ,"android.resource://" + "com.troy.Bioapp" + "/" +
            R.raw.commmmm1
    };
  public   String moremid[] = new String[]{
            "android.resource://" + "com.troy.Bioapp" + "/" +
                    R.raw.more1,"android.resource://" + "com.troy.Bioapp" + "/" +
            R.raw.more2,"android.resource://" + "com.troy.Bioapp" + "/" +
            R.raw.more8,"android.resource://" + "com.troy.Bioapp"+ "/" +
            R.raw.more9, "android.resource://" + "com.troy.Bioapp" + "/" +
            R.raw.more10,"android.resource://" + "com.troy.Bioapp" + "/" +
            R.raw.commmmm1
    };


    static String getRandom(String[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
     }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference qlocked = mDatabase.getReference().child("users").child(user.getUid());
                        qlocked.child("qlocked").setValue("0");
                        Intent intent = new Intent(Quiz.this, MainActivity.class);
                        startActivity(intent);
                    }
                }).create().show();
    }


    public void savedata(String key , String value){
        SharedPreferences.Editor editor = getSharedPreferences("saveqh", MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();

    }


    public String retrivedata(String key){
        SharedPreferences prefs = getSharedPreferences("saveqh", MODE_PRIVATE);
        String data = prefs.getString(key,null);
        return data;
    }

}
