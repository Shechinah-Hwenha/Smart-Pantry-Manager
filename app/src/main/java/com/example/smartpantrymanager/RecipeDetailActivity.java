package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RecipeDetailActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    private TextView txtRecipeName;
    private TextView txtIngredients;
    private TextView txtInstructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe_detail);

        databaseHelper = new DatabaseHelper(this);

        txtRecipeName =
                findViewById(R.id.txtRecipeName);

        txtIngredients =
                findViewById(R.id.txtIngredients);

        txtInstructions =
                findViewById(R.id.txtInstructions);

        int recipeId =
                getIntent().getIntExtra(
                        "recipe_id",
                        -1
                );

        if (recipeId == -1) {

            Toast.makeText(
                    this,
                    "Recipe not found",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

            return;
        }

        loadRecipe(recipeId);
    }

    private void loadRecipe(int recipeId) {

        Recipe recipe =
                databaseHelper.getRecipeById(recipeId);

        if (recipe == null) {

            Toast.makeText(
                    this,
                    "Recipe not found",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

            return;
        }

        txtRecipeName.setText(
                recipe.getName()
        );

        txtInstructions.setText(
                recipe.getInstructions()
        );

        ArrayList<RecipeIngredient> ingredients =
                databaseHelper.getRecipeIngredients(
                        recipeId
                );

        StringBuilder ingredientText =
                new StringBuilder();

        for (RecipeIngredient ingredient :
                ingredients) {

            ingredientText.append("• ")
                    .append(
                            ingredient.getIngredientName()
                    )
                    .append(" - ")
                    .append(
                            ingredient.getRequiredQuantity()
                    )
                    .append(" ")
                    .append(
                            ingredient.getUnit()
                    )
                    .append("\n");
        }

        txtIngredients.setText(
                ingredientText.toString()
        );
    }
}
