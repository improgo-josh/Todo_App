package com.example.joshi.todo_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import static com.example.joshi.todo_app.MainActivity.ITEM_POSITION;
import static com.example.joshi.todo_app.MainActivity.ITEM_TEXT;

public class EditItemActivity extends AppCompatActivity {

    // track edit text
    EditText et_ItemText;

    // position of edited item in list
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        Spinner spinner = findViewById(R.id.spin_priorities);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(EditItemActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.priorities));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // resolve edit text from layout
        et_ItemText = findViewById(R.id.et_editItem);

        // set edit text value from intent extra
        et_ItemText.setText(getIntent().getStringExtra(ITEM_TEXT));

        // update position from intent extra
        position = getIntent().getIntExtra(ITEM_POSITION, 0);

        // update the title bar of the activity
        getSupportActionBar().setTitle("Edit Item");

    }

    // handler for save button
    public void onSaveItem(View v) {
        // prepare new intent for result
        Intent i = new Intent();

        // pass updated item text as extra
        i.putExtra(ITEM_TEXT, et_ItemText.getText().toString());

        // pass original position as extra
        i.putExtra(ITEM_POSITION, position);

        // set the intent as the result of the activity
        setResult(RESULT_OK, i);

        // close the activity and redirect to main
        finish();
    }
}
