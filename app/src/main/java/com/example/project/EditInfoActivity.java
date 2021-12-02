package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import Models.User;
import Services.DatabaseHelper;

public class EditInfoActivity extends AppCompatActivity {
    EditText fnameEDT, lnameEDT, emailEDT,phoneEDT,addressEDT;
    Button UpdateBTN;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    ProgressDialog progressDialog;
    DatabaseHelper dbHelper;
    //Preference field
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);

        setContentView(R.layout.activity_edit_info);
        fnameEDT = findViewById(R.id.editText_edit_fname);
        lnameEDT = findViewById(R.id.edit_text_edit_lname);
        emailEDT = findViewById(R.id.editText_edit_email);
        phoneEDT = findViewById(R.id.editText_edit_phone);
        addressEDT = findViewById(R.id.edit_address);
        rootNode = FirebaseDatabase.getInstance();
        dbHelper = new DatabaseHelper(this);

        //Initialising preference
        sharedPreferences = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String Fname=sharedPreferences.getString("FNAME","");
        String Lname=sharedPreferences.getString("LNAME","");
        String Adress=sharedPreferences.getString("ADRESS","");
        String Phone=sharedPreferences.getString("PHONE","");
        String Email=sharedPreferences.getString("EMAIL","");

        System.out.println(Fname);
        System.out.println(Lname);
        fnameEDT.setText(""+Fname);
        lnameEDT.setText(""+Lname);
        phoneEDT.setText(""+Phone);
        addressEDT.setText(""+Adress);
        emailEDT.setText(""+Email);

        progressDialog = new ProgressDialog(this);
        UpdateBTN = findViewById(R.id.edit_button);
        UpdateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fname = fnameEDT.getText().toString().trim();
                String lname = lnameEDT.getText().toString().trim();
                String phone = phoneEDT.getText().toString().trim();
                String email = emailEDT.getText().toString().trim();
                String address = addressEDT.getText().toString().trim();
                if (fname.length() < 3) {
                    fnameEDT.setError("Too short");
                    return;
                }
                if (lname.length() < 3) {
                    lnameEDT.setError("Too short");
                    return;
                }
                if (phone.length() < 10 || phone.length() > 13) {
                    phoneEDT.setError("Invalid Number");
                    return;
                }

                progressDialog.setMessage("Updating Account Info...");
                progressDialog.show();
                String uid = sharedPreferences.getString("ID", "");
                User user = new User(fname, lname, phone, email, address);

                reference = rootNode.getReference("USERS");
                reference.child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(EditInfoActivity.this, "User Info is Updated", Toast.LENGTH_SHORT).show();
                            editor.putString("PHONE", phone);
                            String username = "" + fname + ", " + lname;
                            editor.putString("USERNAME", username);
                            editor.putString("EMAIL",email);
                            editor.putString("ADRESS",address);
                            editor.putString("FNAME", fname);
                            editor.putString("LNAME", lname);
                            editor.commit();

                            fnameEDT.setText(fname);
                            lnameEDT.setText(lname);
                            phoneEDT.setText(phone);
                            emailEDT.setText(email);
                            addressEDT.setText(address);
                            Intent intent=new Intent(EditInfoActivity.this,Home.class);
                            startActivity(intent);
                            finish();

                        } else
                            System.out.println("Imekata ku Update");

                    }


                });

            }


        });

    }
}