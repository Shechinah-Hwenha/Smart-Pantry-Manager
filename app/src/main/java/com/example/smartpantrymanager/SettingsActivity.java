
package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private TextView txtAppName;
    private TextView txtAppDescription;
    private TextView txtDatabaseInfo;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        txtAppName = findViewById(R.id.txtAppName);
        txtAppDescription =
                findViewById(R.id.txtAppDescription);
        txtDatabaseInfo =
                findViewById(R.id.txtDatabaseInfo);

        btnBack = findViewById(R.id.btnBack);

        txtAppName.setText("Smart Pantry Manager");

        txtAppDescription.setText(
                "Manage your pantry ingredients, " +
                        "reduce food waste, and discover recipes " +
                        "you can prepare with what you have."
        );

        txtDatabaseInfo.setText(
                "Storage: SQLite Database\n" +
                        "Version: 1.0\n" +
                        "Developer: Smart Pantry Manager Team"
        );

        btnBack.setOnClickListener(view -> finish());
    }
}