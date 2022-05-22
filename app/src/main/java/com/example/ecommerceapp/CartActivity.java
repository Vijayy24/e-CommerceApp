package com.example.ecommerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerceapp.Model.Cart;
import com.example.ecommerceapp.Prevalent.Prevalent;
import com.example.ecommerceapp.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button NextProcessBtn;
    private TextView txtTotalAmount,textMsg1;

    private int overTotalPrice = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);




        NextProcessBtn = findViewById(R.id.next_process_btn);
        txtTotalAmount = findViewById(R.id.total_price);
        textMsg1= findViewById(R.id.msg1);

        NextProcessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                txtTotalAmount.setText("Total Price = Rs."+String.valueOf(overTotalPrice));

                Intent intent = new Intent(CartActivity.this,ConfirmFinalOrderActivity.class);
                intent.putExtra("Total Price",String.valueOf(overTotalPrice));
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        CheckOrederState();

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                    .setQuery(cartListRef.child("User View")
                        .child(Prevalent.currentOnlineUser.getPhone()).child("Products"),Cart.class)
                        .build();

        FirebaseRecyclerAdapter<Cart,CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
              View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
              CartViewHolder holder = new CartViewHolder(view);
              return  holder;
            }


            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model)
            {
                holder. textProductNmae.setText(model.getPname());
                holder.textProductprice.setText("Price :"+model.getPrice()+".Rs");


                int oneTyperProdcutTPrice = ((Integer.valueOf(model.getPrice())));
                overTotalPrice = overTotalPrice +oneTyperProdcutTPrice;
                //holder.textProductQuantity.setText(model.getquantity)

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence options[] = new CharSequence[]
                                {
                                  "Edit" ,
                                  "Remove"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Option:");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == 0)
                                {
                                    Intent intent = new Intent(CartActivity.this,ProductDetailsActivity.class);
                                    intent .putExtra("pid",model.getPid());
                                    startActivity(intent);
                                }
                                if (i == 1)
                                {
                                    cartListRef.child("User View")
                                            .child(Prevalent.currentOnlineUser.getPhone())
                                            .child("Products")
                                            .child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task)
                                                {
                                                   if (task.isSuccessful())
                                                   {
                                                       Toast.makeText(CartActivity.this, "Item removed successfully.", Toast.LENGTH_SHORT).show();
                                                       Intent intent = new Intent(CartActivity.this,HomeActivity.class);
                                                       startActivity(intent);
                                                   }
                                                }
                                            });
                                }

                            }
                        });
                        builder.show();
                    }
                });

            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
    private void CheckOrederState()
    {
        DatabaseReference oerderRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders").child(Prevalent.currentOnlineUser.getPhone());
        oerderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.exists())
                {
                    String shippingState = snapshot.child("state").getValue().toString();
                    String userName = snapshot.child("name").getValue().toString();
                    if (shippingState.equals("shipped"))
                        {
                            txtTotalAmount.setText("Dear "+userName+"\n order is shipped successfully");
                            recyclerView.setVisibility(View.GONE);

                            textMsg1.setVisibility(View.VISIBLE);
                            textMsg1.setText("Congratulation Your Final Order has been Shipped successfully.Soon you will received your oeder at your door step.");
                            NextProcessBtn.setVisibility(View.GONE);
                            Toast.makeText(CartActivity.this, "You can purchase more Product......", Toast.LENGTH_SHORT).show();
                        }
                    else if (shippingState.equals("not shipped"))
                        {
                            txtTotalAmount.setText("Shipping State  = Not Shipped");
                            recyclerView.setVisibility(View.GONE);

                            textMsg1.setVisibility(View.VISIBLE);
                            NextProcessBtn.setVisibility(View.GONE);
                            Toast.makeText(CartActivity.this, "You can purchase more Product......", Toast.LENGTH_SHORT).show();

                        }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}