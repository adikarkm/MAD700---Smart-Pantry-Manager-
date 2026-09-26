package com.smartpantry.manager.database;

import com.smartpantry.manager.dao.RecipeDao;
import com.smartpantry.manager.dao.RecipeIngredientDao;
import com.smartpantry.manager.model.Recipe;
import com.smartpantry.manager.model.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;

// Adds the starting recipes to the database the first time the app runs.
// Each recipe is inserted first, then its ingredients are linked using the new recipe ID.
public class DatabaseSeeder {

    public static void seedRecipes(AppDatabase db) {
        RecipeDao recipeDao = db.recipeDao();
        RecipeIngredientDao recipeIngredientDao = db.recipeIngredientDao();

        // 1. Tomato Pasta
        long r1 = recipeDao.insert(new Recipe("Tomato Pasta", "A simple and classic tomato-based pasta dish", "1. Boil pasta according to package instructions. 2. Dice tomatoes and mince garlic. 3. Heat olive oil in a pan, sauté garlic until golden. 4. Add tomatoes and cook for 10 minutes. 5. Season with salt. 6. Toss pasta with the sauce and serve."));
        List<RecipeIngredient> i1 = new ArrayList<>();
        i1.add(new RecipeIngredient((int) r1, "pasta", 200, "g"));
        i1.add(new RecipeIngredient((int) r1, "tomato", 3, "pcs"));
        i1.add(new RecipeIngredient((int) r1, "garlic", 2, "pcs"));
        i1.add(new RecipeIngredient((int) r1, "olive oil", 2, "tbsp"));
        i1.add(new RecipeIngredient((int) r1, "salt", 1, "tsp"));
        recipeIngredientDao.insertAll(i1);

        // 2. Scrambled Eggs
        long r2 = recipeDao.insert(new Recipe("Scrambled Eggs", "Quick and fluffy scrambled eggs", "1. Crack eggs into a bowl and whisk well. 2. Melt butter in a non-stick pan over medium-low heat. 3. Pour in eggs and gently stir with a spatula. 4. Cook until just set but still creamy. 5. Season with salt and pepper."));
        List<RecipeIngredient> i2 = new ArrayList<>();
        i2.add(new RecipeIngredient((int) r2, "egg", 3, "pcs"));
        i2.add(new RecipeIngredient((int) r2, "butter", 1, "tbsp"));
        i2.add(new RecipeIngredient((int) r2, "salt", 0.5, "tsp"));
        i2.add(new RecipeIngredient((int) r2, "pepper", 0.25, "tsp"));
        recipeIngredientDao.insertAll(i2);

        // 3. Cheese Toast
        long r3 = recipeDao.insert(new Recipe("Cheese Toast", "Classic crispy grilled cheese sandwich", "1. Butter one side of each bread slice. 2. Place cheese between the unbuttered sides. 3. Heat a pan over medium heat. 4. Grill sandwich until golden brown on each side. 5. Slice and serve hot."));
        List<RecipeIngredient> i3 = new ArrayList<>();
        i3.add(new RecipeIngredient((int) r3, "bread", 2, "pcs"));
        i3.add(new RecipeIngredient((int) r3, "cheese", 2, "pcs"));
        i3.add(new RecipeIngredient((int) r3, "butter", 1, "tbsp"));
        recipeIngredientDao.insertAll(i3);

        // 4. Omelette
        long r4 = recipeDao.insert(new Recipe("Omelette", "Classic omelette with cheese and vegetables", "1. Whisk eggs in a bowl. 2. Dice onion and pepper. 3. Melt butter in a pan over medium heat. 4. Sauté onion and pepper until soft. 5. Pour in eggs and cook until set on the bottom. 6. Add cheese, fold, and serve."));
        List<RecipeIngredient> i4 = new ArrayList<>();
        i4.add(new RecipeIngredient((int) r4, "egg", 3, "pcs"));
        i4.add(new RecipeIngredient((int) r4, "cheese", 50, "g"));
        i4.add(new RecipeIngredient((int) r4, "onion", 0.5, "pcs"));
        i4.add(new RecipeIngredient((int) r4, "pepper", 0.5, "pcs"));
        i4.add(new RecipeIngredient((int) r4, "butter", 1, "tbsp"));
        recipeIngredientDao.insertAll(i4);

        // 5. Mashed Potatoes
        long r5 = recipeDao.insert(new Recipe("Mashed Potatoes", "Creamy homemade mashed potatoes", "1. Peel and quarter potatoes. 2. Boil in salted water until fork-tender (about 15 min). 3. Drain and return to pot. 4. Add butter and milk. 5. Mash until smooth and creamy. 6. Season with salt."));
        List<RecipeIngredient> i5 = new ArrayList<>();
        i5.add(new RecipeIngredient((int) r5, "potato", 4, "pcs"));
        i5.add(new RecipeIngredient((int) r5, "butter", 3, "tbsp"));
        i5.add(new RecipeIngredient((int) r5, "milk", 0.5, "cups"));
        i5.add(new RecipeIngredient((int) r5, "salt", 1, "tsp"));
        recipeIngredientDao.insertAll(i5);
    }
}
