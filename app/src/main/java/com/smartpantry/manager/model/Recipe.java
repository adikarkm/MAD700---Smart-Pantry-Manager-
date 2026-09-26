package com.smartpantry.manager.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// One recipe (a row in the recipes table).
// Its ingredients are stored separately in RecipeIngredient.
@Entity(tableName = "recipes")
public class Recipe {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "preparationSteps")
    private String preparationSteps;

    public Recipe(@NonNull String name, String description, String preparationSteps) {
        this.name = name;
        this.description = description;
        this.preparationSteps = preparationSteps;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    @NonNull
    public String getName() { return name; }
    public void setName(@NonNull String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPreparationSteps() { return preparationSteps; }
    public void setPreparationSteps(String preparationSteps) { this.preparationSteps = preparationSteps; }
}
