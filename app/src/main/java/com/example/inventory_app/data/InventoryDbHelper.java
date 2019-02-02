package com.example.inventory_app.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.inventory_app.App;
import com.example.inventory_app.R;
import com.example.inventory_app.data.InventoryContract.InventoryEntry;

//database helper class for inventory app. manages db creation and versions
public class InventoryDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = InventoryDbHelper.class.getSimpleName();

    //name of the database file
    private static final String DATABASE_NAME = App.getContext().getResources().getString(R.string.inventory_db);

    //database version, traditionally set at 1. changed database will increment this number
    private static final int DATABASE_VERSION = 1;

    //construct a new instance of inventory dbhelper
    //param - context of the app
    public InventoryDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create a string that contains the SQL statement to create the inventory table
        String SQL_CREATE_INVENTORY_TABLE = "CREATE TABLE " + InventoryEntry.TABLE_NAME + " ("
                + InventoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + InventoryEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
                + InventoryEntry.COLUMN_PRICE + " INTEGER NOT NULL, "
                + InventoryEntry.COLUMN_QUANTITY + " INTEGER NOT NULL, "
                + InventoryEntry.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL, "
                + InventoryEntry.COLUMN_SUPPLIER_PHONE + " TEXT NOT NULL, ";

        //execute the sql statement
        db.execSQL(SQL_CREATE_INVENTORY_TABLE);

    }

    //this is called when the database needs to be upgraded to a new version
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    //TODO
    }
}
