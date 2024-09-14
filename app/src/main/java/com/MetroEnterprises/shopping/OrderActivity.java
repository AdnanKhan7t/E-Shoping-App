package com.MetroEnterprises.shopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.MetroEnterprises.shopping.Model.Cart;
import com.MetroEnterprises.shopping.Model.UserOrders;
import com.MetroEnterprises.shopping.Prevalent.CartViewHolder;
import com.MetroEnterprises.shopping.Prevalent.Prevalent;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderActivity extends AppCompatActivity {
    private RecyclerView orderList;
    private DatabaseReference orderReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        orderReference = FirebaseDatabase.getInstance().getReference().child("Orders");
        orderList = findViewById(R.id.orders);
        orderList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<UserOrders> options =
                new FirebaseRecyclerOptions.Builder<UserOrders>()
                        .setQuery(orderReference.child("Users").child(Prevalent.currentOnlineUser.getPhone()).child("my orders"),
                                UserOrders.class)
                        .build();

        FirebaseRecyclerAdapter<UserOrders, UserOrdersViewHolder> adapter =
                new FirebaseRecyclerAdapter<UserOrders, UserOrdersViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull UserOrdersViewHolder holder,final int position, @NonNull UserOrders model) {
                        holder.UserTotalQuantity.setText("total Quantity"+model.getQuantity());
                        holder.userName.setText("Name: " + model.getName());
                        holder.userPhoneNumber.setText("Phone No.: "+ model.getPhone());
                        holder.userTotalPrice.setText("Total Ammount = Rs."+model.getTotalAmount());
                        holder.userDateTime.setText("Order at: "+model.getDate()+" "+ model.getTime());
                        holder.userShippingAddress.setText("Shipping Address: "+model.getAddress()+", "+model.getCity());

                    }

                    @NonNull
                    @Override
                    public UserOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_layout,parent,false);
                        return new UserOrdersViewHolder(view);
                    }
                };
        orderList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class UserOrdersViewHolder extends RecyclerView.ViewHolder {
        public TextView userName,userPhoneNumber,userTotalPrice,userDateTime,userShippingAddress,UserTotalQuantity;
        public UserOrdersViewHolder(View itemView) {
            super(itemView);
            UserTotalQuantity = itemView.findViewById(R.id.order_Quantity);
            userName = itemView.findViewById(R.id.order_user_name);
            userPhoneNumber = itemView.findViewById(R.id.order_phone_number);
            userTotalPrice = itemView.findViewById(R.id.order_total_price);
            userDateTime = itemView.findViewById(R.id.order_date_time);
            userShippingAddress = itemView.findViewById(R.id.order_address_city);
        }
    }


}

