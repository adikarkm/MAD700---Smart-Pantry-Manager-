package com.smartpantry.manager.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.smartpantry.manager.model.RecipeIngredient;

import java.util.List;

// Database queries for the recipe_ingredients table
@Dao
public interface RecipeIngredientDao {
    // Get the ingredients for one recipe
    @Query("SELECT * FROM recipe_ingredients WHERE recipeId = :recipeId")
    List<RecipeIngredient> getByRecipeId(int recipeId);

    // Get every recipe ingredient (used by the matching screen)
    @Query("SELECT * FROM recipe_ingredients")
    List<RecipeIngredient> getAll();

    // Add many ingredients at once
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<RecipeIngredient> ingredients);
}
