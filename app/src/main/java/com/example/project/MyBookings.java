package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import Models.Booking;

public class MyBookings extends AppCompatActivity {
    RecyclerView bookings_recycler;
    ArrayList<Booking> bookings;
    ProgressDialog progressDialog;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    //Preference field
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);
        bookings_recycler=findViewById(R.id.bookings_recycler);
        rootNode=FirebaseDatabase.getInstance();

        //Initialising preference
        sharedPreferences=getSharedPreferences("USER_DATA",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        String UserId=sharedPreferences.getString("ID","");



    }
}