package com.example.inventory_app.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

//content provider for inventory app
public class InventoryProvider extends ContentProvider {

    InventoryDbHelper mDbHelper;

    //tag for log messages
    public static final String LOG_TAG = InventoryProvider.class.getSimpleName();

    //initializes the provider and the database helper object
    @Override
    public boolean onCreate(){
        return true;
    }

    //query the given URI, use given projection, selection, sel. args, and sort order
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder){
        return null;
    }

    //insert new data into the provider with the given ContentValues
    @Override
    public Uri insert(Uri uri, ContentValues contentValues){
        return null;
    }

    //updates data at the given selection with the new ContentValues
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs){
        return 0;
    }

    //deletes the data at the given selection and arguments
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs){
        return 0;
    }

    //returns the MIME type of data for the content URI
    @Override
    public String getType(Uri uri){
        return null;
    }

}
