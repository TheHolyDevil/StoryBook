package com.github.theholydevil.storybook;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Stefan on 17.09.2015.
 */
public class StoryDirectoryImporter implements BookImporter {
    private String name;
    private File directory;
    private ArrayList<File> subDirectories = new ArrayList<>();
    private ArrayList<Chapter> chapterList = new ArrayList<>();
    private ArrayList<File> imageFileList = new ArrayList<>();
    private ReadingOrientation orientation;

    public StoryDirectoryImporter(String directory, Context context) {
        this.directory = new File(directory);
        this.name = this.directory.getName();

        if (orientation == null) {
            Dialog d = new AlertDialog.Builder(context)
                    .setCancelable(false)
                    .setTitle(R.string.get_reading_orientation)
                    .setNegativeButton(R.string.get_reading_orientation_left, new dialogClickHandler())
                    .setPositiveButton(R.string.get_reading_orientation_right, new dialogClickHandler())
                    .create();

            d.show();
        }

        for (File file : this.directory.listFiles()) {
            if (file != null && file.isDirectory()) {
                subDirectories.add(file);
            }
        }

        Collections.sort(subDirectories);

        for (File subDir : subDirectories) {
            chapterList.add(new Chapter(subDir.getName(), imageFileList.size(), false));

            File[] subFiles = subDir.listFiles();

            for (File file : subFiles) {
                if (file != null && file.isFile() && BookHandler.fileHasAllowedExtension(file)) {
                    imageFileList.add(file);
                }
            }
        }

        Collections.sort(imageFileList);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public File getDirectory() {
        return directory;
    }

    @Override
    public ArrayList<File> getSortedImageFileList() {
        return imageFileList;
    }

    @Override
    public ArrayList<Chapter> getChapterList() {
        return chapterList;
    }

    @Override
    public ReadingOrientation getOrientation() {
        return orientation;
    }

    private class dialogClickHandler implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case AlertDialog.BUTTON_NEGATIVE:
                    orientation = ReadingOrientation.LEFT;
                    break;
                default:
                    orientation = ReadingOrientation.RIGHT;
            }

            //TODO: WORKAROUND

            BookHandler.loadAllBooks().get(0).setReadingOrientation(orientation);

        }
    }
}
