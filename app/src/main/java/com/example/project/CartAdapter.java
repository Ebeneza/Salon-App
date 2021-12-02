package com.example.project;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import java.util.ArrayList;

import Models.Cart;
import Services.DatabaseHelper;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartItemViewHolder> {

    private  OnCartRemoveItemClickListener onCartRemoveItemClickListener;
    private ArrayList<Cart>cartItems;
    public CartAdapter(ArrayList<Cart> cartItems, OnCartRemoveItemClickListener onCartRemoveItemClickListener){
        this.cartItems = cartItems;
        this.onCartRemoveItemClickListener=onCartRemoveItemClickListener;
    }

    @Override
    public int getItemCount()
    {
        return cartItems.size();
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_book_item_template,parent,false);
        return new CartItemViewHolder(view, onCartRemoveItemClickListener);

    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        holder.bind(cartItems.get(position));
    }


    public class CartItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        OnCartRemoveItemClickListener onCartRemoveItemClickListener;
        DatabaseHelper _dbHelper;
        ImageView Url;
        TextView Name, Amount;
        ImageButton removeItemBTN;
        public CartItemViewHolder(@NonNull View itemView, OnCartRemoveItemClickListener onCartRemoveItemClickListener) {
            super(itemView);
            _dbHelper=new DatabaseHelper(itemView.getContext());
            Name=itemView.findViewById(R.id.textView_cart_view_name);
            Url = itemView.findViewById(R.id.image_view_cart_view_img);
            Amount=itemView.findViewById(R.id.textView_cart_view_price);
            removeItemBTN=itemView.findViewById(R.id.imageButton_cart_view_remove);
            this.onCartRemoveItemClickListener=onCartRemoveItemClickListener;

        }
        public void bind(Cart cartItem){
            Name.setText(""+cartItem.getName());
            Glide.with(itemView).load(cartItem.getUrl()).into(Url);
            Amount.setText("Total Amount: "+cartItem.getPrice()+" KSH");
            removeItemBTN.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onCartRemoveItemClickListener.onCartRemoveItemClick(cartItems.get(getAdapterPosition()));
        }
    }
    public interface OnCartRemoveItemClickListener {
        void onCartRemoveItemClick(Cart cartItem);
    }

}