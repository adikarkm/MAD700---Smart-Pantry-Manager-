package com.smartpantry.manager.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.smartpantry.manager.R;
import com.smartpantry.manager.activity.RecipeDetailActivity;
import com.smartpantry.manager.adapter.RecipeAdapter;
import com.smartpantry.manager.database.AppDatabase;
import com.smartpantry.manager.logic.RecipeMatcher;
import com.smartpantry.manager.model.PantryItem;
import com.smartpantry.manager.model.Recipe;
import com.smartpantry.manager.model.RecipeIngredient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

// Recipes tab - lists only the recipes the user can make with their current pantry,
// plus a separate "Almost There" list of recipes missing just one ingredient
public class SuggestedRecipesFragment extends Fragment implements RecipeAdapter.OnRecipeClickListener {

    private RecyclerView rvRecipes;
    private TextView tvNoRecipes;
    private TextView tvRecipeCount;
    private RecipeAdapter adapter;
    private View layoutAlmostThere;
    private RecyclerView rvAlmostThere;
    private RecipeAdapter almostThereAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_suggested_recipes, container, false);

        rvRecipes = view.findViewById(R.id.rv_recipes);
        tvNoRecipes = view.findViewById(R.id.tv_no_recipes);
        tvRecipeCount = view.findViewById(R.id.tv_recipe_count);

        rvRecipes.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecipeAdapter(new ArrayList<>(), this);
        rvRecipes.setAdapter(adapter);

        // Almost There list (bonus) - uses the same adapter class in a second RecyclerView
        layoutAlmostThere = view.findViewById(R.id.layout_almost_there);
        rvAlmostThere = view.findViewById(R.id.rv_almost_there);
        rvAlmostThere.setLayoutManager(new LinearLayoutManager(getContext()));
        almostThereAdapter = new RecipeAdapter(new ArrayList<>(), this);
        rvAlmostThere.setAdapter(almostThereAdapter);

        return view;
    }

    // Re-run the matching every time the tab is opened, so changes to the pantry show straight away
    @Override
    public void onResume() {
        super.onResume();
        loadRecipes();
    }

    // Loads pantry and recipes from the database, runs the strict matching,
    // then shows the results (or a "no recipes" message)
    private void loadRecipes() {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(getContext());
            List<PantryItem> pantryItems = db.pantryDao().getAllItems();
            List<Recipe> allRecipes = db.recipeDao().getAll();
            List<RecipeIngredient> allIngredients = db.recipeIngredientDao().getAll();

            // Build the map of recipeId -> ingredient list
            Map<Integer, List<RecipeIngredient>> recipeIngredientsMap = new HashMap<>();
            for (RecipeIngredient ri : allIngredients) {
                if (!recipeIngredientsMap.containsKey(ri.getRecipeId())) {
                    recipeIngredientsMap.put(ri.getRecipeId(), new ArrayList<>());
                }
                recipeIngredientsMap.get(ri.getRecipeId()).add(ri);
            }

            // Strict matching - only recipes with every ingredient in the pantry
            List<Recipe> matchedRecipes = RecipeMatcher.getMatchingRecipes(pantryItems, allRecipes, recipeIngredientsMap);

            // Almost There - recipes missing exactly one ingredient (shown in a separate list)
            Map<Recipe, String> almostThere = RecipeMatcher.getAlmostThereRecipes(pantryItems, allRecipes, recipeIngredientsMap);

            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    adapter.updateData(matchedRecipes);
                    // Show feedback instead of a blank screen when nothing matches
                    if (matchedRecipes.isEmpty()) {
                        tvNoRecipes.setVisibility(View.VISIBLE);
                        rvRecipes.setVisibility(View.GONE);
                        tvRecipeCount.setText(getResources().getQuantityString(R.plurals.recipes_you_can_make, 0, 0));
                    } else {
                        tvNoRecipes.setVisibility(View.GONE);
                        rvRecipes.setVisibility(View.VISIBLE);
                        tvRecipeCount.setText(getResources().getQuantityString(R.plurals.recipes_you_can_make, matchedRecipes.size(), matchedRecipes.size()));
                    }

                    // Fill the Almost There list, or hide the whole section when it is empty
                    Map<Integer, String> missing = new HashMap<>();
                    for (Map.Entry<Recipe, String> entry : almostThere.entrySet()) {
                        missing.put(entry.getKey().getId(), entry.getValue());
                    }
                    almostThereAdapter.setMissingIngredients(missing);
                    almostThereAdapter.updateData(new ArrayList<>(almostThere.keySet()));
                    layoutAlmostThere.setVisibility(almostThere.isEmpty() ? View.GONE : View.VISIBLE);
                });
            }
        });
    }

    // Opens the Recipe Detail screen and passes the recipe ID in the Intent
    @Override
    public void onRecipeClick(Recipe recipe) {
        Intent intent = new Intent(getContext(), RecipeDetailActivity.class);
        intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, recipe.getId());
        startActivity(intent);
    }
}
