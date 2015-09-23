package com.github.theholydevil.storybook;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.github.theholydevil.storybook.model.Book;

/**
 * Created by Stefan on 06.09.2015.
 */

public class ChapterReader extends FragmentActivity{
    private ViewPager viewPager;
    private FragmentAdapter fragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_chapter_reader);

        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(fragmentAdapter);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (BookHandler.currentBook().getReadingOrientation().equals(ReadingOrientation.LEFT)) {
            viewPager.setCurrentItem(BookHandler.currentBook().getPagePathList().size() - 1
                    - getIntent().getIntExtra("startposition", 0), false);
        } else viewPager.setCurrentItem(getIntent().getIntExtra("startposition", 0), false);
    }

    @Override
    public void onStop() {
        BookHandler.currentBook().setLastPosition(this.viewPager.getCurrentItem());
        BookHandler.currentBook().updateXML();

        super.onStop();
    }

    public static class FragmentAdapter extends FragmentStatePagerAdapter {
        int count;
        Book book;

        public FragmentAdapter(FragmentManager fm)
        {
            super(fm);
            this.book = BookHandler.currentBook();
            this.count = this.book.getPagePathList().size();
        }
        @Override
        public int getCount()
        {
            return count;
        }

        @Override
        public Fragment getItem(int position){
            return PageFragment.newInstance(position, this.book);
        }
    }

    public static class PageFragment extends Fragment {
        //int pageIndex;
        String path;

        static PageFragment newInstance(int index, Book book)
        {
            PageFragment f = new PageFragment();

            Bundle args = new Bundle();
            //args.putInt("page", index);
            args.putString("path", book.getItemPath(index));
            f.setArguments(args);

            return f;
        }

        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            //this.pageIndex = getArguments().getInt("page");
            this.path = getArguments().getString("path");
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            View v = inflater.inflate(R.layout.fragment_page, container, false);
            ImageView imageView = (ImageView) v.findViewById(R.id.imageView);

            Bitmap bitmap = BitmapFactory.decodeFile(this.path);
            imageView.setImageBitmap(bitmap);

            return v;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState)
        {
            super.onActivityCreated(savedInstanceState);
        }
    }
}
