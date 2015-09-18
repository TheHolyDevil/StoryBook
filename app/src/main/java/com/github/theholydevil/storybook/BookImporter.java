package com.github.theholydevil.storybook;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Stefan on 07.09.2015.
 */
public interface BookImporter {

    String getName();

    File getDirectory();

    ArrayList<File> getSortedImageFileList();

    ArrayList<Chapter> getChapterList();

    ReadingOrientation getOrientation();

    //File getThumbnail();
}