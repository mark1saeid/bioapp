package com.troy.Bioapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class descion extends AppCompatActivity {
    FrameLayout button1,button2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descion);




        button1 =(FrameLayout)findViewById(R.id.button11);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentsss = new Intent (descion.this, HWanswer.class);
                startActivity(intentsss);
            }
        });





        button2 =(FrameLayout)findViewById(R.id.button21);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    createPdf();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }

                Date d = new Date();
                CharSequence s = DateFormat.format("MMMM d, yyyy ", d.getTime());

                File files = new File(getExternalCacheDir(), "hw");
                File newFile = new File(files, s + "hw.pdf");

                String sss = newFile.toString();

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                Uri screenshotUri = Uri.parse(sss);
                sharingIntent.setType("*/*");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                startActivity(Intent.createChooser(sharingIntent, "Share HomeWork using"));


            }});

    }
    public void back(View view) {
        Intent intent = new Intent (descion.this, MainActivity.class);
        startActivity(intent);
    }




    public void createPdf() throws IOException, DocumentException {
Toast.makeText(descion.this , "Loading... Wait",Toast.LENGTH_LONG).show();

        final String arrnum = getIntent().getStringExtra("arrnum");



        Date d = new Date();
        CharSequence s  = DateFormat.format("MMMM d, yyyy ", d.getTime());
        File files = new File(this.getExternalCacheDir(), "hw");
        files.mkdirs();
        File file = new File(files, s+"hw.pdf");
        if (!file.exists()) {
            file.createNewFile();
        }
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(file.getAbsoluteFile()));
        document.open();
        for (int i = 0; i<= Integer.parseInt(arrnum); i++) {
            File filesss = new File(this.getExternalCacheDir()+ "/images/HWimages/" + i + "image.png");
            try {
                FileInputStream ims = new FileInputStream(filesss);
                Bitmap bmp = BitmapFactory.decodeStream(ims);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                Image image = Image.getInstance(stream.toByteArray());
                image.scalePercent(30);
                image.setAlignment(Element.ALIGN_LEFT);
                document.add(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        document.close();
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(descion.this, MainActivity.class);
                        startActivity(intent);
                    }
                }).create().show();
    }


}
