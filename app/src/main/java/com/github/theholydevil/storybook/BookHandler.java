package com.github.theholydevil.storybook;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
    private FragmentAdapter fragmentAdapter;

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

    public static void initChapterReaderFragmentAdapter(FragmentManager fragmentManager) {
        FragmentAdapter fragmentAdapter = instance().fragmentAdapter;

        if (fragmentAdapter == null) {
            instance().fragmentAdapter = new FragmentAdapter(fragmentManager);
        }
    }

    public static FragmentAdapter getChapterReaderFragmentAdapter() {
        FragmentAdapter fragmentAdapter = instance().fragmentAdapter;
        if (fragmentAdapter == null) {
            throw new NullPointerException("Chapter Reader Fragment Adapter was not initialized");
        }

        return fragmentAdapter;
    }

    public static class FragmentAdapter extends FragmentStatePagerAdapter {
        int count;
        Book book;

        public FragmentAdapter(FragmentManager fm)
        {
            super(fm);
            this.count = this.book.getPagePathList().size();
        }

        public void setBook(Book book) {
            this.book = book;
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

    public static class PageFragment extends Fragment {
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