package com.github.theholydevil.storybook;

import android.support.v4.view.ViewPager;

/**
 * Created by Stefan on 06.09.2015.
 */

public class ChapterReader {
    private ViewPager viewPager;
    private Book book;

    public ChapterReader(ViewPager pager, Book book)
    {
        this.book = book;
        this.viewPager = pager;
        BookHandler.getChapterReaderFragmentAdapter().setBook(this.book);
        this.viewPager.setAdapter(BookHandler.getChapterReaderFragmentAdapter());
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


}
