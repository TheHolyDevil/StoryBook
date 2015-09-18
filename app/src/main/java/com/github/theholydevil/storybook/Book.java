package com.github.theholydevil.storybook;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Stefan on 06.09.2015.
 */
enum ReadingOrientation {RIGHT, LEFT}

public class Book {
    private ArrayList<String> pagePathList = new ArrayList<>();
    private ArrayList<Chapter> chapterList = new ArrayList<>();
    private String path;
    private String pagePath;
    private String thumbnailPath;
    private int lastPosition;
    private String name;
    private ReadingOrientation readingOrientation = ReadingOrientation.RIGHT;

    public ArrayList<Chapter> getChapterList() {
        return chapterList;
    }

    /**
     * Simple class for storing and getting information for a Book
     * @param Path Full path to the storage of the corresponding book
     */
    public Book(String Path) {
        this.path = Path;
        this.pagePath = Path + "pages.path";
        this.thumbnailPath = Path + "thumbnail.png";
        this.parseBook();
    }

    /**
     * Extension of Constructor to avoid unnecessary complexity
     */
    private void parseBook(){
        try
        {
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser newSAXParser = saxParserFactory.newSAXParser();
            XMLReader xmlReader = newSAXParser.getXMLReader();
            BookXMLSax xmlHandler = new BookXMLSax(this);
            xmlReader.setContentHandler(xmlHandler);
            xmlReader.setErrorHandler(xmlHandler);
            xmlReader.parse(new InputSource(new FileReader(this.path + "book.xml")));
        }
        catch (SAXException | IOException |ParserConfigurationException ex) {
            Log.e("Book/parseBook", "Failed to parse book.xml: " + ex.getMessage());
        }
        catch (NullPointerException ex) {
            Log.e("Book/parseBook", "NullPointerEx" + ex.getMessage());
        }
    }

    /**
     * Add a Chapter to the end! of the List
     * @param name Name of the Chapter
     * @param pageIndex Index of the first Page in pagePathList,
     *                  also is a key for each Chapter
     *                  -> A Page can only be owned by one Chapter / No empty Chapters
     * @param isRead Indicates if Chapter is marked Read
     */
    public void addChapter(String name, int pageIndex, boolean isRead){
        this.chapterList.add(new Chapter(name,pageIndex, isRead));
    }

    /**
     * Gets the List of all paths to the corresponding Pages
     * @return
     */
    public ArrayList<String> getPagePathList() {
        if(this.pagePathList.isEmpty())
        {
            try
            {
                String prefix = this.pagePath.substring( 0, (this.pagePath.lastIndexOf("/") + 1));

                FileReader fileReader = new FileReader(this.pagePath);
                BufferedReader reader = new BufferedReader(fileReader);
                String line = reader.readLine();
                while(line != null && !line.isEmpty())
                {
                    this.pagePathList.add(line);
                    line = reader.readLine();
                }
                reader.close();
                fileReader.close();
            }
            catch(IOException ex)
            {
                Log.e("Book", "IOEX: Page path parse ex: " + ex.getMessage());
            }
        }
        return pagePathList;
    }

    /**
     * Gets the Path of the Corresponding Item / Page Index
     * @param position Index of the Item / Page
     * @return Path of the Item / Page
     */
    public String getItemPath(int position){
        switch (this.getReadingOrientation()){
            case LEFT:
                return this.getPagePathList().get(this.getPagePathList().size() - position -1);
            default:
                return this.getPagePathList().get(position);
        }
    }

    /**
     * Gets Index of last read Page
     * @return Index of the last read Page
     */
    public int getLastPosition() {
        return lastPosition;
    }

    /**
     * Gets Index of first Page of a Chapter
     * @param chapterIndex Index of Chapter
     * @return Index of first Page
     */
    public int getChapterPageIndex(int chapterIndex) {
        return chapterList.get(chapterIndex).getPageIndex();
    }

    /**
     * Sets Index for last read Page
     * @param lastPosition Index of new last read Page
     */
    public void setLastPosition(int lastPosition) {
        switch (this.getReadingOrientation()){
            case LEFT:
                this.lastPosition = this.getPagePathList().size() - lastPosition -1;
                break;
            default:
                this.lastPosition = lastPosition;
                break;
        }
        /**TODO: write Information to XML, but this method will be called on screen
         * rotation. Careful with DOM
         */
    }

    public void setXMLLastPosition(int lastPosition){
        this.lastPosition = lastPosition;
    }
    /**
     * Gets Thumbnail of Book
     * @return Thumbnail as Bitmap Factory Object
     */
    public Bitmap getThumbnail()
    {
        return BitmapFactory.decodeFile(this.thumbnailPath);
    }

    /**
     * Sets the reading direction to left or right
     * @param readingOrientation Sets the reading direction to left or right
     */
    public void setReadingOrientation(ReadingOrientation readingOrientation) {
        this.readingOrientation = readingOrientation;
    }

    /**
     * Sets Name of Book
     * @param name New Name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets direction of Reading
     * @return Reading Direction Left or Right
     */
    public ReadingOrientation getReadingOrientation() {
        return readingOrientation;
    }

    /**
     * Name of the Book
     * @return Name of the Book
     */
    public String getName() {
        return name;
    }
}
