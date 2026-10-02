package com.smartpantry.manager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartpantry.manager.R;
import com.smartpantry.manager.model.Recipe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Adapter that shows each suggested recipe as a card in the RecyclerView
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private List<Recipe> recipes;
    private OnRecipeClickListener listener;
    // Recipe ID -> missing ingredient (only used by the Almost There list)
    private Map<Integer, String> missingIngredients = new HashMap<>();

    // Lets the fragment know which recipe was tapped
    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }

    public RecipeAdapter(List<Recipe> recipes, OnRecipeClickListener listener) {
        this.recipes = recipes;
        this.listener = listener;
    }

    // Creates a new card view from item_recipe.xml
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        return new ViewHolder(view);
    }

    // Fills a card with the recipe name and description (or what it still needs)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.tvRecipeName.setText(recipe.getName());
        String missing = missingIngredients.get(recipe.getId());
        if (missing != null) {
            holder.tvRecipeDescription.setText(holder.itemView.getContext().getString(R.string.almost_there_needs, missing));
        } else {
            holder.tvRecipeDescription.setText(recipe.getDescription());
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRecipeClick(recipe);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipes == null ? 0 : recipes.size();
    }

    // Replaces the list with new results and refreshes the screen
    public void updateData(List<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    // Sets which ingredient each recipe is missing (for the Almost There list)
    public void setMissingIngredients(Map<Integer, String> missingIngredients) {
        this.missingIngredients = missingIngredients;
    }

    // Holds the views for one card
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvRecipeName;
        public TextView tvRecipeDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            tvRecipeName = itemView.findViewById(R.id.tv_recipe_name);
            tvRecipeDescription = itemView.findViewById(R.id.tv_recipe_description);
        }
    }
}
