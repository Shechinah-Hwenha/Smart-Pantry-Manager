package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SuggestedRecipesActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private ListView listSuggestedRecipes;

    private ArrayList<Recipe> suggestedRecipes;
    private RecipeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_suggested_recipes);

        databaseHelper = new DatabaseHelper(this);

        listSuggestedRecipes =
                findViewById(R.id.listSuggestedRecipes);

        suggestedRecipes = new ArrayList<>();

        adapter =
                new RecipeAdapter(
                        this,
                        suggestedRecipes
                );

        listSuggestedRecipes.setAdapter(adapter);

        loadSuggestedRecipes();

        listSuggestedRecipes.setOnItemClickListener(
                (parent, view, position, id) -> {

                    Recipe selectedRecipe =
                            adapter.getItem(position);

                    if (selectedRecipe != null) {

                        Intent intent =
                                new Intent(
                                        SuggestedRecipesActivity.this,
                                        RecipeDetailActivity.class
                                );

                        intent.putExtra(
                                "recipe_id",
                                selectedRecipe.getId()
                        );

                        startActivity(intent);
                    }
                }
        );
    }

    private void loadSuggestedRecipes() {

        ArrayList<Recipe> allRecipes =
                databaseHelper.getAllRecipes();

        suggestedRecipes.clear();

        ArrayList<PantryItem> pantryItems =
                databaseHelper.getAllPantryItems();

        for (Recipe recipe : allRecipes) {

            ArrayList<RecipeIngredient> requiredIngredients =
                    databaseHelper.getRecipeIngredients(
                            recipe.getId()
                    );

            if (requiredIngredients.isEmpty()) {
                continue;
            }

            boolean canMakeRecipe = true;

            for (RecipeIngredient required :
                    requiredIngredients) {

                boolean ingredientFound = false;

                for (PantryItem pantry :
                        pantryItems) {

                    String pantryName =
                            normalizeIngredientName(
                                    pantry.getName()
                            );

                    String requiredName =
                            normalizeIngredientName(
                                    required.getIngredientName()
                            );

                    if (pantryName.equals(requiredName)
                            && pantry.getUnit()
                            .equalsIgnoreCase(
                                    required.getUnit()
                            )
                            && pantry.getQuantity()
                            >= required.getRequiredQuantity()) {

                        ingredientFound = true;
                        break;
                    }
                }

                if (!ingredientFound) {

                    canMakeRecipe = false;
                    break;
                }
            }

            if (canMakeRecipe) {

                suggestedRecipes.add(recipe);
            }
        }

        adapter.notifyDataSetChanged();

        if (suggestedRecipes.isEmpty()) {

            Toast.makeText(
                    this,
                    "No recipes can be made with your pantry items",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    private String normalizeIngredientName(String name) {

        String value =
                name.toLowerCase().trim();

        if (value.endsWith("ies")
                && value.length() > 3) {

            value =
                    value.substring(
                            0,
                            value.length() - 3
                    ) + "y";

        } else if (value.endsWith("es")
                && value.length() > 3) {

            value =
                    value.substring(
                            0,
                            value.length() - 2
                    );

        } else if (value.endsWith("s")
                && value.length() > 3) {

            value =
                    value.substring(
                            0,
                            value.length() - 1
                    );
        }

        return value;
    }
}