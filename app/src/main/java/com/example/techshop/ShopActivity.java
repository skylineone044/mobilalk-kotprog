package com.example.techshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.color.DynamicColors;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ShopActivity extends AppCompatActivity {
    private static final String LOG_TAG = ShopActivity.class.getName();
    private Toolbar toolbar;
    private FirebaseUser user;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private CollectionReference firesotreItems;
    private RecyclerView itemlistRV;
    private ArrayList<ShopItem> itemList;
    private ItemListAdapter itemListAdapter;
    private int columns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DynamicColors.applyToActivitiesIfAvailable(this.getApplication());
        setContentView(R.layout.activity_shop);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.d(LOG_TAG, "Shopping as " + user.getDisplayName());
        } else {
            Log.d(LOG_TAG, "User unauthorized");
            finish();
        }

        itemlistRV = findViewById(R.id.itemListRV);
        columns = getResources().getInteger(R.integer.gallery_columns);
        itemlistRV.setLayoutManager(new GridLayoutManager(this, columns));
        itemList = new ArrayList<>();
        itemListAdapter = new ItemListAdapter(this, itemList);

        itemlistRV.setAdapter(itemListAdapter);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firesotreItems = firestore.collection("Items");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.openCartButton) {
                    openCart();
                    return true;
                } else if (item.getItemId() == R.id.logoutButton) {
                    logout();
                    return true;
                }
                return false;
            }
        });

        loadData();
    }

    private void openCart() {
        Intent openLoginIntent = new Intent(this, CartActivity.class);
        startActivity(openLoginIntent);
    }

    private void logout() {
        firebaseAuth.signOut();
        finish();
    }

    private void loadData() {
        itemList.clear();

        firesotreItems.orderBy("title").limit(10).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot queryDocumentSnapshot: queryDocumentSnapshots) {
                itemList.add(queryDocumentSnapshot.toObject(ShopItem.class));
            }
            if (itemList.isEmpty()) {
                initData();
                loadData();
            }
            itemListAdapter.notifyDataSetChanged();
        });

    }

    private void initData() {
        String[] itemTitles = getResources().getStringArray(R.array.itemTitles);
        String[] itemDescriptions = getResources().getStringArray(R.array.itemDescriptions);
        String[] itemPrices = getResources().getStringArray(R.array.itemPrices);
        TypedArray itemImageResource = getResources().obtainTypedArray(R.array.itemImages);

        for (int i = 0; i < itemTitles.length; i++) {
            firesotreItems.add(new ShopItem(
                    itemTitles[i],
                    itemDescriptions[i],
                    Integer.parseInt(itemPrices[i]),
                    itemImageResource.getResourceId(i, 0)
                    )
            );
        }

        itemImageResource.recycle();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.shop_top_menu, menu);
        return true;
    }
}