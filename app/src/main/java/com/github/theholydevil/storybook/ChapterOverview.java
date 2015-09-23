package com.github.theholydevil.storybook;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import com.github.theholydevil.storybook.model.Book;
import com.github.theholydevil.storybook.model.Chapter;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChapterOverview extends Fragment {
    ArrayList<String> chapterNameList = new ArrayList<>();
    ArrayList<Integer> chapterIndexList = new ArrayList<>();
    GridView gridView;

    public ChapterOverview() {
        // Required empty public constructor
    }

    static ChapterOverview newInstance(Book book)
    {
        ChapterOverview f = new ChapterOverview();

        Bundle args = new Bundle();

        ArrayList<Chapter> chapterList = book.getChapterList();

        ArrayList<String> chapterNameList = new ArrayList<>();
        ArrayList<Integer> chapterIndexList = new ArrayList<>();

        for (int i = 0; i < chapterList.size(); i++) {
            chapterIndexList.add(chapterList.get(i).getPageIndex());
            chapterNameList.add(chapterList.get(i).getName());
        }

        args.putStringArrayList("names", chapterNameList);
        args.putIntegerArrayList("indexes", chapterIndexList);
        args.putString("title", book.getName());

        BookHandler.saveBook(book);

        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chapterIndexList = getArguments().getIntegerArrayList("indexes");
        chapterNameList = getArguments().getStringArrayList("names");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chapter_overview, container, false);

        gridView = (GridView) view.findViewById(R.id.grid_chapter_view);

        ArrayAdapter<String> listChapterAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1,
                chapterNameList);
        gridView.setAdapter(listChapterAdapter);
        gridView.setOnItemClickListener(new chapterListListener(getContext()));

        view.findViewById(R.id.continue_reading).setOnClickListener(new continueListener(getContext()));

        getActivity().setTitle(getArguments().getString("title", "StoryBook"));

        return view;
    }

    private class chapterListListener implements AdapterView.OnItemClickListener {
        Context compatActivity;

        public chapterListListener(Context activity) {
            this.compatActivity = activity;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id) {
            Intent readerIntent = new Intent(this.compatActivity, ChapterReader.class);
            readerIntent.putExtra("startposition", chapterIndexList.get(position));
            startActivity(readerIntent);
        }
    }

    private class continueListener implements View.OnClickListener {
        Context compatActivity;

        public continueListener(Context compatActivity) {
            this.compatActivity = compatActivity;
        }

        @Override
        public void onClick(View v) {
            Intent readerIntent = new Intent(this.compatActivity, ChapterReader.class);
            readerIntent.putExtra("startposition", BookHandler.currentBook().getLastPosition());
            startActivity(readerIntent);
        }
    }
}
