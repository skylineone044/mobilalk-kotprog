package com.example.techshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CartItemListAdapter extends RecyclerView.Adapter<CartItemListAdapter.ViewHolder>  {
    private static final String LOG_TAG = CartItemListAdapter.class.getName();
    private ArrayList<CartItem> cartItems;
    private Context context;
    private int lastPosition = -1;
    private FirebaseFirestore firestore;
    private FirebaseUser user;

    public CartItemListAdapter( Context context, ArrayList<CartItem> cartItems) {
        this.cartItems = cartItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        firestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.cart_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemListAdapter.ViewHolder holder, int position) {
        CartItem currentItem = cartItems.get(position);
        holder.bindTo(currentItem);

        if (holder.getAdapterPosition() > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim2);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView amount;
        private final TextView price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.itemTitle);
            amount = itemView.findViewById(R.id.itemAmount);
            price = itemView.findViewById(R.id.itemPrice);
        }

        public void bindTo(CartItem currentItem) {
            firestore.collection("Items").whereEqualTo("title", currentItem.getItemTitle()).get().addOnSuccessListener(queryDocumentSnapshots -> {
                title.setText(currentItem.getItemTitle());
                amount.setText(Integer.toString(currentItem.getAmount()) + " db");
                price.setText(queryDocumentSnapshots.getDocuments().get(0).toObject(ShopItem.class).getPrice_ft() + " Ft / db");
            });
        }
    }
}
