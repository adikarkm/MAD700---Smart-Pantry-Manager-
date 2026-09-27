package com.smartpantry.manager.logic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// Cleans up ingredient names so small differences don't stop a match.
// For example "Tomatoes", "tomato" and " TOMATO " all become "tomato".
public class IngredientNormalizer {

    // Plurals that don't follow the normal rules
    private static final Map<String, String> IRREGULAR_PLURALS = new HashMap<>();

    // Words that end in "s" but are not plurals (e.g. if the user adds hummus
    // or asparagus to their pantry), so they are left alone
    private static final Set<String> NOT_PLURAL = new HashSet<>();

    static {
        IRREGULAR_PLURALS.put("leaves", "leaf");
        IRREGULAR_PLURALS.put("loaves", "loaf");

        // Words ending in "s" that should not be made singular
        NOT_PLURAL.add("hummus");
        NOT_PLURAL.add("couscous");
        NOT_PLURAL.add("asparagus");
        NOT_PLURAL.add("molasses");
        NOT_PLURAL.add("citrus");
    }

    // Turns an ingredient name into its simple singular form.
    // Multi-word names like "red onions" only change the last word.
    public static String normalize(String name) {
        if (name == null) {
            return "";
        }

        // Lower case and squash extra spaces
        String cleaned = name.trim().toLowerCase().replaceAll("\\s+", " ");
        if (cleaned.isEmpty()) {
            return "";
        }

        // Split off the last word, e.g. "red onions" -> "red " + "onions"
        int lastSpace = cleaned.lastIndexOf(' ');
        String start = cleaned.substring(0, lastSpace + 1);
        String lastWord = cleaned.substring(lastSpace + 1);

        return start + singular(lastWord);
    }

    // Changes one word from plural to singular
    private static String singular(String word) {
        if (IRREGULAR_PLURALS.containsKey(word)) {
            return IRREGULAR_PLURALS.get(word);
        }
        if (NOT_PLURAL.contains(word) || word.length() <= 3) {
            return word;
        }

        // berries -> berry, cherries -> cherry
        if (word.endsWith("ies")) {
            return word.substring(0, word.length() - 3) + "y";
        }

        // tomatoes -> tomato, potatoes -> potato, peaches -> peach
        if (word.endsWith("oes") || word.endsWith("ches") || word.endsWith("shes")
                || word.endsWith("xes") || word.endsWith("zes") || word.endsWith("sses")) {
            return word.substring(0, word.length() - 2);
        }

        // Words ending in "ss" are not plurals, so they are left alone
        if (word.endsWith("ss")) {
            return word;
        }

        // eggs -> egg, onions -> onion, peppers -> pepper
        if (word.endsWith("s")) {
            return word.substring(0, word.length() - 1);
        }

        return word;
    }

    // True if the pantry name and the recipe name mean the same ingredient
    public static boolean matches(String pantryName, String recipeName) {
        return normalize(pantryName).equals(normalize(recipeName));
    }
}
