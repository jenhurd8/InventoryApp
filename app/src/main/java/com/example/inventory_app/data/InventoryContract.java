package com.example.inventory_app.data;

import android.provider.BaseColumns;

import com.example.inventory_app.App;

/***API Contract for the Inventory App***/
public final class InventoryContract extends App {

    //Empty constructor - to prevent accidental instantiation of the contract class
    private InventoryContract() {
    }

    //Inner class that defines constants for the Inventory database table
    //Each entry in the table will be a single item
    public static final class InventoryEntry implements BaseColumns {

        //name of the inventory table
        public final static String TABLE_NAME = "inventory";

        //unique id number for the item - type: integer
        public final static String _ID = BaseColumns._ID;

        //name of the item - type: text
        public final static String COLUMN_PRODUCT_NAME = "name";

        //price of the item - type: integer
        public final static String COLUMN_PRICE = "price";

        //quantity of the item - type: integer
        public final static String COLUMN_QUANTITY = "quantity";

        //supplier name - type: text
        public final static String COLUMN_SUPPLIER_NAME = "supplier_name";

        //supplier phone number - type: text
        public final static String COLUMN_SUPPLIER_PHONE = "supplier_phone";
    }

}
