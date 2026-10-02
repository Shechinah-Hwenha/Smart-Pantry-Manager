
package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private ListView listPantryItems;
    private PantryAdapter adapter;
    private ArrayList<PantryItem> pantryItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);

        listPantryItems = findViewById(R.id.listPantryItems);
        Button btnAddIngredient = findViewById(R.id.btnAddIngredient);
        Button btnSuggestedRecipes = findViewById(R.id.btnSuggestedRecipes);
        Button btnSettings = findViewById(R.id.btnSettings);

        pantryItems = new ArrayList<>();

        adapter = new PantryAdapter(this, pantryItems);
        listPantryItems.setAdapter(adapter);

        loadPantryItems();

        // Open the form to add a new ingredient
        btnAddIngredient.setOnClickListener(view -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    AddEditIngredientActivity.class
            );

            startActivity(intent);
        });

        // Tap an ingredient to edit it
        listPantryItems.setOnItemClickListener((parent, view, position, id) -> {

            PantryItem selectedItem = adapter.getItem(position);

            if (selectedItem != null) {
                Intent intent = new Intent(
                        MainActivity.this,
                        AddEditIngredientActivity.class
                );

                intent.putExtra("item_id", selectedItem.getId());

                startActivity(intent);
            }
        });

        // Long-press an ingredient to delete it
        listPantryItems.setOnItemLongClickListener(
                (parent, view, position, id) -> {

                    PantryItem selectedItem = adapter.getItem(position);

                    if (selectedItem != null) {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Delete Ingredient")
                                .setMessage(
                                        "Are you sure you want to delete "
                                                + selectedItem.getName() + "?"
                                )
                                .setPositiveButton("Delete", (dialog, which) -> {

                                    databaseHelper.deletePantryItem(
                                            selectedItem.getId()
                                    );

                                    loadPantryItems();

                                    Toast.makeText(
                                            MainActivity.this,
                                            "Ingredient deleted",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                })
                                .setNegativeButton("Cancel", null)
                                .show();
                    }

                    return true;
                }
        );

        btnSuggestedRecipes.setOnClickListener(view -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    SuggestedRecipesActivity.class
            );

            startActivity(intent);
        });
        

        btnSettings.setOnClickListener(view ->
                Toast.makeText(
                        this,
                        "Settings screen coming later",
                        Toast.LENGTH_SHORT
                ).show()
        );
    }

    // Load ingredients from the SQLite database
    private void loadPantryItems() {

        ArrayList<PantryItem> savedItems =
                databaseHelper.getAllPantryItems();

        adapter.clear();
        adapter.addAll(savedItems);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (databaseHelper != null && adapter != null) {
            loadPantryItems();
        }
    }
}