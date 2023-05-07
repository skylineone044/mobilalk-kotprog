package com.example.thechshop;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {
    private static final String LOG_TAG = ItemListAdapter.class.getName();
    private ArrayList<ShopItem> items;
    private Context context;
    private int lastPosition = -1;
    private FirebaseFirestore firestore;
    private FirebaseUser user;

    public ItemListAdapter(Context context, ArrayList<ShopItem> items) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        firestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemListAdapter.ViewHolder holder, int position) {
        ShopItem currentItem = items.get(position);
        holder.bindTo(currentItem);

        if (holder.getAdapterPosition() > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_anim1);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView description;
        private final TextView price;
        private final ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.itemTitle);
            description = itemView.findViewById(R.id.itemDescription);
            price = itemView.findViewById(R.id.itemPrice);
            imageView = itemView.findViewById(R.id.itemImage);
            itemView.findViewById(R.id.toCartButton).setOnClickListener(v -> {
                addToCart(new ShopItem(title.getText().toString(), description.getText().toString(), Integer.parseInt(price.getText().toString().split(" ")[0])));
                Log.d(LOG_TAG, "Adding item to cart");
            });
        }

        public void bindTo(ShopItem currentItem) {
            title.setText(currentItem.getTitle());
            description.setText(currentItem.getDescription());
            price.setText(String.valueOf(currentItem.getPrice_ft()) + " Ft");
            Glide.with(context)
                    .load(currentItem.getImageResource())
                    .dontAnimate()
                    .transform(new RoundedCorners(50))
                    .into(imageView);
        }
    }

    private void addToCart(ShopItem item) {
        firestore.collection("Cart").whereEqualTo("itemTitle", item.getTitle()).whereEqualTo("userID", user.getUid()).get().addOnSuccessListener(queryDocumentSnapshots -> {
            int currentlyInCart = 0;
            String inCartID = "";

            if (queryDocumentSnapshots.size() > 0) {
                currentlyInCart = queryDocumentSnapshots.toObjects(CartItem.class).get(0).getAmount();
                inCartID = queryDocumentSnapshots.getDocuments().get(0).getId();
                firestore.collection("Cart").document(inCartID).update("amount", currentlyInCart + 1);
            } else {
                firestore.collection("Cart").add(new CartItem(
                        user.getUid(),
                        item.getTitle(),
                        1
                ));
            }
        });
        Toast.makeText(context.getApplicationContext(), item.getTitle() + " " + context.getResources().getString(R.string.added_to_cart), Toast.LENGTH_SHORT).show();
    }
}
