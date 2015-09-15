package com.github.theholydevil.storybook;

import android.os.Environment;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Stefan on 07.09.2015.
 */
public class BookHandler
{
    private static BookHandler _instance;
    private Book savedBook;
    private ArrayList<Book> books = new ArrayList<>();

    private BookHandler() {}

    private static final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/StoryBook/";

    private static BookHandler instance() {
        if (_instance == null) {
            _instance = new BookHandler();
        }
        return _instance;
    }

    public static Book newBook(BookImporter importer) {
        String bookPath = importer.getName().trim().toLowerCase() + "/";

        //TODO: Put Images in correct places /Chapter<i>/img<i>

        for(int i = 0; i < importer.getChapterCount(); i++)
        {
            //importer.getSortedImageFileList(i);
        }

        createPagesPath(importer.getSortedImageFileList(), bookPath);

        addToBooksPath(bookPath);

        //TODO: Create Thumbnail to /thumbnail.png

        Book book = new Book(bookPath);
        return book;
    }

    /**
     * extension from newBook()
     * @param sortedImageFileList
     */
    private static void createPagesPath(ArrayList<File> sortedImageFileList, String bookPath) {
        try {
            StringBuffer sb = new StringBuffer();

            for (int i = 0; i < sortedImageFileList.size(); i++) {
                sb.append(sortedImageFileList.get(i).getAbsolutePath());
                sb.append(System.lineSeparator());
            }
            FileWriter fileWriter = new FileWriter(path + bookPath + "pages.path");
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

        bookPath = path + bookPath;
        try
        {
            FileReader fileReader = new FileReader(path + "books.path");
            BufferedReader reader = new BufferedReader(fileReader);
            StringBuffer sb = new StringBuffer();
            String line = reader.readLine();
            while(line != null && !line.isEmpty())
            {
                int cmp = bookPath.compareTo(line);
                if (cmp < 0){
                    sb.append(line);
                    line = reader.readLine();
                }
                else if (cmp >= 0) {
                    sb.append(bookPath + System.lineSeparator());
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
                sb.append(bookPath + System.lineSeparator());
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
                    books.add(new Book(path + line));
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
}