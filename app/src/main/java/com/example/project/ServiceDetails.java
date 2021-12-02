package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import Models.Cart;
import Services.DatabaseHelper;

public class ServiceDetails extends AppCompatActivity {
    TextView textView_p_name,textView_p_price,textView_p_description,text_view_ptitle,textView,textView3;
    Button button_p_add_to_cart,button_p_order_now;
    ImageView image_view_cart_view_img;
    Spinner spinner4;
     DatePicker datepicker;
    ArrayList<String> days, hours;
    Cart cart;
    private DatabaseHelper _dBHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_details);
        textView_p_name=findViewById(R.id.textView_p_name);
        textView_p_price=findViewById(R.id.textView_p_price);
        textView_p_description=findViewById(R.id.textView_p_description);
        text_view_ptitle=findViewById(R.id.text_view_ptitle);
        button_p_add_to_cart=findViewById(R.id.editInfo);
        button_p_order_now=findViewById(R.id.Logout);
        image_view_cart_view_img=findViewById(R.id.image_view_cart_view_img);
        spinner4=findViewById(R.id.spinner4);
        datepicker=findViewById(R.id.spinner3);
        datepicker.setMinDate(System.currentTimeMillis()-1000);

        textView=findViewById(R.id.textView);
        textView3=findViewById(R.id.textView3);
        _dBHelper= new DatabaseHelper(this);





String dateSelected = datepicker.getDayOfMonth() + "-" + datepicker.getMonth() + "-" + datepicker.getYear();

        Bundle bundle=getIntent().getExtras();
        String name=bundle.getString("NAME","");
        String description=bundle.getString("DESCRIPTION","");
        String price=bundle.getString("PRICE","");
        String image=bundle.getString("IMAGE","");
        textView_p_name.setText(name);
        textView_p_price.setText(String.format("%s Ksh", price));
        Glide.with(this).load(image).into(image_view_cart_view_img);
        textView_p_description.setText(description);

            hours=new ArrayList<>();
        hours.add("8:00 Am - 9:00 AM");
        hours.add("9:00 Am - 10:00 AM");
        hours.add("10:00 Am - 11:00 AM");
        hours.add("11:00 Am - 12:00 PM");
        hours.add("12:00 Pm - 1:00 PM");
        hours.add("1:00 Pm - 2:00 PM");

ArrayAdapter<String> adapter=new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,hours);
spinner4.setAdapter(adapter);


String time=spinner4.getSelectedItem().toString();
cart=new Cart(name,dateSelected,time,price,image);


        button_p_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res=_dBHelper.getCartItem(image);
                if (res.getCount()==0){
                    boolean isInserted= _dBHelper.insertCartItem(name,dateSelected,time,price,image);
                    if (isInserted){
                        Toast.makeText(ServiceDetails.this,name+ " Has Been Added To Cart",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(ServiceDetails.this,CartActivity.class);
                        System.out.println("IMEFANYA VIZURI");
                        startActivity(intent);
                        finish();
                    }
                }else   {

                    boolean isUpdated=_dBHelper.updateCartItem(name,dateSelected,time,price,image);
                    if (isUpdated)
                        Toast.makeText(ServiceDetails.this,name+" is updated to Cart",Toast.LENGTH_SHORT).show();
                }

            }
        });
        button_p_order_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res=_dBHelper.getCartItem(image);
                if (res.getCount()==0){
                    boolean isInserted= _dBHelper.insertCartItem(name,dateSelected,time,price,image);
                    if (isInserted){
                        Toast.makeText(ServiceDetails.this,name+ " Has Been Added To Cart",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(ServiceDetails.this,CheckOutActivity.class);
                        System.out.println("IMEFANYA VIZURI");
                        startActivity(intent);
                        finish();
                    }
                }else   {
                    boolean isUpdated=_dBHelper.updateCartItem(name,dateSelected,time,price,image);
                    if (isUpdated){
                        Toast.makeText(ServiceDetails.this,name+" is updated to Cart",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(ServiceDetails.this,Home.class);
                        System.out.println("IMEFANYA VIZURI");
                        startActivity(intent);
                        finish();
                    }

                }
            }
        });





    }
}