package com.github.theholydevil.storybook;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.github.theholydevil.storybook.model.Book;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookOverview extends Fragment {
    GridView gridView;
    ArrayList<Book> bookList = new ArrayList<>();

    public BookOverview() {
        // Required empty public constructor
    }

    static BookOverview newInstance() {
        BookOverview f = new BookOverview();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book_overview, container, false);

        bookList = BookHandler.loadAllBooks();
        gridView = (GridView) view.findViewById(R.id.grid_books_view);
        ArrayList<String> bookNameList = new ArrayList<>();

        for (int i = 0; i < bookList.size(); i++) {
            bookNameList.add(bookList.get(i).getName());
        }

        ArrayAdapter<String> listBookAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1,
                bookNameList);
        gridView.setAdapter(listBookAdapter);
        gridView.setOnItemClickListener(new bookListListener(this));
        return view;
    }

    public class bookListListener implements AdapterView.OnItemClickListener {
        Fragment owner;

        public bookListListener(Fragment owner) {
            this.owner = owner;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id) {
            android.support.v4.app.Fragment chapterOverview = ChapterOverview.newInstance(
                    bookList.get(position));
            getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    chapterOverview).addToBackStack(null).commit();
        }
    }
}
