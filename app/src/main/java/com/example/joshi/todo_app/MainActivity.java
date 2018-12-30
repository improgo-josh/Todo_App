package com.example.joshi.todo_app;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import static java.nio.charset.Charset.defaultCharset;

public class MainActivity extends AppCompatActivity {
    // a numeric code to identify the edit activity
    public final static int EDIT_REQUEST_CODE = 2;

    // keys used for passing data between activities
    public final static String ITEM_TEXT = "itemText";
    public final static String ITEM_POSITION = "itemPosition";

    ArrayList<String> items;
    ArrayList<Integer> priorities;
    ArrayAdapter<String> itemsAdapter;
    ListView lv_items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Log.i("MainActivity", items.size() + "asd");

        readItems();
        Log.i("MainActivity", items.size() + "asd");
        if (items.size() == 0) {
            Log.i("MainActivity", "Therea re 0 items");
        }

        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lv_items = findViewById(R.id.list);
        lv_items.setAdapter(itemsAdapter);

        final TextView btn_add = findViewById(R.id.btn_add);
        final EditText et_addItem = findViewById(R.id.et_addItem);

        setupListViewListener();


    }

    public void onAddItem(View v) {
        EditText et_addItem = findViewById(R.id.et_addItem);
        String itemText = et_addItem.getText().toString();
        if (itemText.trim().equals("") || itemText.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter text to add item", Toast.LENGTH_SHORT).show();
            return;
        }

        itemsAdapter.add(itemText);
        et_addItem.setText("");
        writeItems();
        Toast.makeText(getApplicationContext(), "Item added to list", Toast.LENGTH_SHORT).show();
    }

    private void setupListViewListener() {
        Log.i("MainActivity", "Setting up listener on list view");
        lv_items.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("MainActivity", "Item removed from list: " + position);
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });

        // set up item listener for edit (regular click)
        lv_items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // show edit or priority options
                // edit option

                // set up priorit
                // create the new activity
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);

                // pass the data being edited
                i.putExtra(ITEM_TEXT, items.get(position));
                i.putExtra(ITEM_POSITION, position);

                // display the activity
                startActivityForResult(i, EDIT_REQUEST_CODE);
            }
        });
    }

    private void setupEmptyCase() {
        if (items.size() > 0) return;



    }
    private File getDataFile() {
        return new File(getFilesDir(), "todo.txt");
    }

    private void readItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading file", e);
            items = new ArrayList<>();
        }
    }

    private void writeItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing file", e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if the edit activity completed ok
        if (resultCode != RESULT_OK) return;

        if (requestCode == EDIT_REQUEST_CODE) {
            // extract updated item text from result intent extras
            String updatedItem = data.getExtras().getString(ITEM_TEXT);

            // extract original position of edited item
            int position = data.getExtras().getInt(ITEM_POSITION);

            // update the model with the new item text at the edited position
            items.set(position, updatedItem);

            // notify the adapter that the model changed
            itemsAdapter.notifyDataSetChanged();

            // persist the changed model
            writeItems();

            // notify the user the operation completed ok
            Toast.makeText(this, "Item updated", Toast.LENGTH_SHORT).show();
        }
    }
}
