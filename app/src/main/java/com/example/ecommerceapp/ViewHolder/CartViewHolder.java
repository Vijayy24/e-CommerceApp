package com.example.ecommerceapp.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.Interface.ItemClickListner;
import com.example.ecommerceapp.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView textProductNmae ,textProductprice,textProductQuantity;
    private ItemClickListner itemClickListner;

    public CartViewHolder(@NonNull View itemView)
    {
        super(itemView);
        textProductNmae  = itemView.findViewById(R.id.car_product_Name);
        textProductprice  = itemView.findViewById(R.id.cart_product_price);
        textProductQuantity  = itemView.findViewById(R.id.cart_product_quantity);
    }


    @Override
    public void onClick(View view) {
        itemClickListner.onClick(view,getAdapterPosition(), false);

    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}
