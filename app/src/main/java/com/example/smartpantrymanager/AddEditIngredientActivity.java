package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddEditIngredientActivity extends AppCompatActivity {

    private EditText edtName;
    private EditText edtQuantity;
    private EditText edtExpiryDate;
    private Spinner spinnerUnit;

    private DatabaseHelper databaseHelper;

    // ID of the ingredient being edited
    // -1 means we are adding a new ingredient
    private int itemId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_edit_ingredient);

        databaseHelper = new DatabaseHelper(this);

        edtName = findViewById(R.id.edtName);
        edtQuantity = findViewById(R.id.edtQuantity);
        edtExpiryDate = findViewById(R.id.edtExpiryDate);
        spinnerUnit = findViewById(R.id.spinnerUnit);

        TextView txtTitle = findViewById(R.id.txtTitle);

        Button btnSaveIngredient =
                findViewById(R.id.btnSaveIngredient);

        Button btnCancel =
                findViewById(R.id.btnCancel);

        // Available measurement units
        String[] units = {
                "units",
                "g",
                "kg",
                "ml",
                "L",
                "pack",
                "can"
        };

        ArrayAdapter<String> unitAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        units
                );

        unitAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerUnit.setAdapter(unitAdapter);

        // Check if an ingredient ID was sent from MainActivity
        if (getIntent().hasExtra("item_id")) {

            itemId = getIntent().getIntExtra(
                    "item_id",
                    -1
            );

            // Change the title when editing
            txtTitle.setText("Edit Ingredient");

            // Load the existing ingredient
            loadIngredient();
        }

        // Save button
        btnSaveIngredient.setOnClickListener(
                view -> saveIngredient()
        );

        // Cancel button
        btnCancel.setOnClickListener(
                view -> finish()
        );
    }



    private void loadIngredient() {

        PantryItem item =
                databaseHelper.getPantryItemById(itemId);

        if (item == null) {

            Toast.makeText(
                    this,
                    "Ingredient not found",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

            return;
        }

        // Put saved values into the form
        edtName.setText(item.getName());

        edtQuantity.setText(
                String.valueOf(item.getQuantity())
        );

        edtExpiryDate.setText(
                item.getExpiryDate()
        );

        // Select the saved unit in the Spinner
        String savedUnit = item.getUnit();

        ArrayAdapter<String> adapter =
                (ArrayAdapter<String>) spinnerUnit.getAdapter();

        int position =
                adapter.getPosition(savedUnit);

        if (position >= 0) {

            spinnerUnit.setSelection(position);
        }
    }


    private void saveIngredient() {

        String name =
                edtName.getText()
                        .toString()
                        .trim();

        String quantityText =
                edtQuantity.getText()
                        .toString()
                        .trim();

        String unit =
                spinnerUnit.getSelectedItem()
                        .toString();

        String expiryDate =
                edtExpiryDate.getText()
                        .toString()
                        .trim();

        // Validate name
        if (name.isEmpty()) {

            edtName.setError(
                    "Please enter an ingredient name"
            );

            edtName.requestFocus();

            return;
        }

        // Validate quantity
        if (quantityText.isEmpty()) {

            edtQuantity.setError(
                    "Please enter a quantity"
            );

            edtQuantity.requestFocus();

            return;
        }

        double quantity;

        try {

            quantity =
                    Double.parseDouble(quantityText);

        } catch (NumberFormatException e) {

            edtQuantity.setError(
                    "Please enter a valid number"
            );

            edtQuantity.requestFocus();

            return;
        }

        // Quantity must be greater than zero
        if (quantity <= 0) {

            edtQuantity.setError(
                    "Quantity must be greater than zero"
            );

            edtQuantity.requestFocus();

            return;
        }

        // Create the PantryItem
        PantryItem item =
                new PantryItem(
                        itemId,
                        name,
                        quantity,
                        unit,
                        expiryDate
                );

        boolean success;



        if (itemId == -1) {

            success =
                    databaseHelper.addPantryItem(item);

            if (success) {

                Toast.makeText(
                        this,
                        "Ingredient added successfully",
                        Toast.LENGTH_SHORT
                ).show();

                finish();

            } else {

                Toast.makeText(
                        this,
                        "Failed to add ingredient",
                        Toast.LENGTH_SHORT
                ).show();
            }

        }



        else {

            success =
                    databaseHelper.updatePantryItem(item);

            if (success) {

                Toast.makeText(
                        this,
                        "Ingredient updated successfully",
                        Toast.LENGTH_SHORT
                ).show();

                finish();

            } else {

                Toast.makeText(
                        this,
                        "Failed to update ingredient",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }
}