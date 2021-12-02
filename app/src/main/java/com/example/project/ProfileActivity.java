package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import Models.Cart;
import Services.DatabaseHelper;

public class ProfileActivity<profileActivity> extends AppCompatActivity {
    TextView usernameTXT, emailTXT,phoneTXT,addressTXT;
    Button editInfo,Logout,myBookings;



    ArrayList<Cart> items;
    DatabaseHelper dbHelper;
    private ProgressDialog progressDialog;
    private FirebaseFirestore firestore;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String UserId;
    private String phone;
    private String address;
    private String username;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        usernameTXT=findViewById(R.id.textView_checkout_username);
        emailTXT=findViewById(R.id.textView_checkout_email);
        phoneTXT=findViewById(R.id.textView_checkout_phone);
        addressTXT=findViewById(R.id.checkout_address);
        editInfo=findViewById(R.id.editInfo);
        Logout=findViewById(R.id.Logout);
        items= new ArrayList<>();
        dbHelper=new DatabaseHelper(this);
        progressDialog=new ProgressDialog(this);
        firestore = FirebaseFirestore.getInstance();
        myBookings=findViewById(R.id.myBookings);
        myBookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(ProfileActivity.this, MyBookings.class);
                startActivity(intent);

            }
        });
        editInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(ProfileActivity.this, EditInfoActivity.class);
                startActivity(intent);

            }
        });
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
               editor.clear();
                editor.commit();

            }
        });
        //Initialising preference
        sharedPreferences=getSharedPreferences("USER_DATA",MODE_PRIVATE);
        editor=sharedPreferences.edit();


        System.out.println("USERNAME: "+sharedPreferences.getString("USERNAME",""));
        usernameTXT.setText(""+sharedPreferences.getString("USERNAME",""));
        phoneTXT.setText(""+sharedPreferences.getString("PHONE",""));
        emailTXT.setText(""+sharedPreferences.getString("EMAIL",""));
        addressTXT.setText(""+sharedPreferences.getString("ADRESS",""));

    }
}