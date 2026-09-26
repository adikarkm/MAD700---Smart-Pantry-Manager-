package com.smartpantry.manager.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.smartpantry.manager.model.PantryItem;

import java.util.List;

// Database queries for the pantry_items table (Create, Read, Update, Delete)
@Dao
public interface PantryDao {
    // Get all items sorted A-Z
    @Query("SELECT * FROM pantry_items ORDER BY name ASC")
    List<PantryItem> getAllItems();

    // Add a new item
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PantryItem item);

    // Save changes to an item
    @Update
    void update(PantryItem item);

    // Remove an item
    @Delete
    void delete(PantryItem item);

    // Remove an item by its ID
    @Query("DELETE FROM pantry_items WHERE id = :id")
    void deleteById(int id);

    // Get one item by its ID
    @Query("SELECT * FROM pantry_items WHERE id = :id")
    PantryItem getById(int id);
}
