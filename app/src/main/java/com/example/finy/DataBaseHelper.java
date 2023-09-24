/*package com.example.finy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static DataBaseHelper sInstance;
    public static final String FOOD_CALORIES = "Food_calories";
    public static final String FOOD_NAME = "Food name";
    public static final String CALORIES = "Calories";
    public static synchronized DataBaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DataBaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }
    public DataBaseHelper(@Nullable Context context) {
        super(context, "/data/data/com.example.finy/databases/Food_calories.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE FOOD_CALORIES (" +
                " ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " FOOD_NAME TEXT, " +
                " CALORIES NUMERIC)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + FOOD_CALORIES);
            onCreate(db);
        }
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }


    /*public boolean addOne(FoodCalories foodCalories) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(FOOD_NAME, foodCalories.getCalories());
        cv.put(CALORIES, foodCalories.getCalories());
        long insert = db.insert(FOOD_CALORIES, null, cv);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }



    public String getCaloriesFromDb(String foodName) {
        String calories = "";
        String selectFoodName = "SELECT CALORIES FROM FOOD_CALORIES WHERE FOOD_NAME = ?";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectFoodName, new String[]{foodName});
        try {
            if (cursor.moveToFirst()) {
                calories = cursor.getString(cursor.getColumnIndexOrThrow(CALORIES));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            cursor.close();
            db.close();
        }
        return calories;
    }
}
*/

