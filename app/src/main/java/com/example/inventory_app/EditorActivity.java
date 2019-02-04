package com.example.inventory_app;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventory_app.data.InventoryContract;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private Uri mCurrentItemUri;

    //fields to edit item values
    private EditText mItemNameEditText;
    private EditText mItemPriceEditText;
    private EditText mItemQuantityEditText;
    private EditText mItemSupplierEditText;
    private EditText mItemSupplierPhoneEditText;

    //flag to keep track if item has changed
    private boolean mItemHasChanged;

    //on touch listener for view
    private View.OnTouchListener mTouchListener = new View.OnTouchListener(){
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent){
            mItemHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        Uri currentItemUri = ((Intent) intent).getData();

        //if intent does not contain an item URI then we are creating a new item
        if(currentItemUri == null){
            //this is a new item, so change the app bar to say add item
            setTitle(getString(R.string.add_item));
        }else {
            //otherwise if an existing pet, show edit item
            setTitle(getString(R.string.edit_item));
        }

        //find relevent views
        mItemNameEditText = (EditText) findViewById(R.id.edit_item_name);
        mItemPriceEditText = (EditText) findViewById(R.id.edit_item_price);
        mItemQuantityEditText = (EditText) findViewById(R.id.edit_item_quantity);
        mItemSupplierEditText = (EditText) findViewById(R.id.edit_item_supplier);
        mItemSupplierPhoneEditText = (EditText) findViewById(R.id.edit_item_supplier_name);

        mItemNameEditText.setOnTouchListener(mTouchListener);
        mItemPriceEditText.setOnTouchListener(mTouchListener);
        mItemQuantityEditText.setOnTouchListener(mTouchListener);
        mItemSupplierEditText.setOnTouchListener(mTouchListener);
        mItemSupplierPhoneEditText.setOnTouchListener(mTouchListener);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //since the editor shows all item attributes, define a projection
        String[] projection = {
                InventoryContract.InventoryEntry._ID,
                InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME,
                InventoryContract.InventoryEntry.COLUMN_PRICE,
                InventoryContract.InventoryEntry.COLUMN_QUANTITY,
                InventoryContract.InventoryEntry.COLUMN_SUPPLIER_NAME,
                InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE};

        return new CursorLoader(this,
                mCurrentItemUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

//    private void insertInventory(){
//       // read from inputs, use trim to remove leading and trailing whitespace
//        String nameString = mNameEditText.getText().toString().trim;
//
////        Create a contentValues object where column names are keys and inventory
////        attributes from the editor are the values
//        ContentValues values = new ContentValues();
//        values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME, nameString);
//
//        //insert a new inventory item into the provider returning the content uri for the new item
//        Uri newUri = getContentResolver.insert(InventoryContract.InventoryEntry.CONTENT_URI, values);
//
//        //Show a toast message to confirm if successful or show failure
//        if(newUri == null){
//            Toast.makeText(this, getString(R.string.editor_insert_item_failed),
//                    Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, getString(R.string.editor_insert_item_success),
//                    Toast.LENGTH_SHORT).show();
//        }
//    }
}
