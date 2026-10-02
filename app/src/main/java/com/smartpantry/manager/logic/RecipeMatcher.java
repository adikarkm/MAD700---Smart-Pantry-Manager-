package com.smartpantry.manager.logic;

import com.smartpantry.manager.model.PantryItem;
import com.smartpantry.manager.model.Recipe;
import com.smartpantry.manager.model.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// Decides which recipes the user can make with what is in their pantry.
// A recipe is only suggested if EVERY ingredient is in the pantry
// with at least the amount the recipe needs.
public class RecipeMatcher {

    // Unit groups - amounts can only be compared inside the same group
    private static final String GROUP_WEIGHT = "weight";
    private static final String GROUP_VOLUME = "volume";
    private static final String GROUP_COUNT = "count";

    // Returns the list of recipes the user can make right now
    public static List<Recipe> getMatchingRecipes(
            List<PantryItem> pantryItems,
            List<Recipe> allRecipes,
            Map<Integer, List<RecipeIngredient>> recipeIngredientsMap) {

        List<Recipe> matchingRecipes = new ArrayList<>();

        for (Recipe recipe : allRecipes) {
            List<RecipeIngredient> ingredients = recipeIngredientsMap.get(recipe.getId());

            // Skip recipes that have no ingredients saved
            if (ingredients == null || ingredients.isEmpty()) {
                continue;
            }

            // Check every ingredient - stop as soon as one is missing
            boolean canMake = true;
            for (RecipeIngredient needed : ingredients) {
                if (!hasEnough(pantryItems, needed)) {
                    canMake = false;
                    break;
                }
            }

            if (canMake) {
                matchingRecipes.add(recipe);
            }
        }

        return matchingRecipes;
    }

    // Checks if the pantry has enough of one ingredient
    private static boolean hasEnough(List<PantryItem> pantryItems, RecipeIngredient needed) {
        String neededGroup = getUnitGroup(needed.getUnit());
        double neededAmount = toBaseAmount(needed.getQuantity(), needed.getUnit());

        for (PantryItem item : pantryItems) {
            if (!IngredientNormalizer.matches(item.getName(), needed.getIngredientName())) {
                continue;
            }

            // Units must be comparable (g and kg are fine, g and cups are not, tsp/tbsp work with g)
            if (!unitsCanBeCompared(neededGroup, needed.getUnit(), item.getUnit())) {
                continue;
            }

            // Convert the pantry amount and check there is enough
            double pantryAmount = toBaseAmount(item.getQuantity(), item.getUnit());
            if (pantryAmount >= neededAmount) {
                return true;
            }
        }

        return false;
    }

    // Units can be compared if they are in the same group, or if one is a
    // teaspoon/tablespoon and the other is g or kg (1 tsp = 5 g, 1 tbsp = 15 g)
    private static boolean unitsCanBeCompared(String neededGroup, String neededUnit, String pantryUnit) {
        String pantryGroup = getUnitGroup(pantryUnit);
        if (neededGroup.equals(pantryGroup)) {
            return true;
        }
        return (isSpoon(neededUnit) && pantryGroup.equals(GROUP_WEIGHT))
                || (isSpoon(pantryUnit) && neededGroup.equals(GROUP_WEIGHT));
    }

    // True for teaspoons and tablespoons
    private static boolean isSpoon(String unit) {
        String u = cleanUnit(unit);
        return u.equals("tsp") || u.equals("tbsp");
    }

    // Works out which group a unit belongs to
    private static String getUnitGroup(String unit) {
        switch (cleanUnit(unit)) {
            case "g":
            case "kg":
                return GROUP_WEIGHT;
            case "ml":
            case "l":
            case "tsp":
            case "tbsp":
            case "cups":
            case "cup":
                return GROUP_VOLUME;
            default:
                // "pcs" or anything unknown is treated as a count
                return GROUP_COUNT;
        }
    }

    // Converts an amount to grams (weight), millilitres (volume) or pieces (count)
    // so that different units can be compared with each other
    private static double toBaseAmount(double quantity, String unit) {
        switch (cleanUnit(unit)) {
            case "kg":
                return quantity * 1000;
            case "l":
                return quantity * 1000;
            case "tsp":
                return quantity * 5;   // 1 tsp = 5 ml or 5 g
            case "tbsp":
                return quantity * 15;  // 1 tbsp = 15 ml or 15 g
            case "cups":
            case "cup":
                return quantity * 240;
            default:
                // g, ml and pcs are already base units
                return quantity;
        }
    }

    // Makes unit text lower case and removes spaces so "KG " matches "kg"
    private static String cleanUnit(String unit) {
        if (unit == null) {
            return "";
        }
        return unit.trim().toLowerCase();
    }

    // "Almost There" (bonus) - recipes missing exactly ONE ingredient.
    // These are kept separate from the strict suggestions above.
    // Returns each recipe with the name of the ingredient it still needs.
    public static Map<Recipe, String> getAlmostThereRecipes(
            List<PantryItem> pantryItems,
            List<Recipe> allRecipes,
            Map<Integer, List<RecipeIngredient>> recipeIngredientsMap) {

        Map<Recipe, String> almostThere = new java.util.LinkedHashMap<>();

        for (Recipe recipe : allRecipes) {
            List<RecipeIngredient> ingredients = recipeIngredientsMap.get(recipe.getId());
            if (ingredients == null || ingredients.isEmpty()) {
                continue;
            }

            // Count the ingredients that are missing or not enough
            int missingCount = 0;
            String missingName = null;
            for (RecipeIngredient needed : ingredients) {
                if (!hasEnough(pantryItems, needed)) {
                    missingCount++;
                    missingName = needed.getIngredientName();
                    if (missingCount > 1) {
                        break; // more than one missing, so it is not "almost there"
                    }
                }
            }

            if (missingCount == 1) {
                almostThere.put(recipe, missingName);
            }
        }

        return almostThere;
    }
}
