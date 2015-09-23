package com.github.theholydevil.storybook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
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
    boolean onlineSource;

    public ChapterOverview() {
        // Required empty public constructor
    }

    static ChapterOverview newInstance(Book book) {
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
        args.putBoolean("online", book.isFromOnlineSource());

        BookHandler.saveBook(book);

        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chapterIndexList = getArguments().getIntegerArrayList("indexes");
        chapterNameList = getArguments().getStringArrayList("names");
        this.onlineSource = getArguments().getBoolean("online");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chapter_overview, container, false);

        gridView = (GridView) view.findViewById(R.id.grid_chapter_view);

        chapterOverviewListAdapter listChapterAdapter = new chapterOverviewListAdapter(getContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1,
                chapterNameList);
        gridView.setAdapter(listChapterAdapter);

        view.findViewById(R.id.continue_reading).setOnClickListener(new continueListener(getContext()));

        getActivity().setTitle(getArguments().getString("title", "StoryBook"));

        return view;
    }

    private class chapterListListener implements View.OnClickListener {
        Context compatActivity;

        public chapterListListener(Context activity) {
            this.compatActivity = activity;
        }

        @Override
        public void onClick(View view) {
            int position = ((chapterOverviewListAdapter.ViewHolder) view.getTag()).position;
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

    class chapterOverviewListAdapter extends ArrayAdapter<String> {

        public chapterOverviewListAdapter(Context context, int resource, int textViewResourceId, ArrayList<String> objects) {
            super(context, resource, textViewResourceId, objects);
        }

        @Override
        public boolean  areAllItemsEnabled() {
            return false;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            String item = getItem(position);

            View rowView = convertView;

            if (rowView == null) {
                LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
                if (onlineSource)  rowView = inflater.inflate(R.layout.listview_chapter_row_online, parent, false);
                else rowView = inflater.inflate(R.layout.listview_chapter_row_offline, parent, false);
                ViewHolder h = new ViewHolder();
                h.text = (TextView) rowView.findViewById(R.id.listView_chapter_row_text);
                if (onlineSource) h.btn = (ImageButton) rowView.findViewById(R.id.listView_chapter_row_imageButton);
                h.position = position;
                rowView.setTag(h);
            }

            ViewHolder h = (ViewHolder) rowView.getTag();

            h.text.setText(item);
            if (onlineSource) h.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // DO what you want to recieve on btn click there.
                }
            });

            rowView.setClickable(true);
            rowView.setOnClickListener(new chapterListListener(getContext()));

            return rowView;
        }

        class ViewHolder {
            TextView text;
            ImageButton btn;
            int position;
        }



    }
}
