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
                    android.R.layout.simple_list_item_2,
                    parent,
                    false
            );
        }

        PantryItem item = getItem(position);

        TextView title = convertView.findViewById(android.R.id.text1);
        TextView details = convertView.findViewById(android.R.id.text2);

        if (item != null) {
            title.setText(item.getName());

            details.setText(
                    "Quantity: " + item.getQuantity() + " " + item.getUnit()
                            + "\nExpiry: "
                            + (item.getExpiryDate() == null
                            || item.getExpiryDate().isEmpty()
                            ? "Not specified"
                            : item.getExpiryDate())
            );
        }

        return convertView;
    }
}
