package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;

import Models.User;

public class register extends AppCompatActivity {

    EditText fnameEDT, lnameEDT, emailEDT,phoneEDT,passwordEDT,confirmPasswordEDT,addressEDT;
    Button regBTN;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    ProgressDialog progressDialog;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        FirebaseApp.initializeApp(this);

        fnameEDT=findViewById(R.id.editText_edit_fname);
        lnameEDT=findViewById(R.id.edit_text_edit_lname);
        emailEDT=findViewById(R.id.editText_edit_email);
        phoneEDT=findViewById(R.id.editText_edit_phone);
        passwordEDT=findViewById(R.id.editText_reg_password);
        confirmPasswordEDT=findViewById(R.id.editText_reg_confirm_password);
        addressEDT=findViewById(R.id.edit_address);


        rootNode=FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);

        regBTN=findViewById(R.id.edit_button);
        regBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fname = fnameEDT.getText().toString().trim();
                String lname = lnameEDT.getText().toString().trim();
                String phone = phoneEDT.getText().toString().trim();
                String email = emailEDT.getText().toString().trim();
                String password = passwordEDT.getText().toString().trim();
                String confirmpassword = confirmPasswordEDT.getText().toString().trim();
                String address=addressEDT.getText().toString().trim();

                boolean isValid = validateUserInputs(fname, lname, phone,email, password, confirmpassword,address);
                if (isValid) {
                    registerUser(fname, lname, phone,email, password,address);
                }
                else
                    return;
            }
        });
    }

    //Validating user inputs
    private boolean validateUserInputs(String fname, String lname, String phone,String email, String password, String confirmpassword, String address) {
        if (email.isEmpty()) {
            emailEDT.setError("required");
            emailEDT.requestFocus();
            return false;
        }
        if (fname.isEmpty()) {
            fnameEDT.setError("required");
            fnameEDT.requestFocus();
            return false;
        }
        if (lname.isEmpty()) {
            lnameEDT.setError("required");
            lnameEDT.requestFocus();
            return false;
        }
        if (address.isEmpty()) {
            addressEDT.setError("required");
            addressEDT.requestFocus();
            return false;
        }
        if (phone.isEmpty()) {
            phoneEDT.setError("Phone number required");
            phoneEDT.requestFocus();
            return false;
        }
        if (password.isEmpty()) {
            passwordEDT.setError("The password cannot be empty");
            passwordEDT.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEDT.setError("Please provide a valid email");
            emailEDT.requestFocus();
            return false;
        }
        if (password.length() < 8) {
            passwordEDT.setError("Min password length is 8");
            passwordEDT.requestFocus();
            return false;
        }
        if (confirmpassword.length() < 8) {
            confirmPasswordEDT.setError("Min password length is 8");
            confirmPasswordEDT.requestFocus();
            return false;
        }
        if (!TextUtils.equals(password, confirmpassword)) {
            Toast.makeText(this, "Passwords don't match.Try again", Toast.LENGTH_LONG).show();
            return false;
        } else
            return true;
    }
    //Registering user
    private void registerUser(String fname, String lname, String phone,String email, String password, String address) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {

                FirebaseUser rUser = mAuth.getCurrentUser();
                assert rUser != null;
                String userId = rUser.getUid();
                User userObj = new User(fname, lname, phone, email,address);
                System.out.println(userObj.toString());

                //Calling the saveUserFunction that saves the information in the firestore
                saveUserInfo(userId,userObj);
                clearUserInputs();


            } else {
                String errMSG = task.getException().getMessage();
                Toast.makeText(this, errMSG, Toast.LENGTH_LONG).show();
            }
        });

    }

    //This method Saves the User information in the firestore
    private void saveUserInfo(String userId, User user){
        //Setting up the Progress dialog
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        //end

        reference=rootNode.getReference("USERS");
        reference.child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()){
                    //end
                    Toast.makeText(register.this, "Registration is successfull", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(register.this, MainActivity.class));
                    finish();
                }
                else{
                    Toast.makeText(register.this, "Failed to Register", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    //Clearing the Edit text inputs
    private void clearUserInputs(){

        emailEDT.setText("");
        fnameEDT.setText("");
        lnameEDT.setText("");
        phoneEDT.setText("");
        emailEDT.setText("");
        passwordEDT.setText("");
        confirmPasswordEDT.setText("");
        addressEDT.setText("");
    }

}