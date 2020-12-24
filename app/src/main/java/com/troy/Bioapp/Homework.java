package com.troy.Bioapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialog;
import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialogListener;
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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class Homework extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    RecyclerView mPeopleRV;
    private FirebaseRecyclerAdapter<hModel, Homework.NewsViewHolder> mPeopleRVAdapter;
static String fullmarkss;
    static String qnumss;
    ProgressDialog PD;

    float studentmark;
    Float [] arr ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework);

        arr = new Float[0];


        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(false);
        PD.setCanceledOnTouchOutside(false);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        Intent intent=getIntent();
        final String classname = intent.getStringExtra("class");
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child(classname).child("Homework");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
 qnumss = snapshot.child("qnum").getValue(String.class);
 fullmarkss = snapshot.child("fullmark").getValue(String.class);
                TextView fullm = findViewById(R.id.fullmark);
                fullm.setText(String.valueOf(fullmarkss));
if (qnumss != null || fullmarkss !=null){
    int qnum = Integer.parseInt(qnumss);
    arr = new Float[qnum];
}else {
    Intent intent = new Intent(Homework.this, MainActivity.class);
    startActivity(intent);
}


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });



        mPeopleRV = findViewById(R.id.newsrecyclar);




        Button delete = findViewById(R.id.clear);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new AlertDialog.Builder(Homework.this)
                        .setTitle("Really Delete?")
                        .setMessage("Are you sure you want to delete all data?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                Toast.makeText(Homework.this, "Deleted", Toast.LENGTH_LONG).show();
                                SharedPreferences.Editor editor = getSharedPreferences("saveq", MODE_PRIVATE).edit();
                                editor.clear();
                                editor.apply();
                            }
                        }).create().show();
            }
        });


        DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child(classname).child("Homework").child("Test");
        Query personsQuery = personsRef.orderByKey();
        mPeopleRV.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<hModel>().setQuery(personsQuery, hModel.class).build();
        mPeopleRVAdapter = new FirebaseRecyclerAdapter<hModel, Homework.NewsViewHolder>(personsOptions) {
            @Override
            protected void onBindViewHolder(final Homework.NewsViewHolder holder, final int position, final hModel model) {
                holder.seta(model.getA());
                holder.setb(model.getB());
                holder.setc(model.getC());
                holder.setd(model.getD());
                holder.setimage(model.getImage());
                holder.setQ(model.getQ());
                holder.qn(model.getId());
                holder.answercheck(model.getAn(),Float.parseFloat(fullmarkss),Integer.parseInt(qnumss),model.getId(),retrivedata(model.getId()));


            }

            @Override
            public Homework.NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.hpost, parent, false);

                return new Homework.NewsViewHolder(view);
            }
        };

        mPeopleRV.setAdapter(mPeopleRVAdapter);
        mPeopleRV.addItemDecoration(new DividerItemDecoration(this, 0));


        Button clickButton = (Button) findViewById(R.id.finishs);
        clickButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date d = new Date();
                CharSequence s  = DateFormat.format("MMMM d, yyyy ", d.getTime());
                s7s7();
                TextView fullm = findViewById(R.id.fullmark);
                yaaaa(studentmark,fullm.getText().toString());
                FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
                DatabaseReference submit = mDatabase.getReference().child("users").child(user.getUid());
                submit.child("HWsubmit").setValue(String.valueOf(s)+" | "+String.valueOf(studentmark));

            }
        });

    }

    public void back(View view) {
        Intent intent = new Intent(Homework.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        PD.show();

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                PD.dismiss();
                PD.cancel();
                PD.hide();
            }
        }, 2000);


        super.onStart();
        mPeopleRVAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPeopleRVAdapter.stopListening();
    }


    public void addmark(int keyss, Float value){
        arr[keyss] = value;
    }



    public class NewsViewHolder extends RecyclerView.ViewHolder {
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

        public void answercheck(final String data, Float fullmark , int qnum , final String q , String answershared ){
            final Float qmark = fullmark / qnum ;
         //   Toast.makeText(Homework.this , "qmark = "+String.valueOf(qmark),Toast.LENGTH_SHORT).show();
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
                        addmark(Integer.parseInt(q)-1,qmark);
                    }else {
                        addmark(Integer.parseInt(q)-1,Float.parseFloat("0"));
                    }
                    viewToBitmap(Integer.parseInt(q));
                    savedata(q,A);

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

                        addmark(Integer.parseInt(q)-1, qmark);
                    }else {
                        addmark(Integer.parseInt(q)-1,Float.parseFloat("0"));
                    }
                    viewToBitmap(Integer.parseInt(q));
                    savedata(q,B);
                }
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

                        addmark(Integer.parseInt(q)-1, qmark);
                    }else {
                        addmark(Integer.parseInt(q)-1,Float.parseFloat("0"));
                    }
                    viewToBitmap(Integer.parseInt(q));
                    savedata(q,C);
                }
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

                        addmark(Integer.parseInt(q)-1, qmark);
                    }else {
                        addmark(Integer.parseInt(q)-1,Float.parseFloat("0"));
                    }
                    viewToBitmap(Integer.parseInt(q));
                    savedata(q,D);
                }
            });

