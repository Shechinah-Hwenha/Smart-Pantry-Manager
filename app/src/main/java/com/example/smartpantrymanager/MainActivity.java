
package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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

        btnAddIngredient.setOnClickListener(view -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    AddEditIngredientActivity.class
            );

            startActivity(intent);
        });

        btnSuggestedRecipes.setOnClickListener(view ->
                Toast.makeText(
                        this,
                        "Suggested Recipes screen coming later",
                        Toast.LENGTH_SHORT
                ).show()
        );

        btnSettings.setOnClickListener(view ->
                Toast.makeText(
                        this,
                        "Settings screen coming later",
                        Toast.LENGTH_SHORT
                ).show()
        );
    }

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