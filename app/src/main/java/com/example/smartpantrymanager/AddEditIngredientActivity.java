package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddEditIngredientActivity extends AppCompatActivity {

    private EditText edtName;
    private EditText edtQuantity;
    private EditText edtExpiryDate;
    private Spinner spinnerUnit;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_edit_ingredient);

        databaseHelper = new DatabaseHelper(this);

        edtName = findViewById(R.id.edtName);
        edtQuantity = findViewById(R.id.edtQuantity);
        edtExpiryDate = findViewById(R.id.edtExpiryDate);
        spinnerUnit = findViewById(R.id.spinnerUnit);

        Button btnSaveIngredient =
                findViewById(R.id.btnSaveIngredient);

        Button btnCancel =
                findViewById(R.id.btnCancel);

        // Units available for pantry ingredients
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

        // Save button
        btnSaveIngredient.setOnClickListener(view ->
                saveIngredient()
        );

        // Cancel button
        btnCancel.setOnClickListener(view ->
                finish()
        );
    }

    private void saveIngredient() {

        String name = edtName.getText()
                .toString()
                .trim();

        String quantityText = edtQuantity.getText()
                .toString()
                .trim();

        String unit = spinnerUnit
                .getSelectedItem()
                .toString();

        String expiryDate = edtExpiryDate
                .getText()
                .toString()
                .trim();

        // Validation: ingredient name
        if (name.isEmpty()) {

            edtName.setError(
                    "Please enter an ingredient name"
            );

            edtName.requestFocus();

            return;
        }

        // Validation: quantity
        if (quantityText.isEmpty()) {

            edtQuantity.setError(
                    "Please enter a quantity"
            );

            edtQuantity.requestFocus();

            return;
        }

        double quantity;

        try {

            quantity = Double.parseDouble(quantityText);

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

        // Create pantry item
        PantryItem item = new PantryItem(
                0,
                name,
                quantity,
                unit,
                expiryDate
        );

        // Save to SQLite
        boolean success =
                databaseHelper.addPantryItem(item);

        if (success) {

            Toast.makeText(
                    this,
                    "Ingredient saved successfully",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

        } else {

            Toast.makeText(
                    this,
                    "Failed to save ingredient",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}
