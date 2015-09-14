package com.github.theholydevil.storybook;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Stefan on 06.09.2015.
 */

public class ChapterReader extends Activity
{
    private ViewPager viewPager;
    private Book book;
    FragmentAdapter fragmentAdapter;

    public ChapterReader(FragmentManager fm, ViewPager pager, Book book)
    {
        this.book = book;
        fragmentAdapter = new FragmentAdapter(fm, this.book);
        this.viewPager = pager;
        this.viewPager.setAdapter(fragmentAdapter);
        //this.viewPager.setOffscreenPageLimit(3); //Didn't make anything more smooth, only increases memory usage, try something else
    }

    public void goToLastPosition()
    {
        switch (this.book.getReadingOrientation()){
            case LEFT:
                this.viewPager.setCurrentItem(this.book.getPagePathList().size()
                        - this.book.getLastPosition() - 1);
                break;
            default:
                this.viewPager.setCurrentItem(this.book.getLastPosition());
                break;
        }
    }

    public void goToChapter(int chapterIndex)
    {
        this.viewPager.setCurrentItem(book.getChapterPageIndex(chapterIndex));
    }

    public static class FragmentAdapter extends FragmentStatePagerAdapter
    {
        int count;
        Book book;

        public FragmentAdapter(FragmentManager fm, Book items)
        {
            super(fm);
            this.book = items;
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

    public static class PageFragment extends Fragment
    {
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
