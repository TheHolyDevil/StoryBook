package com.github.theholydevil.storybook;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Stefan on 07.09.2015.
 */
public abstract class BookImporter {

    abstract public String getName();

    abstract public ArrayList<File> getSortedImageFileList();

    abstract public int getChapterCount();

    abstract public File getThumbnail();
}