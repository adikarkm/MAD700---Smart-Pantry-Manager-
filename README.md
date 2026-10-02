# Smart Pantry Manager

Adikar Kandhai (402110242)

Smart Pantry Manager is an Android app made in Java for the Mobile App Development 700 assignment. The idea is to reduce food waste. You add the ingredients you have at home, and the app shows you which recipes you can make with them. A recipe only shows up if you have every ingredient it needs in the right amount.

## Features

- **Pantry:** add, edit and delete pantry items (name, quantity, unit and an optional expiry date)
- **Recipes:** 16 recipes that are loaded into the database the first time the app opens
- **Suggested Recipes:** shows only the recipes you can make right now
- **Recipe Detail:** the ingredients and steps for a recipe
- **Expiry alerts:** items that are expired or expire within 3 days are highlighted, and a pop-up lists them when the app opens
- **Settings:** turn the expiry alerts on or off and switch on a high contrast dark mode
- **Unit preference:** Metric/Imperial is in Settings but marked as coming soon

## How the matching works

The app checks every ingredient in a recipe against the pantry. If one ingredient is missing, or there is not enough of it, the recipe is not shown.

To make the matching less strict about small differences, it ignores upper and lower case, treats singular and plural names as the same (tomato and tomatoes) and converts units before comparing amounts. The conversions used are:

- 1 kg = 1000 g
- 1 L = 1000 ml
- 1 cup = 240 ml
- 1 tsp = 5 ml or 5 g
- 1 tbsp = 15 ml or 15 g

## Database

I used Room, which is built on top of SQLite. I chose it because everything is saved on the phone so the app works offline, there is no server or cloud account to set up, the data stays saved after the app is closed, and it is the database we covered in the module. Room also checks the SQL queries when the app is built, which helped me find mistakes early.

There are three tables: pantry_items, recipes and recipe_ingredients.

## Project structure

```
com.smartpantry.manager
    activity   MainActivity, AddEditIngredientActivity, RecipeDetailActivity
    fragment   PantryFragment, SuggestedRecipesFragment, SettingsFragment
    adapter    PantryAdapter, RecipeAdapter
    model      PantryItem, Recipe, RecipeIngredient
    dao        PantryDao, RecipeDao, RecipeIngredientDao
    database   AppDatabase, DatabaseSeeder
    logic      IngredientNormalizer, RecipeMatcher
```

## How to run it

1. Clone the repository:
   ```
   git clone https://github.com/adikarkm/MAD700---Smart-Pantry-Manager-.git
   ```
2. Open the folder in **Android Studio**.
3. Let **Gradle** sync.
4. Pick an emulator or phone with Android 7.0 (API 24) or newer.
5. Press **Run**.

The recipes are added the first time the app runs. If an older version of the app is already on the device, uninstall it first so the recipes load properly.

## Built with

Java, Android Studio, Room (SQLite), RecyclerView, CardView, BottomNavigationView and Material Components. Min SDK 24, target SDK 34.
