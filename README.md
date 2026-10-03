# Smart Pantry Manager

## Overview

Smart Pantry Manager is an Android application developed using Java and Android Studio.

The application helps users manage ingredients stored in their pantry and discover recipes that can be prepared using the ingredients they currently have.

The application uses SQLite for persistent local storage.

## Main Features

### Pantry Management

Users can:

- Add pantry ingredients
- Edit existing ingredients
- Delete ingredients
- Store ingredient quantities
- Select measurement units
- Store expiry dates

### Input Validation

The application validates:

- Ingredient name
- Quantity
- Quantity must be greater than zero
- Quantity must be a valid number
- Expiry date format

Expiry dates use the format:

`YYYY-MM-DD`

### Recipe Suggestions

The application contains a collection of recipes stored in the SQLite database.

Recipes are suggested only when:

- All required ingredients are available
- The pantry quantity is sufficient
- Ingredient names match regardless of case
- Basic singular and plural differences are handled
- Measurement units match

### Recipe Details

Users can select a suggested recipe to view:

- Recipe name
- Required ingredients
- Required quantities
- Cooking instructions

### Settings

The Settings screen provides information about:

- Application name
- Application description
- Database technology
- Application version

## Technologies Used

- Java
- Android Studio
- Android SDK
- SQLite
- Android XML layouts
- ListView
- Custom ArrayAdapters

## Database

The application uses SQLite for local data storage.

The database contains tables for:

- Pantry items
- Recipes
- Recipe ingredients

The database is created and managed through the `DatabaseHelper` class.

## Application Screens

The application contains the following main screens:

1. Pantry List
2. Add/Edit Ingredient
3. Suggested Recipes
4. Recipe Details
5. Settings

## How to Run

1. Open the project in Android Studio.
2. Allow Gradle to finish syncing.
3. Connect an Android device or start an Android emulator.
4. Run the application.
5. Add ingredients to the pantry.
6. Open Suggested Recipes to view recipes that can be prepared.

## Testing

The application was tested for:

- Adding ingredients
- Editing ingredients
- Deleting ingredients
- Invalid quantity input
- Empty ingredient names
- Invalid expiry dates
- Recipe matching
- Insufficient ingredient quantities
- Recipe detail navigation
- Settings navigation
- Empty pantry display
- Persistent SQLite storage

## Project Structure

```text
SmartPantryManager
│
├── app
│   └── src
│       └── main
│           ├── java
│           │   └── com.example.smartpantrymanager
│           │
│           └── res
│               └── layout
│
├── README.md
└── settings.gradle