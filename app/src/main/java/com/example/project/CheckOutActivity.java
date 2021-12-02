package com.example.project;

import static Helpers.Constants.BUSINESS_SHORT_CODE;
import static Helpers.Constants.CALLBACKURL;
import static Helpers.Constants.PARTYB;
import static Helpers.Constants.PASSKEY;
import static Helpers.Constants.TRANSACTION_TYPE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import Models.User;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import Models.AccessToken;
import Models.Booking;
import Models.Cart;
import Models.STKPush;
import Models.Service;
import Services.DarajaApiClient;
import Services.DatabaseHelper;
import Services.Utils;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckOutActivity extends AppCompatActivity {
    TextView totalTXT;
    EditText phoneEDT;
    Button makePaymentBTN;

ArrayList<Cart> items;
DatabaseHelper dbHelper;
    private ProgressDialog progressDialog;
    private FirebaseFirestore firestore;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String UserId;
    private String phone;
    private String username;
    private String address;
    private String email;
    private Integer total=0;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    private DarajaApiClient mApiClient;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        initializeViews();


        mProgressDialog = new ProgressDialog(this);
        mApiClient = new DarajaApiClient();
        mApiClient.setIsDebug(true); //Set True to enable logging, false to disable.

        getAccessToken();

        dbHelper=new DatabaseHelper(this);
        progressDialog=new ProgressDialog(this);
        rootNode=FirebaseDatabase.getInstance();
        makePaymentBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone_number=phoneEDT.getText().toString();

                performSTKPush(phone_number,total.toString());
                Booking booking= new Booking(UserId,phone,username,address,items,total.toString());
                booking(booking);
                dbHelper.clearCart();
            }
        });
//Initialising preference
        sharedPreferences=getSharedPreferences("USER_DATA",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        UserId=sharedPreferences.getString("ID","");
        username=sharedPreferences.getString("USERNAME","");
        address=sharedPreferences.getString("ADRESS","");
        phone=sharedPreferences.getString("PHONE","");

        System.out.println("USERNAME: "+sharedPreferences.getString("USERNAME",""));
        Cursor res = dbHelper.getCartItems();

        while (res.moveToNext()){
            items.add(new Cart(res.getString(0),res.getString(1),res.getString(2),res.getString(3),res.getString(4)));
        }

        for (Cart cart: items){
            total+=Integer.parseInt(cart.getPrice());
        }
        totalTXT.setText(total+" KSH");

    }

    private void booking(Booking booking) {
        progressDialog.setMessage("booking.......");
        progressDialog.show();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM yyyy");
        Calendar calendar = Calendar.getInstance();
        String todayDate = simpleDateFormat.format(calendar.getTime());

       reference = rootNode.getReference("BOOKINGS");
       reference.child(UserId).child(reference.getKey())
               .setValue(booking).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                Toast.makeText(CheckOutActivity.this, "Your booking has Been Sent Successfully", Toast.LENGTH_LONG).show();
                Intent intent=new Intent(CheckOutActivity.this, Home.class);
                startActivity(intent);
                finish();
            }
        });

    }
    private boolean validate(String phone){
         if (phone.length()<10)
            return false;
        else
            return true;
    }

private void initializeViews(){
    totalTXT=findViewById(R.id.textView_checkout_totalamount);
    phoneEDT=findViewById(R.id.editText_checkout_mpesa_number);
    makePaymentBTN=findViewById(R.id.button_make_payment);
    items= new ArrayList<>();
}







    public void performSTKPush(String phone_number,String amount) {
        mProgressDialog.setMessage("Processing your request");
        mProgressDialog.setTitle("Please Wait...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
        String timestamp = Utils.getTimestamp();
        STKPush stkPush = new STKPush(
                BUSINESS_SHORT_CODE,
                Utils.getPassword(BUSINESS_SHORT_CODE, PASSKEY, timestamp),
                timestamp,
                TRANSACTION_TYPE,
                String.valueOf(amount),
                Utils.sanitizePhoneNumber(phone_number),
                PARTYB,
                Utils.sanitizePhoneNumber(phone_number),
                CALLBACKURL,
                "MPESA Android Test", //Account reference
                "Testing"  //Transaction description
        );

        mApiClient.setGetAccessToken(false);

        //Sending the data to the Mpesa API, remember to remove the logging when in production.
        mApiClient.mpesaService().sendPush(stkPush).enqueue(new Callback<STKPush>() {
            @Override
            public void onResponse(retrofit2.Call<STKPush> call, Response<STKPush> response) {
                mProgressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        //Timber.d("post submitted to API. %s", response.body());
                    } else {
                        //Timber.e("Response %s", response.errorBody().string());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<STKPush> call, Throwable t) {
                mProgressDialog.dismiss();
                //Timber.e(t);
            }

        });
    }

    public void getAccessToken() {
        mApiClient.setGetAccessToken(true);
        mApiClient.mpesaService().getAccessToken().enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(retrofit2.Call<AccessToken> call, Response<AccessToken> response) {
                if (response.isSuccessful()) {
                    mApiClient.setAuthToken(response.body().accessToken);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<AccessToken> call, Throwable t) {

            }
        });
    }


}




