package com.smartpantry.manager.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.smartpantry.manager.model.Recipe;

import java.util.List;

// Database queries for the recipes table
@Dao
public interface RecipeDao {
    // Get all recipes sorted A-Z
    @Query("SELECT * FROM recipes ORDER BY name ASC")
    List<Recipe> getAll();

    // Get one recipe by its ID
    @Query("SELECT * FROM recipes WHERE id = :id")
    Recipe getById(int id);

    // Add many recipes at once
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Recipe> recipes);

    // Add one recipe and return its new ID
    @Insert
    long insert(Recipe recipe);

    // Count how many recipes are saved
    @Query("SELECT COUNT(*) FROM recipes")
    int getRecipeCount();
}
