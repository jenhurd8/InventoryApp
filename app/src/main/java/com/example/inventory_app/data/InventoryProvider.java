package com.example.inventory_app.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

//content provider for inventory app
public class InventoryProvider extends ContentProvider {

    //tag for log messages
    public static final String LOG_TAG = InventoryProvider.class.getSimpleName();

    //uri matcher for contents of the inventory table
    private static final int INVENTORY = 100;

    //uri matcher for returning a single item from the inventory table
    private static final int INVENTORY_ID = 101;

    //uri matcher to match to a corresponding code
    //input passed into the constructor represents the root uri, its common to use NO_MATCH as the input
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    //static initializer, runs the first time anything is called from this class
    static {
        //call to add URI goes here, all paths added with corresponding code return match

        //content of the form "content://com.example.android.inventory_app/inventory" will map
        //to the integer code. this uri provides access to multiple rows of the table
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_INVENTORY, INVENTORY);

        //content of the form "content://com.example.android.inventory_app/inventory/#" will map
        //to the integer code for one single row of the inventory table. # is the wildcard to match
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_INVENTORY + "/#",
                INVENTORY_ID);
    }

    //database helper object
    private InventoryDbHelper mDbHelper;

    //initializes the provider and the database helper object
    @Override
    public boolean onCreate(){
        mDbHelper = new InventoryDbHelper(getContext());
        return true;
    }

    //query the given URI, use given projection, selection, sel. args, and sort order
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder){
        //Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        //this cursor holds result of the query
        Cursor cursor;

       // try to match the URI to a specific code
        int match = sUriMatcher.match(uri);

        switch (match){
            case INVENTORY:
                //for INVENTORY query the table with the given
                //projection, selection, sel args, and sort order
                //could contain multiple rows. Perform query on the table
                cursor = database.query(InventoryContract.InventoryEntry.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            case INVENTORY_ID:
                //for INVENTORY_ID extract the ID from the URI
                //example uri: "content://com.example.android.inventory_app/inventory/3"
                //returns the 3rd row
                //for every ? in the selection we need to have an argument to fill it
                selection = InventoryContract.InventoryEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri))};

                //this will perform a query on the table where the _id will
                //return a Cursor containing that row
                cursor = database.query(InventoryContract.InventoryEntry.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
                default:
                    throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        //set notification uri on the cursor so we know what content URI the cursor was created for
        //if the data at this URI changes, then we update the cursor

        return cursor;

    }

    //insert new data into the provider with the given ContentValues
    @Override
    public Uri insert(Uri uri, ContentValues contentValues){
        //to be implemented later
        final int match = sUriMatcher.match(uri);
        switch (match){
            case INVENTORY:
                return insertInventory(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for this uri " + uri);
        }
    }

    //insert inventory into the database with given content. return the new content uri for that row
    private Uri insertInventory(Uri uri, ContentValues values){
        //check name is not null
        String name = values.getAsString(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME);
        if(name == null){
            throw new IllegalArgumentException("Inventory item requires a name");
        }

        //check price is valid
        Integer price = values.getAsInteger(InventoryContract.InventoryEntry.COLUMN_PRICE);
        if(price == null || price < 0){
            throw new IllegalArgumentException("Price must be over 0");
        }

        //check quantity is valid
        Integer quantity = values.getAsInteger(InventoryContract.InventoryEntry.COLUMN_QUANTITY);
        if(quantity== null || quantity < 0){
            throw new IllegalArgumentException("Quantity cannot be negative or empty");
        }

        //check supplier is valid
        String supplier = values.getAsString(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_NAME);
        if(supplier == null){
            throw new IllegalArgumentException("Supplier requires a name");
        }

        //check supplier phone has data
        String supplierPhone = values.getAsString(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE);
        if(supplierPhone == null){
            throw new IllegalArgumentException("Supplier phone requires information");
        }

        //get write-able database
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
        final int match = sUriMatcher.match(uri);
        switch(match){
            case INVENTORY:
                return updateInventory(uri, contentValues, selection, selectionArgs);
            case INVENTORY_ID:
                selection = InventoryContract.InventoryEntry._ID + "=?";
                return updateInventory(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported " + uri);
        }
    }

    //updates pets in the database with given content values
    //applies the changes in the specified rows (one or more)
    //return the number of rows that were successfully updated
    private int updateInventory(Uri uri, ContentValues values, String selection, String[] selectionArgs){
        //if inventoryEntry# is present, check that the name value is not null
        if(values.containsKey(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME)){
            String name = values.getAsString(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME);
            if (name == null){
                throw new IllegalArgumentException("Inventory item requires a name");
            }
        }
        //if inventoryEntry# is present, check that the price value is not null
        if(values.containsKey(InventoryContract.InventoryEntry.COLUMN_PRICE)){
            Integer price = values.getAsInteger(InventoryContract.InventoryEntry.COLUMN_PRICE);
            if (price == null){
                throw new IllegalArgumentException("Price must be entered");
            }
        }
        //if inventoryEntry# is present, check that the quantity value is not null
        if(values.containsKey(InventoryContract.InventoryEntry.COLUMN_QUANTITY)){
            Integer quantity = values.getAsInteger(InventoryContract.InventoryEntry.COLUMN_QUANTITY);
            if (quantity == null){
                throw new IllegalArgumentException("Quantity must be entered");
            }
        }
        //if inventoryEntry# is present, check that the supplier value is not null
        if(values.containsKey(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_NAME)){
            String supplier = values.getAsString(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_NAME);
            if (supplier == null){
                throw new IllegalArgumentException("Supplier requires a name");
            }
        }
        //if inventoryEntry# is present, check that the supplier phone is not null
        if(values.containsKey(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE)){
            String supplierPhone = values.getAsString(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE);
            if (supplierPhone == null){
                throw new IllegalArgumentException("Supplier phone requires info");
            }
        }

    //if no values updated, do not update the database
        if(values.size() == 0){
            return 0;
        }

        //otherwise, get the write-able database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        //perform the update on the database and get the rows affected
        int rowsUpdated = database.update(InventoryContract.InventoryEntry.TABLE_NAME, values, selection, selectionArgs);

        //if one or more rows updated, notify the listeners the data has changed
        if(rowsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    //deletes the data at the given selection and arguments
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs){
       //get write-able database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        //track the rows that were deleted
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match){
            case INVENTORY:
                //delete rows that match the selection args
                rowsDeleted = database.delete(InventoryContract.InventoryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case INVENTORY_ID:
                //delete the specified row from the uri
                selection = InventoryContract.InventoryEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(InventoryContract.InventoryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        //if one or more rows deleted, notify listeners
        if(rowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    //returns the MIME type of data for the content URI
    @Override
    public String getType(Uri uri){
        final int match = sUriMatcher.match(uri);
        switch (match){
            case INVENTORY:
                return InventoryContract.InventoryEntry.CONTENT_LIST_TYPE;
            case INVENTORY_ID:
                return InventoryContract.InventoryEntry.CONTENT_ITEM_TYPE;
                default:
                    throw new IllegalArgumentException("Unknown URI " + uri + " with match " + match);
        }
    }

}
