package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Models.Cart;
import Services.DatabaseHelper;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartRemoveItemClickListener {
    private FirebaseFirestore firestore;
    //Preference field
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    Button checkOutBTN;
    DatabaseHelper _dbHelper;
    RecyclerView rv_cartItems;
    ArrayList<Cart> _cartItems;
    CartAdapter adapter;
    TextView totalTXT;
    Integer total=0;
    String UserId;
    private LinearLayoutManager cartItemsLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        _cartItems=new ArrayList<>();
        _dbHelper=new DatabaseHelper(this);
        rv_cartItems=findViewById(R.id.rv_cart_items);
        totalTXT=findViewById(R.id.textView_total);
        checkOutBTN=findViewById(R.id.button_checkOut);

        firestore = FirebaseFirestore.getInstance();
        //Initialising preference
        sharedPreferences=getSharedPreferences("USER_DATA",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        UserId=sharedPreferences.getString("USER_ID","");

        Cursor res=_dbHelper.getCartItems();
        _cartItems.clear();
        if (res.getCount()==0){
            Toast.makeText(this,"No Cart Items",Toast.LENGTH_SHORT).show();
        }
        while(res.moveToNext()) {
            _cartItems.add(new Cart(res.getString(0),res.getString(1),res.getString(2),res.getString(3), res.getString(4)));
        }
        for (Cart item: _cartItems){
           // total +=Integer.parseInt(item.getPrice());
        }
        //totalTXT.setText("Total Amount: "+total+" KSH");

        //Setting the Recycler View Adapter
        cartItemsLayoutManager=new LinearLayoutManager(this);
        adapter=new CartAdapter(_cartItems,this);
        rv_cartItems.setLayoutManager(cartItemsLayoutManager);
        rv_cartItems.setAdapter(adapter);
        //End

        checkOutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res=_dbHelper.getCartItems();
                if (res.getCount()==0){
                    Toast.makeText(CartActivity.this,"Cart Is Empty. Add Items First Before Check Out",Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent=new Intent(CartActivity.this, CheckOutActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        total=0;
        _cartItems.clear();
        Cursor res=_dbHelper.getCartItems();
        if (res.getCount()==0){
            return;
            //Toast.makeText(this,"No Cart Items",Toast.LENGTH_SHORT).show();
        }
        while(res.moveToNext()) {
            _cartItems.add(new Cart(res.getString(0),res.getString(1),res.getString(2),res.getString(3),res.getString(4)));

        }

        for (Cart item: _cartItems){
           // total +=Integer.parseInt(item.getPrice());
        }
       // totalTXT.setText("Total Amount: "+total+" KSH");

        adapter=new CartAdapter(_cartItems,this);
        rv_cartItems.setAdapter(adapter);

    }

    @Override
    public void onCartRemoveItemClick(Cart cartItem) {
        System.out.println("Remove Button Has Been Clicked");
        _dbHelper.deleteCartItem(cartItem.getUrl());
        Toast.makeText(CartActivity.this, cartItem.getName()+" is removed from Cart",Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(CartActivity.this, CartActivity.class);
        startActivity(intent);
        finish();

    }
}