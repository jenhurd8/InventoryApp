package com.example.inventory_app;

import android.content.ContentValues;
import android.net.Uri;
import android.widget.Toast;

import com.example.inventory_app.data.InventoryContract;

public class EditorActivity {

    private void insertInventory(){
        //read from inputs, use trim to remove leading and trailing whitespace
        //String nameString = mNameEditText.getText().toString().trim;

        //Create a contentValues object where column names are keys and inventory
        //attributes from the editor are the values
       // ContentValues values = new ContentValues();
       // values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME, nameString);

        //insert a new inventory item into the provider returning the content uri for the new item
        //Uri newUri = getContentResolver.insert(InventoryContract.InventoryEntry.CONTENT_URI, values);

        //Show a toast message to confirm if successful or show failure
//        if(newUri == null){
//            Toast.makeText(this, getString(R.string.editor_insert_item_failed),
//                    Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, getString(R.string.editor_insert_item_success),
//                    Toast.LENGTH_SHORT).show();
//        }
    }
}
