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
        long r3 = recipeDao.insert(new Recipe("Cheese Toast", "Cheese toast", "1. Butter one side of each bread slice. 2. Place cheese between the unbuttered sides. 3. Heat a pan over medium heat. 4. Grill sandwich until golden brown on each side. 5. Slice and serve hot."));
        List<RecipeIngredient> i3 = new ArrayList<>();
        i3.add(new RecipeIngredient((int) r3, "bread", 2, "pcs"));
        i3.add(new RecipeIngredient((int) r3, "cheese", 50, "g")); // grams, same as the Omelette
        i3.add(new RecipeIngredient((int) r3, "butter", 1, "tbsp"));
        recipeIngredientDao.insertAll(i3);

        // 4. Omelette
        long r4 = recipeDao.insert(new Recipe("Omelette", "Classic omelette with cheese and vegetables", "1. Whisk eggs in a bowl. 2. Dice onion and bell pepper. 3. Melt butter in a pan over medium heat. 4. Sauté onion and bell pepper until soft. 5. Pour in eggs and cook until set on the bottom. 6. Add cheese, fold, and serve."));
        List<RecipeIngredient> i4 = new ArrayList<>();
        i4.add(new RecipeIngredient((int) r4, "egg", 3, "pcs"));
        i4.add(new RecipeIngredient((int) r4, "cheese", 50, "g"));
        i4.add(new RecipeIngredient((int) r4, "onion", 0.5, "pcs"));
        i4.add(new RecipeIngredient((int) r4, "bell pepper", 0.5, "pcs")); // the vegetable, not ground pepper
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

        // 6. Peanut Butter Toast
        long r6 = recipeDao.insert(new Recipe("Peanut Butter Toast", "Quick toast with peanut butter", "1. Toast the bread until golden. 2. Spread peanut butter on each slice. 3. Serve straight away."));
        List<RecipeIngredient> i6 = new ArrayList<>();
        i6.add(new RecipeIngredient((int) r6, "bread", 2, "pcs"));
        i6.add(new RecipeIngredient((int) r6, "peanut butter", 2, "tbsp"));
        recipeIngredientDao.insertAll(i6);

        // 7. Egg Fried Rice
        long r7 = recipeDao.insert(new Recipe("Egg Fried Rice", "Simple fried rice with egg and onion", "1. Cook the rice and let it cool. 2. Dice the onion. 3. Heat olive oil in a pan and fry the onion until soft. 4. Push the onion aside, crack in the eggs and scramble them. 5. Add the rice and soy sauce, stir-fry for 3 minutes and serve."));
        List<RecipeIngredient> i7 = new ArrayList<>();
        i7.add(new RecipeIngredient((int) r7, "rice", 200, "g"));
        i7.add(new RecipeIngredient((int) r7, "egg", 2, "pcs"));
        i7.add(new RecipeIngredient((int) r7, "onion", 1, "pcs"));
        i7.add(new RecipeIngredient((int) r7, "soy sauce", 2, "tbsp"));
        i7.add(new RecipeIngredient((int) r7, "olive oil", 1, "tbsp"));
        recipeIngredientDao.insertAll(i7);

        // 8. Cheeseburger
        long r8 = recipeDao.insert(new Recipe("Cheeseburger", "Homemade beef burger with cheese", "1. Mix the beef mince with salt and shape it into a patty. 2. Fry the patty for 4 minutes on each side. 3. Put the cheese on the patty to melt. 4. Slice the tomato and onion. 5. Place everything in the bun and serve."));
        List<RecipeIngredient> i8 = new ArrayList<>();
        i8.add(new RecipeIngredient((int) r8, "burger bun", 1, "pcs"));
        i8.add(new RecipeIngredient((int) r8, "beef mince", 150, "g"));
        i8.add(new RecipeIngredient((int) r8, "cheese", 25, "g"));
        i8.add(new RecipeIngredient((int) r8, "tomato", 1, "pcs"));
        i8.add(new RecipeIngredient((int) r8, "onion", 0.5, "pcs"));
        i8.add(new RecipeIngredient((int) r8, "salt", 0.5, "tsp"));
        recipeIngredientDao.insertAll(i8);

        // 9. Boiled Eggs
        long r9 = recipeDao.insert(new Recipe("Boiled Eggs", "Simple boiled eggs", "1. Bring a pot of water to the boil. 2. Gently add the eggs. 3. Boil for 7 minutes (or 5 for soft eggs). 4. Cool in cold water, peel and sprinkle with salt."));
        List<RecipeIngredient> i9 = new ArrayList<>();
        i9.add(new RecipeIngredient((int) r9, "egg", 2, "pcs"));
        i9.add(new RecipeIngredient((int) r9, "salt", 0.25, "tsp"));
        recipeIngredientDao.insertAll(i9);

        // 10. Fried Egg on Toast
        long r10 = recipeDao.insert(new Recipe("Fried Egg on Toast", "A fried egg served on buttered toast", "1. Toast the bread and spread half the butter on it. 2. Melt the rest of the butter in a pan. 3. Fry the egg until the white is set. 4. Place the egg on the toast and serve."));
        List<RecipeIngredient> i10 = new ArrayList<>();
        i10.add(new RecipeIngredient((int) r10, "egg", 1, "pcs"));
        i10.add(new RecipeIngredient((int) r10, "bread", 1, "pcs"));
        i10.add(new RecipeIngredient((int) r10, "butter", 1, "tbsp"));
        recipeIngredientDao.insertAll(i10);

        // 11. French Toast
        long r11 = recipeDao.insert(new Recipe("French Toast", "Bread dipped in egg and milk, then fried", "1. Whisk the eggs and milk in a bowl. 2. Dip each bread slice in the mixture. 3. Melt butter in a pan over medium heat. 4. Fry the bread until golden on both sides. 5. Serve warm."));
        List<RecipeIngredient> i11 = new ArrayList<>();
        i11.add(new RecipeIngredient((int) r11, "bread", 2, "pcs"));
        i11.add(new RecipeIngredient((int) r11, "egg", 2, "pcs"));
        i11.add(new RecipeIngredient((int) r11, "milk", 0.25, "cups"));
        i11.add(new RecipeIngredient((int) r11, "butter", 1, "tbsp"));
        recipeIngredientDao.insertAll(i11);

        // 12. Garlic Bread
        long r12 = recipeDao.insert(new Recipe("Garlic Bread", "Toasted bread with garlic butter", "1. Crush the garlic and mix it with the butter. 2. Spread the garlic butter on the bread. 3. Toast in the oven or a pan until golden. 4. Serve hot."));
        List<RecipeIngredient> i12 = new ArrayList<>();
        i12.add(new RecipeIngredient((int) r12, "bread", 2, "pcs"));
        i12.add(new RecipeIngredient((int) r12, "butter", 2, "tbsp"));
        i12.add(new RecipeIngredient((int) r12, "garlic", 1, "pcs"));
        recipeIngredientDao.insertAll(i12);

        // 13. Cheesy Pasta
        long r13 = recipeDao.insert(new Recipe("Cheesy Pasta", "Easy pasta in a creamy cheese sauce", "1. Boil the pasta according to package instructions. 2. Melt the butter in a pan. 3. Add the milk and warm it up. 4. Stir in the cheese until it melts. 5. Mix the sauce with the pasta and serve."));
        List<RecipeIngredient> i13 = new ArrayList<>();
        i13.add(new RecipeIngredient((int) r13, "pasta", 200, "g"));
        i13.add(new RecipeIngredient((int) r13, "cheese", 100, "g"));
        i13.add(new RecipeIngredient((int) r13, "milk", 0.5, "cups"));
        i13.add(new RecipeIngredient((int) r13, "butter", 1, "tbsp"));
        recipeIngredientDao.insertAll(i13);

        // 14. Tomato Soup
        long r14 = recipeDao.insert(new Recipe("Tomato Soup", "Simple homemade tomato soup", "1. Dice the onion, garlic and tomatoes. 2. Heat olive oil in a pot and cook the onion and garlic until soft. 3. Add the tomatoes and a cup of water. 4. Simmer for 15 minutes. 5. Blend until smooth, season with salt and serve."));
        List<RecipeIngredient> i14 = new ArrayList<>();
        i14.add(new RecipeIngredient((int) r14, "tomato", 4, "pcs"));
        i14.add(new RecipeIngredient((int) r14, "onion", 1, "pcs"));
        i14.add(new RecipeIngredient((int) r14, "garlic", 1, "pcs"));
        i14.add(new RecipeIngredient((int) r14, "olive oil", 1, "tbsp"));
        i14.add(new RecipeIngredient((int) r14, "salt", 0.5, "tsp"));
        recipeIngredientDao.insertAll(i14);

        // 15. Baked Potato
        long r15 = recipeDao.insert(new Recipe("Baked Potato", "Oven-baked potato with butter and cheese", "1. Wash the potato and prick it with a fork. 2. Bake at 200C for about 1 hour until soft. 3. Cut it open and add the butter. 4. Top with cheese and salt and serve."));
        List<RecipeIngredient> i15 = new ArrayList<>();
        i15.add(new RecipeIngredient((int) r15, "potato", 1, "pcs"));
        i15.add(new RecipeIngredient((int) r15, "butter", 1, "tbsp"));
        i15.add(new RecipeIngredient((int) r15, "cheese", 30, "g"));
        i15.add(new RecipeIngredient((int) r15, "salt", 0.25, "tsp"));
        recipeIngredientDao.insertAll(i15);

        // 16. Waffles
        long r16 = recipeDao.insert(new Recipe("Waffles", "Golden homemade waffles", "1. Melt the butter. 2. Mix the flour, eggs, milk and melted butter into a smooth batter. 3. Heat a waffle maker. 4. Pour in some batter and cook until golden. 5. Repeat with the rest of the batter and serve."));
        List<RecipeIngredient> i16 = new ArrayList<>();
        i16.add(new RecipeIngredient((int) r16, "flour", 150, "g"));
        i16.add(new RecipeIngredient((int) r16, "egg", 2, "pcs"));
        i16.add(new RecipeIngredient((int) r16, "milk", 1, "cups"));
        i16.add(new RecipeIngredient((int) r16, "butter", 2, "tbsp"));
        recipeIngredientDao.insertAll(i16);
    }
}
