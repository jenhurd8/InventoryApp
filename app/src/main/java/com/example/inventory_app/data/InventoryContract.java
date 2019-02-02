package com.example.inventory_app.data;

import android.provider.BaseColumns;

import com.example.inventory_app.App;
import com.example.inventory_app.R;

/***API Contract for the Inventory App***/
public final class InventoryContract {

    //Empty constructor - to prevent accidental instantiation of the contract class
    private InventoryContract(){};

    //Inner class that defines constants for the Inventory database table
    //Each entry in the table will be a single item
    public static final class InventoryEntry implements BaseColumns {

        //name of the inventory table
        public final static String TABLE_NAME = App.getContext().getResources().getString(R.string.inventory);

        //unique id number for the item - type: integer
        public final static String _ID = BaseColumns._ID;

        //name of the item - type: text
        public final static String COLUMN_PRODUCT_NAME = App.getContext().getResources().getString(R.string.name);

        //price of the item - type: integer
        public final static String COLUMN_PRICE = App.getContext().getResources().getString(R.string.price);

        //quantity of the item - type: integer
        public final static String COLUMN_QUANTITY = App.getContext().getResources().getString(R.string.quantity);

        //supplier name - type: text
        public final static String COLUMN_SUPPLIER_NAME = App.getContext().getResources().getString(R.string.supplier_name);

        //supplier phone number - type: text
        public final static String COLUMN_SUPPLIER_PHONE = App.getContext().getResources().getString(R.string.supplier_phone);

    }


}
