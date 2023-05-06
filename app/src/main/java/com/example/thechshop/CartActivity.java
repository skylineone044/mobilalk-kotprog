package com.example.thechshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class CartActivity extends AppCompatActivity {
    private static final String LOG_TAG = CartActivity.class.getName();

    private FirebaseUser user;
    private FirebaseFirestore firestore;
    private CollectionReference firestoreCartItems;
    private RecyclerView cartItemlistRV;
    private ArrayList<CartItem> cartItemList;
    private CartItemListAdapter cartItemListAdapter;
    private int columns;
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
        columns = getResources().getInteger(R.integer.gallery_columns);
        cartItemlistRV.setLayoutManager(new GridLayoutManager(this, columns));
        cartItemList = new ArrayList<>();
        cartItemListAdapter = new CartItemListAdapter(this, cartItemList);

        cartItemlistRV.setAdapter(cartItemListAdapter);

        firestore = FirebaseFirestore.getInstance();
        firestoreCartItems = firestore.collection("Cart");

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