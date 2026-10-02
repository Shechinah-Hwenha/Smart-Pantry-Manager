package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SmartPantry.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL(
                "CREATE TABLE pantry_items (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL, " +
                        "quantity REAL NOT NULL, " +
                        "unit TEXT NOT NULL, " +
                        "expiry_date TEXT" +
                        ")"
        );


        db.execSQL(
                "CREATE TABLE recipes (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL, " +
                        "instructions TEXT NOT NULL" +
                        ")"
        );


        db.execSQL(
                "CREATE TABLE recipe_ingredients (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "recipe_id INTEGER NOT NULL, " +
                        "ingredient_name TEXT NOT NULL, " +
                        "required_quantity REAL NOT NULL, " +
                        "unit TEXT NOT NULL, " +
                        "FOREIGN KEY(recipe_id) REFERENCES recipes(id)" +
                        ")"
        );

        // Add the starter recipes
        seedRecipes(db);
    }



    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM recipes",
                null
        );

        if (cursor.moveToFirst()) {

            int recipeCount = cursor.getInt(0);

            if (recipeCount == 0) {
                seedRecipes(db);
            }
        }

        cursor.close();
    }

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion) {

        db.execSQL(
                "DROP TABLE IF EXISTS recipe_ingredients"
        );

        db.execSQL(
                "DROP TABLE IF EXISTS recipes"
        );

        db.execSQL(
                "DROP TABLE IF EXISTS pantry_items"
        );

        onCreate(db);
    }



    public boolean addPantryItem(PantryItem item) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put("name", item.getName());
        values.put("quantity", item.getQuantity());
        values.put("unit", item.getUnit());
        values.put("expiry_date", item.getExpiryDate());

        long result =
                db.insert(
                        "pantry_items",
                        null,
                        values
                );

        return result != -1;
    }



    public ArrayList<PantryItem> getAllPantryItems() {

        ArrayList<PantryItem> itemList =
                new ArrayList<>();

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT * FROM pantry_items ORDER BY name ASC",
                        null
                );

        if (cursor.moveToFirst()) {

            do {

                int id =
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow("id")
                        );

                String name =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("name")
                        );

                double quantity =
                        cursor.getDouble(
                                cursor.getColumnIndexOrThrow("quantity")
                        );

                String unit =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("unit")
                        );

                String expiryDate =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("expiry_date")
                        );

                PantryItem item =
                        new PantryItem(
                                id,
                                name,
                                quantity,
                                unit,
                                expiryDate
                        );

                itemList.add(item);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return itemList;
    }


    public PantryItem getPantryItemById(int id) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT * FROM pantry_items WHERE id = ?",
                        new String[]{
                                String.valueOf(id)
                        }
                );

        PantryItem item = null;

        if (cursor.moveToFirst()) {

            String name =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow("name")
                    );

            double quantity =
                    cursor.getDouble(
                            cursor.getColumnIndexOrThrow("quantity")
                    );

            String unit =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow("unit")
                    );

            String expiryDate =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow("expiry_date")
                    );

            item =
                    new PantryItem(
                            id,
                            name,
                            quantity,
                            unit,
                            expiryDate
                    );
        }

        cursor.close();

        return item;
    }


    public boolean updatePantryItem(PantryItem item) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put("name", item.getName());
        values.put("quantity", item.getQuantity());
        values.put("unit", item.getUnit());
        values.put("expiry_date", item.getExpiryDate());

        int result =
                db.update(
                        "pantry_items",
                        values,
                        "id = ?",
                        new String[]{
                                String.valueOf(item.getId())
                        }
                );

        return result > 0;
    }



    public boolean deletePantryItem(int id) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        int result =
                db.delete(
                        "pantry_items",
                        "id = ?",
                        new String[]{
                                String.valueOf(id)
                        }
                );

        return result > 0;
    }



    private void seedRecipes(SQLiteDatabase db) {

        addRecipe(
                db,
                "Tomato Pasta",
                "Cook pasta. Prepare tomato sauce with tomatoes, onion and garlic. Mix together and serve.",
                new String[][]{
                        {"Pasta", "200", "g"},
                        {"Tomatoes", "2", "units"},
                        {"Onion", "1", "units"},
                        {"Garlic", "2", "units"}
                }
        );

        addRecipe(
                db,
                "Chicken Rice",
                "Cook rice and chicken separately. Combine with onion and garlic and serve.",
                new String[][]{
                        {"Rice", "200", "g"},
                        {"Chicken", "200", "g"},
                        {"Onion", "1", "units"},
                        {"Garlic", "1", "units"}
                }
        );

        addRecipe(
                db,
                "Vegetable Stir Fry",
                "Stir fry vegetables in a pan and serve with cooked rice.",
                new String[][]{
                        {"Rice", "200", "g"},
                        {"Carrots", "2", "units"},
                        {"Onion", "1", "units"},
                        {"Peppers", "1", "units"}
                }
        );

        addRecipe(
                db,
                "Beef Sandwich",
                "Cook beef and place inside bread with lettuce and tomatoes.",
                new String[][]{
                        {"Bread", "2", "units"},
                        {"Beef", "150", "g"},
                        {"Lettuce", "50", "g"},
                        {"Tomatoes", "1", "units"}
                }
        );

        addRecipe(
                db,
                "Chicken Sandwich",
                "Cook chicken and place inside bread with lettuce and mayonnaise.",
                new String[][]{
                        {"Bread", "2", "units"},
                        {"Chicken", "150", "g"},
                        {"Lettuce", "50", "g"},
                        {"Mayonnaise", "20", "ml"}
                }
        );

        addRecipe(
                db,
                "Omelette",
                "Beat eggs with onion and peppers. Cook in a pan until firm.",
                new String[][]{
                        {"Eggs", "2", "units"},
                        {"Onion", "1", "units"},
                        {"Peppers", "1", "units"}
                }
        );

        addRecipe(
                db,
                "French Toast",
                "Dip bread in beaten eggs and fry until golden.",
                new String[][]{
                        {"Bread", "2", "units"},
                        {"Eggs", "2", "units"},
                        {"Milk", "100", "ml"}
                }
        );

        addRecipe(
                db,
                "Pancakes",
                "Mix flour, milk and eggs into a batter. Cook pancakes in a pan.",
                new String[][]{
                        {"Flour", "200", "g"},
                        {"Milk", "250", "ml"},
                        {"Eggs", "2", "units"}
                }
        );

        addRecipe(
                db,
                "Chicken Curry",
                "Cook chicken with onion, tomatoes and curry powder. Simmer until done.",
                new String[][]{
                        {"Chicken", "300", "g"},
                        {"Onion", "1", "units"},
                        {"Tomatoes", "2", "units"},
                        {"Curry Powder", "10", "g"}
                }
        );

        addRecipe(
                db,
                "Beef Stew",
                "Cook beef slowly with potatoes, carrots and onion until tender.",
                new String[][]{
                        {"Beef", "300", "g"},
                        {"Potatoes", "3", "units"},
                        {"Carrots", "2", "units"},
                        {"Onion", "1", "units"}
                }
        );

        addRecipe(
                db,
                "Tuna Pasta",
                "Cook pasta and mix with tuna and mayonnaise.",
                new String[][]{
                        {"Pasta", "200", "g"},
                        {"Tuna", "1", "can"},
                        {"Mayonnaise", "30", "ml"}
                }
        );

        addRecipe(
                db,
                "Egg Salad",
                "Boil eggs and mix with lettuce, tomatoes and mayonnaise.",
                new String[][]{
                        {"Eggs", "2", "units"},
                        {"Lettuce", "50", "g"},
                        {"Tomatoes", "1", "units"},
                        {"Mayonnaise", "20", "ml"}
                }
        );

        addRecipe(
                db,
                "Rice and Beans",
                "Cook rice and beans together with onion and tomatoes.",
                new String[][]{
                        {"Rice", "200", "g"},
                        {"Beans", "1", "can"},
                        {"Onion", "1", "units"},
                        {"Tomatoes", "1", "units"}
                }
        );

        addRecipe(
                db,
                "Potato Omelette",
                "Cook potatoes and onion. Add beaten eggs and cook until firm.",
                new String[][]{
                        {"Potatoes", "3", "units"},
                        {"Eggs", "3", "units"},
                        {"Onion", "1", "units"}
                }
        );

        addRecipe(
                db,
                "Tomato Soup",
                "Cook tomatoes, onion and garlic. Blend until smooth and serve.",
                new String[][]{
                        {"Tomatoes", "4", "units"},
                        {"Onion", "1", "units"},
                        {"Garlic", "2", "units"}
                }
        );
    }



    public ArrayList<Recipe> getAllRecipes() {

        ArrayList<Recipe> recipeList =
                new ArrayList<>();

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM recipes ORDER BY name ASC",
                null
        );

        if (cursor.moveToFirst()) {

            do {

                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow("id")
                );

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow("name")
                );

                String instructions = cursor.getString(
                        cursor.getColumnIndexOrThrow("instructions")
                );

                Recipe recipe = new Recipe(
                        id,
                        name,
                        instructions
                );

                recipeList.add(recipe);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return recipeList;
    }



    public Recipe getRecipeById(int id) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM recipes WHERE id = ?",
                new String[]{
                        String.valueOf(id)
                }
        );

        Recipe recipe = null;

        if (cursor.moveToFirst()) {

            String name = cursor.getString(
                    cursor.getColumnIndexOrThrow("name")
            );

            String instructions = cursor.getString(
                    cursor.getColumnIndexOrThrow("instructions")
            );

            recipe = new Recipe(
                    id,
                    name,
                    instructions
            );
        }

        cursor.close();

        return recipe;
    }



    private void addRecipe(
            SQLiteDatabase db,
            String name,
            String instructions,
            String[][] ingredients) {

        ContentValues recipeValues =
                new ContentValues();

        recipeValues.put("name", name);
        recipeValues.put(
                "instructions",
                instructions
        );

        long recipeId =
                db.insert(
                        "recipes",
                        null,
                        recipeValues
                );

        if (recipeId == -1) {
            return;
        }

        for (String[] ingredient : ingredients) {

            ContentValues ingredientValues =
                    new ContentValues();

            ingredientValues.put(
                    "recipe_id",
                    recipeId
            );

            ingredientValues.put(
                    "ingredient_name",
                    ingredient[0]
            );

            ingredientValues.put(
                    "required_quantity",
                    Double.parseDouble(ingredient[1])
            );

            ingredientValues.put(
                    "unit",
                    ingredient[2]
            );

            db.insert(
                    "recipe_ingredients",
                    null,
                    ingredientValues
            );
        }
    }




    public ArrayList<RecipeIngredient> getRecipeIngredients(int recipeId) {

        ArrayList<RecipeIngredient> ingredientList =
                new ArrayList<>();

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM recipe_ingredients " +
                        "WHERE recipe_id = ?",
                new String[]{
                        String.valueOf(recipeId)
                }
        );

        if (cursor.moveToFirst()) {

            do {

                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow("id")
                );

                int savedRecipeId = cursor.getInt(
                        cursor.getColumnIndexOrThrow("recipe_id")
                );

                String ingredientName = cursor.getString(
                        cursor.getColumnIndexOrThrow("ingredient_name")
                );

                double requiredQuantity = cursor.getDouble(
                        cursor.getColumnIndexOrThrow("required_quantity")
                );

                String unit = cursor.getString(
                        cursor.getColumnIndexOrThrow("unit")
                );

                RecipeIngredient ingredient =
                        new RecipeIngredient(
                                id,
                                savedRecipeId,
                                ingredientName,
                                requiredQuantity,
                                unit
                        );

                ingredientList.add(ingredient);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return ingredientList;
    }
}