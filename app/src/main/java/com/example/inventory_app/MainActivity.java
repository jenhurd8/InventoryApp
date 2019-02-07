package com.example.inventory_app;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.*;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.inventory_app.data.InventoryContract;

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

        //setup floating action button to open editor
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
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
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //create new intent
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);

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

    //helper method to test hard coded data, testing only
    private void insertInventoryItem() {
        //puts database in write-able mode
        //SQLiteDatabase db = mDbHelper.getWritableDatabase();

        //create contentValues object with column names as keys and attributes as values
        ContentValues values = new ContentValues();
        values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME, getString(R.string.generic_item));
        values.put(InventoryContract.InventoryEntry.COLUMN_PRICE, 100);
        values.put(InventoryContract.InventoryEntry.COLUMN_QUANTITY, 2);
        values.put(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_NAME, getString(R.string.supplier_co));
        values.put(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE, getString(R.string.generic_phone));

        //use a content URI to allow us to access the data
        Uri newUri = getContentResolver().insert(InventoryContract.InventoryEntry.CONTENT_URI, values);
    }

    //method to delete all items
    private void deleteAllItems(){
        int rowsDeleted = getContentResolver().delete(InventoryContract.InventoryEntry.CONTENT_URI, null, null);
        Log.v("MainActivity", rowsDeleted + getString(R.string.rows_deleted));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflates menu from res/menu/menu
        //adds menu to the app bar
        getMenuInflater().inflate(R.menu.menu_inventory, menu);

        MenuItem delete = menu.findItem(R.id.delete);
        delete.setVisible(false);

        MenuItem menuItem3 = menu.findItem(R.id.item_completed);
        menuItem3.setVisible(false);
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
                deleteAllItems();
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
