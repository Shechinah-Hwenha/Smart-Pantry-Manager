package com.example.smartpantrymanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class RecipeAdapter extends ArrayAdapter<Recipe> {

    private Context context;
    private ArrayList<Recipe> recipes;

    public RecipeAdapter(
            Context context,
            ArrayList<Recipe> recipes) {

        super(
                context,
                android.R.layout.simple_list_item_1,
                recipes
        );

        this.context = context;
        this.recipes = recipes;
    }

    @Override
    public View getView(
            int position,
            View convertView,
            ViewGroup parent) {

        if (convertView == null) {

            convertView =
                    LayoutInflater.from(context)
                            .inflate(
                                    android.R.layout.simple_list_item_1,
                                    parent,
                                    false
                            );
        }

        TextView textView =
                convertView.findViewById(
                        android.R.id.text1
                );

        Recipe recipe =
                recipes.get(position);

        textView.setText(
                recipe.getName()
        );

        textView.setTextSize(18);

        return convertView;
    }
}