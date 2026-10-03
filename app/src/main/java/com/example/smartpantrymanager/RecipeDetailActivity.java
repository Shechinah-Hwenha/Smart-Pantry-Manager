package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView txtRecipeName;
    private TextView txtIngredients;
    private TextView txtInstructions;
    private Button btnBackToRecipes;

    private DatabaseHelper db;
    private int recipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        txtRecipeName = findViewById(R.id.txtRecipeName);
        txtIngredients = findViewById(R.id.txtIngredients);
        txtInstructions = findViewById(R.id.txtInstructions);
        btnBackToRecipes = findViewById(R.id.btnBackToRecipes);

        db = new DatabaseHelper(this);

        recipeId = getIntent().getIntExtra("recipe_id", -1);

        loadRecipe();

        btnBackToRecipes.setOnClickListener(view -> finish());
    }

    private void loadRecipe() {

        if (recipeId == -1) {
            txtRecipeName.setText("Recipe Not Found");
            txtIngredients.setText("");
            txtInstructions.setText("");
            return;
        }

        Recipe recipe = db.getRecipeById(recipeId);

        if (recipe == null) {
            txtRecipeName.setText("Recipe Not Found");
            txtIngredients.setText("");
            txtInstructions.setText("");
            return;
        }

        txtRecipeName.setText(recipe.getName());
        txtInstructions.setText(recipe.getInstructions());

        List<RecipeIngredient> ingredients =
                db.getRecipeIngredients(recipeId);

        StringBuilder ingredientText = new StringBuilder();

        for (RecipeIngredient ingredient : ingredients) {

            ingredientText.append("• ")
                    .append(ingredient.getIngredientName())
                    .append(" - ")
                    .append(ingredient.getRequiredQuantity())
                    .append(" ")
                    .append(ingredient.getUnit())
                    .append("\n");
        }

        txtIngredients.setText(ingredientText.toString());
    }
}