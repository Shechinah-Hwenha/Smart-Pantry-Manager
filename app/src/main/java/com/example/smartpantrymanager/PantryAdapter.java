package com.example.smartpantrymanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PantryAdapter extends ArrayAdapter<PantryItem> {

    public PantryAdapter(Context context, ArrayList<PantryItem> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_pantry,
                    parent,
                    false
            );
        }

        PantryItem item = getItem(position);

        TextView txtPantryName =
                convertView.findViewById(R.id.txtPantryName);

        TextView txtPantryQuantity =
                convertView.findViewById(R.id.txtPantryQuantity);

        TextView txtPantryExpiry =
                convertView.findViewById(R.id.txtPantryExpiry);

        if (item != null) {

            txtPantryName.setText(
                    item.getName()
            );

            txtPantryQuantity.setText(
                    "Quantity: "
                            + item.getQuantity()
                            + " "
                            + item.getUnit()
            );

            String expiryDate = item.getExpiryDate();

            if (expiryDate == null || expiryDate.isEmpty()) {

                txtPantryExpiry.setText(
                        "Expiry: Not specified"
                );

            } else {

                txtPantryExpiry.setText(
                        "Expiry: " + expiryDate
                );
            }
        }

        return convertView;
    }
}