if (answershared != null) {
    y.setBackgroundResource(R.drawable.f);
    if (answershared.equals("a")) {
        textView1.setBackgroundResource(R.drawable.rb);
        textView2.setBackgroundResource(R.drawable.rc);
        textView3.setBackgroundResource(R.drawable.rc);
        textView4.setBackgroundResource(R.drawable.rc);
        if (A.equals(data)) {

                arr[Integer.parseInt(q)-1] = qmark;

        } else {
            arr[Integer.parseInt(q)-1] = Float.parseFloat("0");
        }
        viewToBitmap(Integer.parseInt(q));

    }
    if (answershared.equals("b")) {
        textView1.setBackgroundResource(R.drawable.rc);
        textView2.setBackgroundResource(R.drawable.rb);
        textView3.setBackgroundResource(R.drawable.rc);
        textView4.setBackgroundResource(R.drawable.rc);
        if (B.equals(data)) {

                arr[Integer.parseInt(q)-1] = qmark;
       } else {
            arr[Integer.parseInt(q)-1] = Float.parseFloat("0");
        }
        viewToBitmap(Integer.parseInt(q));

    }
    if (answershared.equals("c")) {
        textView1.setBackgroundResource(R.drawable.rc);
        textView2.setBackgroundResource(R.drawable.rc);
        textView3.setBackgroundResource(R.drawable.rb);
        textView4.setBackgroundResource(R.drawable.rc);
        if (C.equals(data)) {

                arr[Integer.parseInt(q)-1] = qmark;
        } else {
            arr[Integer.parseInt(q)-1] = Float.parseFloat("0");
        }
        viewToBitmap(Integer.parseInt(q));

    }
    if (answershared.equals("d")) {
        textView1.setBackgroundResource(R.drawable.rc);
        textView2.setBackgroundResource(R.drawable.rc);
        textView3.setBackgroundResource(R.drawable.rc);
        textView4.setBackgroundResource(R.drawable.rb);
        if (D.equals(data)) {
                arr[Integer.parseInt(q)-1] = qmark;
        } else {
            arr[Integer.parseInt(q)-1] = Float.parseFloat("0");
        }
        viewToBitmap(Integer.parseInt(q));

    }
}else {
    y.setBackgroundResource(R.drawable.xx);
}

        }


        public void viewToBitmap(int c) {
            LinearLayout view = mView.findViewById(R.id.ss);
            try {

                File cachePath = new File(getExternalCacheDir(), "images/HWimages");
                cachePath.mkdirs();
if (view.getWidth() > 0 || view.getHeight() > 0){
    Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(returnedBitmap);
    Drawable bgDrawable = view.getBackground();
    if (bgDrawable != null)
        bgDrawable.draw(canvas);
    else
        canvas.drawColor(Color.WHITE);
    view.draw(canvas);
    FileOutputStream stream = new FileOutputStream(cachePath + "/" + c + "image.png"); // overwrites this image every time
    returnedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
    stream.close();
}
else {

    PD.show();

    new Handler().postDelayed(new Runnable(){
        @Override
        public void run() {
            PD.dismiss();
            PD.cancel();
            PD.hide();
        }
    }, 1000);

}

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }









public void savedata(String key , String value){
    SharedPreferences.Editor editor = getSharedPreferences("saveq", MODE_PRIVATE).edit();
    editor.putString(key, value);
    editor.apply();

}


public String retrivedata(String key){
    SharedPreferences prefs = getSharedPreferences("saveq", MODE_PRIVATE);
    String data = prefs.getString(key,null);
    return data;
    }

















    public void yaaaa(float studentmarks, final String fullmark){
        if (String.valueOf(studentmarks).equals(fullmark)){
            new TTFancyGifDialog.Builder(Homework.this)
                    .setTitle("You got "+ studentmarks +",The full marks")
                    .setMessage("Try to maintain your academic level")
                    .setPositiveBtnText("Ok, I will")
                    .setPositiveBtnBackground("#3490dc")
                    .setGifResource(R.drawable.fullma)
                    .isCancellable(false)
                    .OnPositiveClicked(new TTFancyGifDialogListener() {
                        @Override
                        public void OnClick() {
                            Intent intent = new Intent(Homework.this, descion.class);
                            intent.putExtra("arrnum",String.valueOf(arr.length));
                            startActivity(intent);
                         }
                    })
                    .build();

        }
        Float midmark = Float.parseFloat(fullmark)/2;
        if (studentmarks >= midmark && studentmarks < Float.parseFloat(fullmark)){

            new TTFancyGifDialog.Builder(Homework.this)
                    .setTitle("You Got "+studentmarks+", You passed the exam")
                    .setMessage("Try Your best next time to get the full marks")
                    .setPositiveBtnText("Ok, I will")
                    .setPositiveBtnBackground("#3490dc")
                    .setGifResource(R.drawable.happyma)
                    .isCancellable(false)
                    .OnPositiveClicked(new TTFancyGifDialogListener() {
                        @Override
                        public void OnClick() {
                            Intent intent = new Intent(Homework.this, descion.class);
                            intent.putExtra("arrnum",String.valueOf(arr.length));
                            startActivity(intent);
                        }
                    })
                    .build();
        }
        if (studentmarks < midmark){

            new TTFancyGifDialog.Builder(Homework.this)
                    .setTitle("You Fail" +", You Got "+studentmarks )
                    .setMessage("Try Your best next time to pass the exam and get the full marks")
                    .setPositiveBtnText("Ok, I will")
                    .setPositiveBtnBackground("#3490dc")
                    .setGifResource(R.drawable.sadma)      //pass your gif, png or jpg
                    .isCancellable(false)
                    .OnPositiveClicked(new TTFancyGifDialogListener() {
                        @Override
                        public void OnClick() {
                            Intent intent = new Intent(Homework.this, descion.class);
                            intent.putExtra("arrnum",String.valueOf(arr.length));
                            startActivity(intent);
                        }
                    })
                    .build();
        }
    }

    private void s7s7(){
        for (int i = 0 ;i<arr.length ;i++) {
            if (arr[i] != null) {
                studentmark = studentmark + arr[i];
            } else {
                studentmark = studentmark + 0;
            }
        }
    }


}