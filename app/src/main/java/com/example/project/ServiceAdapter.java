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

import Models.Service;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {
    private OnServiceClickListener onServiceClickListener;
    private ArrayList<Service> services;
    public ServiceAdapter(ArrayList<Service> services, OnServiceClickListener onServiceClickListener){
        this.services = services;
        this.onServiceClickListener = onServiceClickListener;
    }

    @Override
    public int getItemCount()
    {
        return services.size();
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_service_template,parent,false);
        return new ServiceViewHolder(view, onServiceClickListener);

    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        holder.bind(services.get(position));
    }


    public class ServiceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        OnServiceClickListener onServiceClickListener;

        ImageView Icon;
        TextView Title, Price;

        public ServiceViewHolder(@NonNull View itemView, OnServiceClickListener onServiceClickListener) {
            super(itemView);
            Icon= itemView.findViewById(R.id.image_view_cart_view_img);
            Title= itemView.findViewById(R.id.text_view_service_name);
            Price= itemView.findViewById(R.id.text_view_service_price);
            this.onServiceClickListener = onServiceClickListener;

        }
        public void bind(Service service){
            Glide.with(itemView).load(service.getImage()).into(Icon);
            Title.setText(service.getName());
            Price.setText(String.format("%s Ksh", service.getPrice()));
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onServiceClickListener.OnServiceItemClick(services.get(getAdapterPosition()));
        }
    }
    public interface OnServiceClickListener {
        void OnServiceItemClick(Service category);
    }
}