package com.smartpantry.manager.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// One ingredient in the user's pantry (a row in the pantry_items table)
@Entity(tableName = "pantry_items")
public class PantryItem {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "quantity")
    private double quantity;

    @ColumnInfo(name = "unit")
    private String unit;

    @ColumnInfo(name = "expiryDate")
    private Long expiryDate; // stored as milliseconds, null if no expiry date

    public PantryItem(@NonNull String name, double quantity, String unit, Long expiryDate) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    @NonNull
    public String getName() { return name; }
    public void setName(@NonNull String name) { this.name = name; }

    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public Long getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Long expiryDate) { this.expiryDate = expiryDate; }
}
