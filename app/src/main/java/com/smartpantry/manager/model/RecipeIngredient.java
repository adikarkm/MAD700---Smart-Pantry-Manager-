package com.smartpantry.manager.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

// One ingredient needed by a recipe (a row in the recipe_ingredients table).
// recipeId links it to its recipe; deleting a recipe also deletes its ingredients.
@Entity(
    tableName = "recipe_ingredients",
    foreignKeys = @ForeignKey(
        entity = Recipe.class,
        parentColumns = "id",
        childColumns = "recipeId",
        onDelete = ForeignKey.CASCADE
    )
)
public class RecipeIngredient {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "recipeId", index = true)
    private int recipeId;

    @NonNull
    @ColumnInfo(name = "ingredientName")
    private String ingredientName;

    @ColumnInfo(name = "quantity")
    private double quantity;

    @ColumnInfo(name = "unit")
    private String unit;

    public RecipeIngredient(int recipeId, @NonNull String ingredientName, double quantity, String unit) {
        this.recipeId = recipeId;
        this.ingredientName = ingredientName;
        this.quantity = quantity;
        this.unit = unit;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getRecipeId() { return recipeId; }
    public void setRecipeId(int recipeId) { this.recipeId = recipeId; }

    @NonNull
    public String getIngredientName() { return ingredientName; }
    public void setIngredientName(@NonNull String ingredientName) { this.ingredientName = ingredientName; }

    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
}
