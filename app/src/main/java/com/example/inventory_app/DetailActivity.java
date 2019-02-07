package com.example.inventory_app;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;

import com.example.inventory_app.data.InventoryContract;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    //inventory loader identifier
    private static final int EXISTING_INVENTORY_LOADER = 0;

    //content uri for the existing item, null if its a new item
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        mCurrentItemUri = intent.getData();

        //if intent does not contain an item URI then we are creating a new item
        if (mCurrentItemUri == null) {
            //this is a new item, so change the app bar to say add item
            setTitle(getString(R.string.add_item));
        } else {
            //otherwise if an existing pet, show edit item
            setTitle(getString(R.string.edit_item));

            //initialize the loader to read the item data from the database
            //and display the current values in the editor
            getLoaderManager().initLoader(EXISTING_INVENTORY_LOADER, null, this);
        }

        //find relevant views
        mItemNameEditText = (EditText) findViewById(R.id.edit_item_name);
        mItemPriceEditText = (EditText) findViewById(R.id.edit_item_price);
        mItemQuantityEditText = (EditText) findViewById(R.id.edit_item_quantity);
        mItemSupplierEditText = (EditText) findViewById(R.id.edit_item_supplier_name);
        mItemSupplierPhoneEditText = (EditText) findViewById(R.id.edit_supplier_phone_number);

        mItemNameEditText.setOnTouchListener(mTouchListener);
        mItemPriceEditText.setOnTouchListener(mTouchListener);
        mItemQuantityEditText.setOnTouchListener(mTouchListener);
        mItemSupplierEditText.setOnTouchListener(mTouchListener);
        mItemSupplierPhoneEditText.setOnTouchListener(mTouchListener);

        Button callSupplier = (Button) findViewById(R.id.call_supplier);

        callSupplier.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String numberToCall = mItemSupplierPhoneEditText.getText().toString();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + numberToCall));
                startActivity(intent);
            }
        });

    }


    private void saveInventory(){
        // read from inputs, use trim to remove leading and trailing whitespace

        String nameString = mItemNameEditText.getText().toString().trim();
//        if(nameString == null){
//           Toast toast = Toast.makeText(getApplicationContext(),"name cannot be empty", Toast.LENGTH_SHORT);
//            toast.show();
//        }

        int priceInt = Integer.parseInt((mItemPriceEditText.getText().toString().trim()));
        int quantityInt = Integer.parseInt((mItemQuantityEditText.getText().toString().trim()));
        String supplierString = mItemSupplierEditText.getText().toString().trim();
        String supplierPhoneString = mItemSupplierPhoneEditText.getText().toString().trim();

        //check if new item and if all fields in the editor are blank
        if (mCurrentItemUri == null &&
                //need to add price int and quantity int checks
                TextUtils.isEmpty(nameString) &&
                TextUtils.isEmpty(getString(priceInt)) &&
                TextUtils.isEmpty(getString(quantityInt)) &&
                TextUtils.isEmpty(supplierString) &&
                TextUtils.isEmpty(supplierPhoneString)){
            return;
        }

//        Create a contentValues object where column names are keys and inventory
//        attributes from the editor are the values
        ContentValues values = new ContentValues();
        values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME, nameString);
        values.put(InventoryContract.InventoryEntry.COLUMN_PRICE, priceInt);
        values.put(InventoryContract.InventoryEntry.COLUMN_QUANTITY, quantityInt);
        values.put(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_NAME, supplierString);
        values.put(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE, supplierPhoneString);

        //determine if new or existing item by checking mCurrentItemUri is null or no
        if(mCurrentItemUri == null) {
            //new item needs insert into provider and return uri
            Uri newUri = getContentResolver().insert(InventoryContract.InventoryEntry.CONTENT_URI, values);

            //Show a toast message to confirm if successful or show failure
            if (newUri == null) {
                Toast.makeText(this, getString(R.string.editor_insert_item_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_insert_item_success),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            //if existing item, update the content uri and pass values
            //pass null for selection and selection args since currentItemUri already identified
            int rowsAffected = getContentResolver().update(mCurrentItemUri, values, null, null);

            //show a toast for success or failure
            if(rowsAffected == 0){
                //if no rows affected show error
                Toast.makeText(this, getString(R.string.editor_insert_item_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                //success toast
                Toast.makeText(this, getString(R.string.editor_insert_item_success),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //inflate the menu options from the res/menu/menu_inventory option
        getMenuInflater().inflate(R.menu.menu_inventory, menu);
        MenuItem mockDataAdd = menu.findItem(R.id.action_insert_test_data);
        MenuItem mockDataDelete = menu.findItem(R.id.action_delete_test_entries);
        mockDataAdd.setVisible(false);
        mockDataDelete.setVisible(false);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        super.onPrepareOptionsMenu(menu);
        //if this is a new item, hide the menu options
        if(mCurrentItemUri == null){
            MenuItem menuItem = menu.findItem(R.id.action_delete_test_entries);
            menuItem.setVisible(false);
            MenuItem menuItem2 = menu.findItem(R.id.action_insert_test_data);
            menuItem2.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //user clicked on a menu option in the app overflow bar
        switch (item.getItemId()){
            //respond to save option
            case R.id.item_completed:
                //save to database
                saveInventory();
                //exit
                finish();
                return true;
            //respond to delete item menu option
            case R.id.delete:
                //pop up to confirm
                showDeleteConfirmation();
                return true;
            case android.R.id.home:
                //if item has not changed, continue to navigate to the parent activity
                if(!mItemHasChanged){
                    NavUtils.navigateUpFromSameTask(DetailActivity.this);
                    return true;
                }

                //otherwise unsaved changes, warn the user with a click listener
                DialogInterface.OnClickListener discareButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //user clicked the discard button, navigate to parent activity
                                NavUtils.navigateUpFromSameTask(DetailActivity.this);
                            }
                        };
                //show user a dialog there are unsaved changes
                showUnsavedChangesDialog(discareButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        //if no change, allow to go back
        if(!mItemHasChanged){
            super.onBackPressed();
            return;
        }
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
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        //leave if cursor is null or less than one row in cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        //proceed with moving to the first row of data in the cursor and read it
        //this should be the only row in the cursor
        if(cursor.moveToFirst()){
            //find the columns of item attributes
            int nameColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_QUANTITY);
            int supplierColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_NAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE);

            //extract the values from the Cursor at the given index
            String name = cursor.getString(nameColumnIndex);
            int price = cursor.getInt(priceColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            String supplier = cursor.getString(supplierColumnIndex);
            String supplierPhone = cursor.getString(supplierPhoneColumnIndex);

            //update the views on the screen with the values
            mItemNameEditText.setText(name);
//            mItemPriceEditText.setText(String.valueOf(price));
//            mItemQuantityEditText.setText(String.valueOf(quantity));
            mItemPriceEditText.setText(Integer.toString(price));
            mItemQuantityEditText.setText(Integer.toString(quantity));
            mItemSupplierEditText.setText(supplier);
            mItemSupplierPhoneEditText.setText(supplierPhone);
        }

    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //if loader is invalidated, clear out the data from the input fields
        mItemNameEditText.setText("");
        mItemPriceEditText.setSelection(0);
        mItemQuantityEditText.setSelection(0);
        mItemSupplierEditText.setText("");
        mItemSupplierPhoneEditText.setText("");

    }

    private void showUnsavedChangesDialog(
        DialogInterface.OnClickListener discardButtonClickListener){
        //create an alert dialog builder and set the message
        //and click listeners for positive and negative buttons in the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.discard_changes);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(getString(R.string.keep_editing), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if keep editing, dismiss and keep editing item
                if(dialog !=null){
                    dialog.dismiss();
                }
            }
        });

        //create and show the alert dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmation(){
        //alert to verify delete with click listener
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_confirmation);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //user clicks delete
                deleteItem();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //user clicks cancel
                if(dialog != null){
                    dialog.dismiss();
                }
            }
        });
        //create and show the alert dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteItem(){
        //only perform delete if existing item
        if(mCurrentItemUri != null){
            //call content resolver to delete at given uri
            int rowsDeleted = getContentResolver().delete(mCurrentItemUri, null, null);

            //show a toast if successful or not
            if(rowsDeleted == 0){
                Toast.makeText(this, R.string.error_delete,
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.item_deleted,
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }
}
