package com.smartpantry.manager.activity;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.smartpantry.manager.R;
import com.smartpantry.manager.database.AppDatabase;
import com.smartpantry.manager.model.Recipe;
import com.smartpantry.manager.model.RecipeIngredient;
import java.util.List;
import java.util.concurrent.Executors;

// Shows the full ingredient list and method for one recipe.
// The recipe ID is passed in from the Suggested Recipes screen.
public class RecipeDetailActivity extends AppCompatActivity {
    // Key used to pass the recipe ID in the Intent
    public static final String EXTRA_RECIPE_ID = "EXTRA_RECIPE_ID";

    private TextView tvRecipeName;
    private TextView tvIngredientsList;
    private TextView tvPreparationSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.recipe_detail);
        }

        tvRecipeName = findViewById(R.id.tv_recipe_name);
        tvIngredientsList = findViewById(R.id.tv_ingredients_list);
        tvPreparationSteps = findViewById(R.id.tv_preparation_steps);

        // Read the recipe ID sent from the previous screen
        int recipeId = getIntent().getIntExtra(EXTRA_RECIPE_ID, -1);
        if (recipeId != -1) {
            loadRecipe(recipeId);
        }
    }

    // Loads the recipe and its ingredients on a background thread,
    // then shows them on the screen
    private void loadRecipe(int recipeId) {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            Recipe recipe = db.recipeDao().getById(recipeId);
            List<RecipeIngredient> ingredients = db.recipeIngredientDao().getByRecipeId(recipeId);

            runOnUiThread(() -> {
                if (recipe != null) {
                    tvRecipeName.setText(recipe.getName());
                    tvPreparationSteps.setText(recipe.getPreparationSteps());
                }

                // Build a bullet list like "• 2 pcs egg"
                StringBuilder ingredientsBuilder = new StringBuilder();
                if (ingredients != null) {
                    for (RecipeIngredient ri : ingredients) {
                        ingredientsBuilder.append("• ")
                                .append(formatQuantity(ri.getQuantity())).append(" ")
                                .append(ri.getUnit()).append(" ")
                                .append(ri.getIngredientName()).append("\n");
                    }
                }
                tvIngredientsList.setText(ingredientsBuilder.toString().trim());
            });
        });
    }

    // Shows whole numbers without ".0" (2.0 -> "2") but keeps decimals (0.25 -> "0.25")
    private String formatQuantity(double quantity) {
        if (quantity == Math.floor(quantity)) {
            return String.valueOf((long) quantity);
        }
        return String.valueOf(quantity);
    }

    // Back arrow in the toolbar closes this screen
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
