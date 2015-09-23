package com.github.theholydevil.storybook.importer;

import com.github.theholydevil.storybook.model.Chapter;
import com.github.theholydevil.storybook.ReadingOrientation;

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