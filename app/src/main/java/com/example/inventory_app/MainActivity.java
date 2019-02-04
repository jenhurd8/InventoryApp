package com.example.inventory_app;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.*;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.inventory_app.data.InventoryContract;
import com.example.inventory_app.data.InventoryDbHelper;

//displays inventory list that has been entered and stored in the app
public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int INVENTORY_LOADER = 0;

    InventoryCursorAdapter mCursorAdapter;

    //database helper that will provide us with access to the database - m for member variable
   // private InventoryDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //to access the database, instantiate our subclass of sqLiteOpenHelper
        //and pass context - which is our current activity
        //mDbHelper = new InventoryDbHelper(this);

        //setup floating action button to open editor
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        //find the list view which will populate the inventory data
        ListView itemListView = (ListView) findViewById(R.id.list);

        //find and set the empty view so that it only shows when 0 items
        View emptyView = findViewById(R.id.empty_view);
        itemListView.setEmptyView(emptyView);

        //set adapter to create a list item for each row of data returned from the cursor
        mCursorAdapter = new InventoryCursorAdapter(this, null);
        //attach the adapter to the list view
        itemListView.setAdapter(mCursorAdapter);

        //set the click listener
        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //create new intent
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);

                //form the uri content specific to item clicked on by appending the id to the uri
                Uri currentItem = ContentUris.withAppendedId(InventoryContract.InventoryEntry.CONTENT_URI, id);

                //set the uri on the data field of the intent
                intent.setData(currentItem);

                //launch the editor activity to display the current item
                startActivity(intent);
            }
        });
        //start the loader
        getLoaderManager().initLoader(INVENTORY_LOADER, null, this);
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        displayDatabaseData();
//    }


    //temporary helper method to display info to the screen text view about the state of the pets database
    //and to verify working ok
    private void displayDatabaseData() {
        //create and/or open the database to read it
       // SQLiteDatabase db = mDbHelper.getReadableDatabase();

        //Define a projection (or view of desired content) from the database in your query
        String[] projection = {
                InventoryContract.InventoryEntry._ID,
                InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME,
                InventoryContract.InventoryEntry.COLUMN_PRICE,
                InventoryContract.InventoryEntry.COLUMN_QUANTITY,
                InventoryContract.InventoryEntry.COLUMN_SUPPLIER_NAME,
                InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE};

        //query of the inventory table
//        Cursor cursor = db.query(
//                InventoryContract.InventoryEntry.TABLE_NAME, //query table
//                projection,        //return columns chosen in projection above
//                null,     //WHERE clause would be listed here
//                null,  //values for WHERE clause
//                null,      //no group rows
//                null,       //no filter row group
//                null);      //sort order

        //this may be unused and need to be deleted
        //TODO: need to comment out cursor above and implement below
        Cursor cursor = getContentResolver().query(InventoryContract
                .InventoryEntry.CONTENT_URI, projection, null, null, null);



        // TextView displayView = (TextView) findViewById(R.id.text_view_inventory_item);

//        try {
//            //create a header in the text view that shows the cursor data
//            //in while loop, iterate through rows of the cursor and display in order
//
//            displayView.setText("The inventory table contents are: " + cursor.getCount() +
//                    " inventory. \n\n");
//            displayView.append(InventoryContract.InventoryEntry._ID + " - " +
//                    InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME + " - " +
//                    InventoryContract.InventoryEntry.COLUMN_PRICE + " - " +
//                    InventoryContract.InventoryEntry.COLUMN_QUANTITY + " - " +
//                    InventoryContract.InventoryEntry.COLUMN_SUPPLIER_NAME + " - " +
//                    InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE + "\n");
//
//            //find the index of the column
//            int idColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry._ID);
//            int productNameColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME);
//            int priceColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRICE);
//            int quantityColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_QUANTITY);
//            int supplierNameColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_NAME);
//            int supplierPhoneColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE);
//
//            //iterate through the items
//            while (cursor.moveToNext()) {
//                //use index to extract String or int at the current row cursor is on
//                int currentRowID = cursor.getInt(idColumnIndex);
//                String currentProductName = cursor.getString(productNameColumnIndex);
//                int currentProductPrice = cursor.getInt(priceColumnIndex);
//                int currentProductQuantity = cursor.getInt(quantityColumnIndex);
//                String currentSupplierName = cursor.getString(supplierNameColumnIndex);
//                String currentSupplierPhone = cursor.getString(supplierPhoneColumnIndex);
//                //displays values of the current row in the text view
//                displayView.append(("\n" + currentRowID + " - " +
//                        currentProductName + " - " +
//                        currentProductPrice + " - " +
//                        currentProductQuantity + " - " +
//                        currentSupplierName + " - " +
//                        currentSupplierPhone));
//            }
//
//        } finally {
//            //close the cursor when complete and release resources - makes invalid
//            cursor.close();
//        }
    }


    //helper method to test hard coded data, testing only
    private void insertInventoryItem() {
        //puts database in write-able mode
        //SQLiteDatabase db = mDbHelper.getWritableDatabase();

        //create contentValues object with column names as keys and attributes as values
        ContentValues values = new ContentValues();
        values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME, "Product XYZ");
        values.put(InventoryContract.InventoryEntry.COLUMN_PRICE, 123);
        values.put(InventoryContract.InventoryEntry.COLUMN_QUANTITY, 987);
        values.put(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_NAME, "Supplier Co");
        values.put(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE, "888-800-1234");

        //insert a new row for our test object
        //first argument is table name, second is column to insert null if Content value is empty,
        //third is content values object
        //long newInventoryTestRowId = db.insert(InventoryContract.InventoryEntry.TABLE_NAME, null, values);

        //use a content URI to allow us to access the data
        Uri newUri = getContentResolver().insert(InventoryContract.InventoryEntry.CONTENT_URI, values);
    }

    //method to delete all items
    private void deleteAllItems(){
        int rowsDeleted = getContentResolver().delete(InventoryContract.InventoryEntry.CONTENT_URI, null, null);
        Log.v("MainActivity", rowsDeleted + " rows deleted from the inventory database");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflates menu from res/menu/menu
        //adds menu to the app bar
        getMenuInflater().inflate(R.menu.menu_inventory, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //user clicked menu item in the app bar
        switch (item.getItemId()) {
            //response to click get dummy data
            case R.id.action_insert_test_data:
                insertInventoryItem();
                return true;
            //respond to click delete all entries
            case R.id.action_delete_test_entries:
                //do nothing at this time
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //define projection
        String[] projection = {
                InventoryContract.InventoryEntry._ID,
                InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME,
                InventoryContract.InventoryEntry.COLUMN_PRICE,
                InventoryContract.InventoryEntry.COLUMN_QUANTITY,
                InventoryContract.InventoryEntry.COLUMN_SUPPLIER_NAME,
                InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE};

                return new CursorLoader(this,
                        InventoryContract.InventoryEntry.CONTENT_URI,
                        projection,
                        null,
                        null,
                        null);
        }

    @Override
    public void onLoadFinished(Loader <Cursor> loader, Cursor data) {
    //update cursor with inventory data
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader <Cursor> loader) {
    //delete data from cursor
       mCursorAdapter.swapCursor(null);
    }
}
