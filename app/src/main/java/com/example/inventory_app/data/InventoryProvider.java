package com.example.inventory_app.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

//content provider for inventory app
public class InventoryProvider extends ContentProvider {

    //database helper object
    private InventoryDbHelper mDbHelper;

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
        //Get readable database
//        SQLiteDatabase database = mDbHelper.getReadableDatabase();
//
//        //this cursor holds result of the query
//        Cursor cursor;
//
//        //try to match the URI to a specific code
//        int match = sUriMatcher.match(uri);
//        switch (match){
//            case INVENTORY:
//                //for INVENTORY query the table with the given
//                //projection, selection, sel args, and sort order
//                //could contain multiple rows. Perform query on the table
//                break;
//            case INVENTORY_ID:
//                //for INVENTORY_ID extract the ID from the URI
//                //example uri: "content://com.example.android.inventory_app/inventory/3"
//                //returns the 3rd row
//                //for every ? in the selection we need to have an argument to fill it
//                selection = InventoryContract.InventoryEntry._ID + "=?";
//                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri))};
//
//                //this will perform a query on the table where the _id will
//                //return a Cursor containing that row
//                cursor = database.query(InventoryContract.InventoryEntry.TABLE_NAME, projection,
//                        selection, selectionArgs, null, null, sortOrder);
//                break;
//                default:
//                    throw new IllegalArgumentException("Cannot query unknown URI " + uri);
//        }
//        return cursor;
        return null;
    }

    //insert new data into the provider with the given ContentValues
    @Override
    public Uri insert(Uri uri, ContentValues contentValues){
        //to be implemented later
//        final int match = sUriMatcher.match(uri);
//        switch (match){
//            case INVENTORY:
//                return insertInventory(uri, contentValues);
//            default:
//                throw new IllegalArgumentException("Insertion is not supported for this uri " + uri);
//        }
        return null;
    }

    //insert inventory into the database with given content. return the new content uri for that row
    private Uri insertInventory(Uri uri, ContentValues values){
        //get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        //insert a new inventory item into the table
        long id = database.insert(InventoryContract.InventoryEntry.TABLE_NAME, null, values);

        if(id ==-1){
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        //after insert find the new ID and return the URI with the ID appended
        return ContentUris.withAppendedId(uri, id);
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
