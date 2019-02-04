package com.example.inventory_app;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.inventory_app.data.InventoryContract;

//adapter for the list view of inventory data - created with list itme in the res layout
public class InventoryCursorAdapter extends CursorAdapter {

    //Construct a new Inventory
    public InventoryCursorAdapter(Context context, Cursor c){
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
    public void bindView(View view, Context context, Cursor cursor) {
        //find the individual views from the xml list_item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.nameTextView);
        TextView priceTextView = (TextView) view.findViewById(R.id.priceTextView);
        TextView quantityTextView = (TextView) view.findViewById(R.id.quantityTextView);
        //TextView supplierTextView = (TextView) view.findViewById(R.id.supplier_nameTextView);
        //TextView supplierPhoneTextView = (TextView) view.findViewById(R.id.supplier_phoneTextView);

        //find the columns of the attributes we are interested in
        int nameColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_NAME);
        int quantityColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_QUANTITY);
        //int supplierNameColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_NAME);
        //int supplierPhoneColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE);

        //read the item attributes for the cursor of the current item
        String itemName = cursor.getString(nameColumnIndex);
        int itemPrice = cursor.getInt(priceColumnIndex);
        int itemQuantity = cursor.getInt(quantityColumnIndex);
       // String supplierName = cursor.getString(supplierNameColumnIndex);
        //String supplierPhone = cursor.getString(supplierPhoneColumnIndex);

        //Update the textViews with the attributes of the current item
        nameTextView.setText(itemName);
       priceTextView.setText(String.valueOf(itemPrice));
       quantityTextView.setText(String.valueOf(itemQuantity));
        //supplierTextView.setText(supplierName);
        //supplierPhoneTextView.setText(supplierPhone);

    }
}
