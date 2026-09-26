package com.smartpantry.manager.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.smartpantry.manager.dao.PantryDao;
import com.smartpantry.manager.dao.RecipeDao;
import com.smartpantry.manager.dao.RecipeIngredientDao;
import com.smartpantry.manager.model.PantryItem;
import com.smartpantry.manager.model.Recipe;
import com.smartpantry.manager.model.RecipeIngredient;

import java.util.concurrent.Executors;

// The Room (SQLite) database for the app.
// Holds three tables: pantry items, recipes and recipe ingredients.
@Database(entities = {PantryItem.class, Recipe.class, RecipeIngredient.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    // Access to each table's queries
    public abstract PantryDao pantryDao();
    public abstract RecipeDao recipeDao();
    public abstract RecipeIngredientDao recipeIngredientDao();

    // Only one copy of the database is used in the whole app
    private static volatile AppDatabase INSTANCE;

    // Returns the database, creating it the first time it is needed
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "smart_pantry_db")
                            .addCallback(new SeedDatabaseCallback(context))
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // Runs when the database is first created and fills it with the recipes
    private static class SeedDatabaseCallback extends RoomDatabase.Callback {
        private final Context context;

        SeedDatabaseCallback(Context context) {
            this.context = context;
        }

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Executors.newSingleThreadExecutor().execute(() -> {
                DatabaseSeeder.seedRecipes(AppDatabase.getInstance(context));
            });
        }
    }
}
