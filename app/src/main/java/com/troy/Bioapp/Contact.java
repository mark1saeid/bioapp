package com.troy.Bioapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Contact extends Fragment {
    public Contact() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    Button button;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_contact, container, false);

        setHasOptionsMenu(true);


        button = (Button) view.findViewById(R.id.send);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail(button);
                Toast.makeText(getActivity().getApplicationContext(), "choose", Toast.LENGTH_LONG).show();//display the text of button1
            }
        });

        return view;


    }












    public void sendEmail(View button){
        final EditText formName=(EditText) view.findViewById(R.id.formName);
        String clientName=formName.getText().toString();
        final EditText formEmail=(EditText) view.findViewById(R.id.formEmail);
        String clientEmail=formEmail.getText().toString();
        final EditText formDetails=(EditText) view.findViewById(R.id.formDetails);
        String clientDetails=formDetails.getText().toString();

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL,new String[]{"koracorner1@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "New Private Service Request");
        i.putExtra(Intent.EXTRA_TEXT, "TODO: compose message body");
        try {
            startActivity(Intent.createChooser(i, "Send email with...?"));
        } catch (android.content.ActivityNotFoundException exception) {
            Toast.makeText(Contact.this.getActivity(), "No email clients installed on device!", Toast.LENGTH_LONG).show();
        }




    }}