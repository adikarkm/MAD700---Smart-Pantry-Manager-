package com.smartpantry.manager.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartpantry.manager.R;
import com.smartpantry.manager.model.PantryItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

// Adapter that shows each pantry item as a card in the RecyclerView
public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.ViewHolder> {

    private List<PantryItem> pantryItems;
    private OnItemClickListener listener;
    private boolean showExpiryWarnings = true; // controlled by the Expiry Alerts setting
    // Items expiring within 3 days are highlighted
    private static final long THREE_DAYS_MS = 3L * 24 * 60 * 60 * 1000;

    // Lets the fragment react to taps, long presses and the delete button
    public interface OnItemClickListener {
        void onItemClick(PantryItem item);
        void onItemLongClick(PantryItem item, int position);
        void onDeleteClick(PantryItem item, int position);
    }

    public PantryAdapter(List<PantryItem> pantryItems, OnItemClickListener listener) {
        this.pantryItems = pantryItems;
        this.listener = listener;
    }

    // Creates a new card view from item_pantry.xml
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pantry, parent, false);
        return new ViewHolder(view);
    }

    // Fills a card with the data for one pantry item
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PantryItem item = pantryItems.get(position);
        holder.tvName.setText(item.getName());

        String unit = item.getUnit() != null ? item.getUnit() : "";
        holder.tvQuantity.setText(item.getQuantity() + " " + unit);

        if (item.getExpiryDate() == null) {
            holder.tvExpiry.setText("No expiry");
            holder.tvExpiry.setTextColor(Color.GRAY);
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            holder.tvExpiry.setText(sdf.format(new Date(item.getExpiryDate())));

            // Colour the date: red = expired, orange = expires soon.
            // Only done when Expiry Alerts is switched on in Settings.
            long timeDiff = item.getExpiryDate() - System.currentTimeMillis();
            if (!showExpiryWarnings) {
                holder.tvExpiry.setTextColor(Color.GRAY);
            } else if (timeDiff < 0) {
                holder.tvExpiry.setTextColor(Color.parseColor("#D32F2F"));
            } else if (timeDiff <= THREE_DAYS_MS) {
                holder.tvExpiry.setTextColor(Color.parseColor("#FF5722"));
            } else {
                holder.tvExpiry.setTextColor(Color.GRAY);
            }
        }

        // Tap = edit, long press or bin icon = delete
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) {
                listener.onItemLongClick(item, position);
                return true;
            }
            return false;
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(item, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pantryItems == null ? 0 : pantryItems.size();
    }

    // Turns expiry highlighting on or off
    public void setShowExpiryWarnings(boolean show) {
        this.showExpiryWarnings = show;
    }

    // Replaces the list with new data from the database and refreshes the screen
    public void updateData(List<PantryItem> items) {
        this.pantryItems = items;
        notifyDataSetChanged();
    }

    // Removes one item from the list on screen
    public void removeItem(int position) {
        if (position >= 0 && position < pantryItems.size()) {
            pantryItems.remove(position);
            notifyItemRemoved(position);
        }
    }

    // Holds the views for one card so they are not looked up every time
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public TextView tvQuantity;
        public TextView tvExpiry;
        public android.widget.ImageView btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_pantry_name);
            tvQuantity = itemView.findViewById(R.id.tv_pantry_quantity);
            tvExpiry = itemView.findViewById(R.id.tv_pantry_expiry);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
