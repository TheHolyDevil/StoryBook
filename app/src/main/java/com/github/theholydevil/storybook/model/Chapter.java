package com.github.theholydevil.storybook.model;

/**
 * Created by Stefan on 14.09.2015.
 */
public class Chapter {
    private String name;
    private int pageIndex;
    private boolean isRead;

    public String getName() {
        return name;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public boolean isRead() {
        return isRead;
    }

    public Chapter(String name, int pageIndex, boolean isRead) {

        this.name = name;
        this.pageIndex = pageIndex;
        this.isRead = isRead;
    }
}
