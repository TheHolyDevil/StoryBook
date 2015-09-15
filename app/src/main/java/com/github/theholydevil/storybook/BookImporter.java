package com.github.theholydevil.storybook;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Stefan on 07.09.2015.
 */
public interface BookImporter {

    String getName();

    ArrayList<File> getSortedImageFileList();

    int getChapterCount();

    //File getThumbnail();
}