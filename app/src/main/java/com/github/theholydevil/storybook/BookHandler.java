package com.github.theholydevil.storybook;

import android.os.Environment;
import android.util.Log;
import com.github.theholydevil.storybook.importer.BookImporter;
import com.github.theholydevil.storybook.model.Book;
import com.github.theholydevil.storybook.model.Chapter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import static java.lang.System.lineSeparator;

/**
 * Created by Stefan on 07.09.2015.
 */
public class BookHandler
{
    private static BookHandler _instance;
    private Book savedBook;
    private ArrayList<Book> books = new ArrayList<>();
    private static final String path = Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/StoryBook/";

    private BookHandler() {}

    private static BookHandler instance() {
        if (_instance == null) {
            _instance = new BookHandler();
        }
        return _instance;
    }

    public static Book newBook(BookImporter importer) {
        String path = importer.getDirectory().getAbsolutePath();
        addToBooksPath(path);

        ArrayList<Chapter> chapterArrayList = importer.getChapterList();

        String bookXML = getBookXML(importer, chapterArrayList);

        try {
            FileWriter fileWriter = new FileWriter(importer.getDirectory().getAbsolutePath()
                    + "/book.xml");
            fileWriter.write(bookXML);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            Log.e("BookHandler/XMLUpdate", e.getMessage());
        }

        createPagesPath(importer.getSortedImageFileList(), path);

        //TODO: Create Thumbnail to /thumbnail.png

        Book book = new Book(path + "/");
        instance().books.add(0, book);
        return book;
    }

    public static String getBookXML(BookImporter importer, ArrayList<Chapter> chapterArrayList) {
        return writeBookXML(importer.getName(),
                importer.getOrientation(), importer.isOnlineSource(), 0,
                chapterArrayList).toString();
    }

    public static String getBookXML(Book book) {
        return writeBookXML(book.getName(),
                book.getReadingOrientation(),
                book.isFromOnlineSource(),
                book.getLastPosition(),
                book.getChapterList()).toString();
    }

    public static StringBuilder writeBookXML(String name, ReadingOrientation orientation, boolean onlineSource,
                                             int lastPosition, ArrayList<Chapter> chapterArrayList) {

        StringBuilder xmlStringBuilder = new StringBuilder();

        xmlStringBuilder.append("<Book>\n<Attributes orientation = \"");

        if (orientation != null) switch (orientation) {
            case LEFT:
                xmlStringBuilder.append("left");
                break;
            case RIGHT:
                xmlStringBuilder.append("right");
                break;
        }
        else xmlStringBuilder.append("right");

        xmlStringBuilder.append("\" name = \"" + name + "\" lastPosition = \"" + lastPosition + "\"" +
                " isOnlineSource = \"" + onlineSource + "\" /> \n");

        for(Chapter chapter : chapterArrayList) {
            xmlStringBuilder.append("<Chapter name = \"" + chapter.getName() + "\" pageIndex = \""
                    + chapter.getPageIndex() + "\" isRead = \"" + chapter.isRead() + "\" />\n");
        }

        xmlStringBuilder.append("</Book>\n");
        return xmlStringBuilder;
    }

    /**
     * extension from newBook()
     * @param sortedImageFileList
     */
    private static void createPagesPath(ArrayList<File> sortedImageFileList, String absolutePath) {
        try {
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < sortedImageFileList.size(); i++) {
                sb.append(sortedImageFileList.get(i).getAbsolutePath() + "\n");
            }
            FileWriter fileWriter = new FileWriter(absolutePath + "/pages.path");
            fileWriter.write(sb.toString());
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            Log.e("BookHandler/WritePages", e.getMessage());
        }
    }

    /**
     * extension from newBook()
     * @param bookPath
     */
    private static void addToBooksPath(String bookPath) {
        bookPath += "/";
        try
        {
            FileReader fileReader = new FileReader(path + "books.path");
            BufferedReader reader = new BufferedReader(fileReader);
            StringBuilder sb = new StringBuilder();
            String line = reader.readLine();
            while(line != null && !line.isEmpty())
            {
                int cmp = bookPath.compareTo(line);
                if (cmp < 0){
                    sb.append(line);
                    line = reader.readLine();
                }
                else if (cmp >= 0) {
                    sb.append(bookPath + "\n");
                    bookPath = "";
                    sb.append(line);
                    line = reader.readLine();
                    while(line != null && !line.isEmpty())
                    {
                        sb.append(line);
                        line = reader.readLine();
                    }
                    break;
                }
            }

            if (!bookPath.isEmpty())
            {
                sb.append(bookPath + "\n");
            }

            reader.close();
            fileReader.close();

            FileWriter fileWriter = new FileWriter(path + "books.path");
            fileWriter.write(sb.toString());
            fileWriter.flush();
            fileWriter.close();
        }
        catch(IOException ex)
        {
            Log.e("Book parse ex: ", ex.getMessage());
        }

    }

    /**
     * Loads all Books from the Paths in the books.path file
     * @return List of all Books
     */
    public static ArrayList<Book> loadAllBooks() {
        ArrayList<Book> books = instance().books;

        if (books.isEmpty()) {
            try
            {
                FileReader fileReader = new FileReader(path + "books.path");
                BufferedReader reader = new BufferedReader(fileReader);
                String line = reader.readLine();
                while(line != null && !line.isEmpty())
                {
                    books.add(new Book(line));
                    line = reader.readLine();
                }
                reader.close();
                fileReader.close();
            }
            catch(IOException ex)
            {
                Log.e("Book parse ex: ", ex.getMessage());
            }
        }

        return books;
    }

    public static Book currentBook() {
        return instance().savedBook;
    }

    public static void saveBook(Book book) {
        instance().savedBook = book;
    }

    public static boolean fileHasAllowedExtension(File file) {
        String filename = file.getName();
        return filename.endsWith(".jpeg") ||
                filename.endsWith(".jpg") ||
                filename.endsWith(".png");

    }
}