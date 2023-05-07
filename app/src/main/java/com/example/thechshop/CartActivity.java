package com.example.thechshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.color.DynamicColors;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class CartActivity extends AppCompatActivity {
    private static final String LOG_TAG = CartActivity.class.getName();

    private FirebaseUser user;
    private FirebaseFirestore firestore;
    private CollectionReference firestoreCartItems;
    private CollectionReference firestoreItems;
    private RecyclerView cartItemlistRV;
    private ArrayList<CartItem> cartItemList;
    private CartItemListAdapter cartItemListAdapter;
    private int columns;
    private TextView totalCostTV;
    private int totalCost = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DynamicColors.applyToActivitiesIfAvailable(this.getApplication());
        setContentView(R.layout.activity_cart);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.d(LOG_TAG, "Cart of " + user.getDisplayName());
        } else {
            Log.d(LOG_TAG, "User unauthorized");
            finish();
        }

        cartItemlistRV = findViewById(R.id.cartItemListRV);
        totalCostTV = findViewById(R.id.totalCostTV);
        columns = getResources().getInteger(R.integer.cart_columns);
        cartItemlistRV.setLayoutManager(new GridLayoutManager(this, columns));
        cartItemList = new ArrayList<>();
        cartItemListAdapter = new CartItemListAdapter(this, cartItemList);

        cartItemlistRV.setAdapter(cartItemListAdapter);

        firestore = FirebaseFirestore.getInstance();
        firestoreCartItems = firestore.collection("Cart");
        firestoreItems = firestore.collection("Items");

        loadData();
    }

    private void loadData() {
        Log.d(LOG_TAG, "Loading cart data...");
        cartItemList.clear();

        firestoreCartItems.whereEqualTo("userID", user.getUid()).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot queryDocumentSnapshot: queryDocumentSnapshots) {
                cartItemList.add(queryDocumentSnapshot.toObject(CartItem.class));
                Log.d(LOG_TAG, "Loading cart: " + queryDocumentSnapshot.toObject(CartItem.class).getItemTitle() + " " + queryDocumentSnapshot.toObject(CartItem.class).getAmount());
            }
            cartItemListAdapter.notifyDataSetChanged();
        });
        Log.d(LOG_TAG, "Loaded " + cartItemList.size() + " items");
//        updateCartTotal();
    }

    private void updateCartTotal() {
        totalCost = 0;
//                totalCost += Integer.parseInt(queryDocumentSnapshots1.getDocuments().get(0).getString("price"));
        for (CartItem cartItem : cartItemList) {
            firestoreItems.whereEqualTo("title", cartItem.getItemTitle()).get().addOnSuccessListener(queryDocumentSnapshots -> {
                totalCost += cartItem.getAmount() * Integer.parseInt(queryDocumentSnapshots.getDocuments().get(0).get("price_ft").toString());
            });
        }
        totalCostTV.setText("Összesen: " + totalCost + " Ft");
    }

    public void palceOrder(View view) {
        Toast.makeText(getApplicationContext(), "Rendelés leadva", Toast.LENGTH_SHORT).show();
        firestoreCartItems.whereEqualTo("userID", user.getUid()).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot queryDocumentSnapshot: queryDocumentSnapshots) {
                firestoreCartItems.document(queryDocumentSnapshot.getId()).delete();
            }
        });
        Toast.makeText(getApplicationContext(), R.string.order_places_successfully, Toast.LENGTH_SHORT).show();
        finish();
    }
}