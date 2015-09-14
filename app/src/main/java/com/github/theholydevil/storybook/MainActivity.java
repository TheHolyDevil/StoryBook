package com.github.theholydevil.storybook;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    ArrayList<Button> buttonBookList = new ArrayList<>();
    ArrayList<Book> bookList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonBookList.add((Button) findViewById(R.id.buttonTestReader));
        bookList = BookHandler.loadAllBooks();
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
        switch (id)
        {
            case R.id.action_add:
                return true;
            case R.id.action_settings:
                return true;
            case R.id.action_help:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onBookButton(View view)
    {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
            Log.e("MainAct/HideBar", e.getMessage());
        }

        setContentView(R.layout.fragment_chapter_reader);
        ChapterReader reader = new ChapterReader(getSupportFragmentManager(),
                (ViewPager)findViewById(R.id.pager),
                bookList.get(0)); //TODO: Hardcoded

        reader.goToLastPosition();
    }
}
