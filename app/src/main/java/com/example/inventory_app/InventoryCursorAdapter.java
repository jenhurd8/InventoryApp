package com.example.inventory_app;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.inventory_app.data.InventoryContract;

//adapter for the list view of inventory data - created with list item in the res layout
public class InventoryCursorAdapter extends CursorAdapter {

    //Construct a new Inventory
    public InventoryCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    /*
    make a new blank list item view, data is bound in the bind view method below
    @param context is the context of the app
    @param cursor - the cursor from which to get the data set to the correct position
    @param parent - the parent to which the new view is attached
    @return - the new created list item view
    */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        //find the individual views from the xml list_item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.nameTextView);
        TextView priceTextView = (TextView) view.findViewById(R.id.priceTextView);
        TextView quantityTextView = (TextView) view.findViewById(R.id.quantityTextView);
        //TextView supplierTextView = (TextView) view.findViewById(R.id.supplier_nameTextView);
        //TextView supplierPhoneTextView = (TextView) view.findViewById(R.id.supplier_phoneTextView);


        //find the columns of the attributes we are interested in
        int nameColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_QUANTITY);
        //int supplierNameColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_NAME);
        //int supplierPhoneColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE);

        //read the item attributes for the cursor of the current item
        String itemName = cursor.getString(nameColumnIndex);
        int itemPrice = cursor.getInt(priceColumnIndex);
        final int itemQuantity = cursor.getInt(quantityColumnIndex);
        // String supplierName = cursor.getString(supplierNameColumnIndex);
        //String supplierPhone = cursor.getString(supplierPhoneColumnIndex);

        //Update the textViews with the attributes of the current item
        nameTextView.setText(itemName);
        priceTextView.setText(String.valueOf(itemPrice));
        quantityTextView.setText(String.valueOf(itemQuantity));

        final String currentSelectedId = cursor.getString(cursor.getColumnIndex(InventoryContract.InventoryEntry._ID));

        Button button = (Button) view.findViewById(R.id.sale);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemQuantity > 0) {
                    //decrease quantity by 1 but not allow negative inventory
                    int quantity = itemQuantity - 1;

                    Uri newUri = ContentUris.withAppendedId(InventoryContract.InventoryEntry.CONTENT_URI, Long.parseLong(currentSelectedId));

                    ContentValues values = new ContentValues();
                    values.put(InventoryContract.InventoryEntry.COLUMN_QUANTITY, quantity);
                    context.getContentResolver().update(newUri, values, null, null);


                }
            }
        });

    }

}
