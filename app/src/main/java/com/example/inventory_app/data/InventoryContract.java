package com.example.inventory_app.data;

import android.net.Uri;
import android.provider.BaseColumns;
import android.content.ContentResolver;

import com.example.inventory_app.App;

/***API Contract for the Inventory App***/
public final class InventoryContract extends App {

    //Empty constructor - to prevent accidental instantiation of the contract class
    private InventoryContract() {
    }

    //content authority for the entire content provider
    //convenient string to use for the content authority is the unique package name of the app
    public static final String CONTENT_AUTHORITY = "com.example.android.inventory_app";

    //use content authority to create the base of all uris which will connect
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    //possible path example: content://com.example.android.inventory_app/inventory/
    public static final String PATH_INVENTORY = "inventory";

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

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INVENTORY);

        //mime type for the list of inventory
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

       //mime type for single item
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

    }

}
