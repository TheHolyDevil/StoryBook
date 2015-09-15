package com.github.theholydevil.storybook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    GridView gridView;
    ArrayList<Book> bookList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        bookList = BookHandler.loadAllBooks();
        gridView = (GridView) findViewById(R.id.grid_books_view);
        ArrayList<String> bookNameList = new ArrayList<>();

        for (int i = 0; i < bookList.size(); i++) {
            bookNameList.add(bookList.get(i).getName());
        }

        ArrayAdapter<String> listBookAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1,
                bookNameList);
        gridView.setAdapter(listBookAdapter);
        gridView.setOnItemClickListener(new bookListListener(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up TestViewButton, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_add:
                return true;
            case R.id.action_settings:
                return true;
            case R.id.action_help:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class bookListListener implements AdapterView.OnItemClickListener {
        AppCompatActivity compatActivity;

        public bookListListener(AppCompatActivity activity) {
            this.compatActivity = activity;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view,
                         int position, long id) {
            BookHandler.saveBook(bookList.get(position));
            startActivity(new Intent(this.compatActivity, ChapterReader.class));
        }
    }
}
