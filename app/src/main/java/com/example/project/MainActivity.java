package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.UUID;

import Models.User;

public class MainActivity extends AppCompatActivity {

    EditText emailEDT, passwordEDT;

    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    TextView signUpTXT,forgotPasswordEDT;
    Button loginBTN;
    FirebaseFirestore firestore;

    FirebaseDatabase rootNode;
    Task<DataSnapshot> reference;
ArrayList<User> users;
    //Preference field
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signUpTXT=findViewById(R.id.text_view_signup);
        loginBTN=findViewById(R.id.button_login);
        emailEDT=findViewById(R.id.edit_text_email);
        passwordEDT=findViewById(R.id.edit_text_password);
        forgotPasswordEDT=findViewById(R.id.text_view_forgot_password);
        FirebaseApp.initializeApp(this);
        rootNode=FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        progressDialog=new ProgressDialog(this);

        //Initialising preference
        sharedPreferences=getSharedPreferences("USER_DATA",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        users=new ArrayList<>();

        signUpTXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(getApplicationContext(), register.class);
                startActivity(intent);
            }
        });
        loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Validating User inputs

                String email = emailEDT.getText().toString().trim();
                String password = passwordEDT.getText().toString().trim();
                if (email.isEmpty()) {
                    emailEDT.setError("Required");
                    emailEDT.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailEDT.setError("Please, provide a valid email");
                    emailEDT.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    passwordEDT.setError("Required");
                    passwordEDT.requestFocus();
                    return;
                }

                else
                    loginUser(email,password);

            }
        });
        forgotPasswordEDT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=emailEDT.getText().toString();
                if(email.isEmpty()) {
                    emailEDT.setError("Required");
                }
                else{
                    passwordEDT.setText("");
                    progressDialog.setMessage("Please wait...");
                    progressDialog.show();
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()){
                                Toast.makeText(MainActivity.this,"An Email has been sent for Password Reset",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                String errorMsg=task.getException().getMessage();
                                Toast.makeText(MainActivity.this,errorMsg,Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void loginUser(String email, String password) {

        //Setting up the progress Dialog
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {

                    String Uid=mAuth.getCurrentUser().getUid();


                   System.out.println("USER ID"+Uid);

                    reference=rootNode.getReference("USERS").child(Uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                         User u= task.getResult().getValue(User.class);

                            String username=u.getFname()+", "+u.getLname();
                            editor.putString("USERNAME", username);
                            editor.putString("PHONE",u.getPhoneNo());
                            editor.putString("EMAIL",u.getEmail());
                            editor.putString("FNAME",u.getFname());
                            editor.putString("LNAME",u.getLname());
                            editor.putString("ADRESS",u.getAddress());
                            editor.putString("ID",Uid);
                            editor.putString("REGDATE",u.getRegistrationDate());
                            editor.commit();
                            System.out.println("USER "+u.toString());
                        }
                    });

                    //registering a Shared Preference
                    editor.putBoolean("LOGGED_IN_STATUS",true);
                    editor.putString("USER_ID",Uid);
                    editor.commit();

                    //starting the main activity intent

                    Intent intent = new Intent(MainActivity.this, Home.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {

                    //Registering a shared preference
                    editor.putBoolean("LOGGED_IN_STATUS",false);
                    editor.commit();
                    Toast.makeText(MainActivity.this, "Email or Password is Incorrect", Toast.LENGTH_LONG).show();
                }
            }
        });
     }
}