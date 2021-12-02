package com.example.project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import Models.Booking;
import Models.Cart;
import Models.Service;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {
    private OnBookingClickListener onBookingClickListener;
    private ArrayList<Booking> bookings;
    public BookingAdapter(ArrayList<Booking> bookings, OnBookingClickListener onBookingClickListener){
        this.bookings = bookings;
        this.onBookingClickListener = onBookingClickListener;
    }

    @Override
    public int getItemCount()
    {
        return bookings.size();
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booked_template,parent,false);
        return new BookingViewHolder(view, onBookingClickListener);

    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        holder.bind(bookings.get(position));
    }


    public class BookingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        OnBookingClickListener onBookingClickListener;

        TextView  Price, Date, Time;


        public BookingViewHolder(@NonNull View itemView, OnBookingClickListener onBookingClickListener) {
            super(itemView);
            Price= itemView.findViewById(R.id.booked_price);
            Date=itemView.findViewById(R.id.booked_day);
            Time=itemView.findViewById(R.id.booked_time);
            this.onBookingClickListener = onBookingClickListener;

        }
        public void bind(Booking booking){

            Price.setText(String.format("%s Ksh", booking.getTotalAmount()));
            Time.setText(booking.getTime());
            Cart cart=booking.getCartItems().get(0);
            Date.setText(booking.getDate());
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onBookingClickListener.OnBookingItemClick(bookings.get(getAdapterPosition()));
        }
    }
    public interface OnBookingClickListener {
        void OnBookingItemClick(Booking booking);
    }
}
