package com.github.theholydevil.storybook.model;

import com.github.theholydevil.storybook.ReadingOrientation;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by Stefan on 09.09.2015.
 */
public class BookXMLSax extends DefaultHandler{
    Book book;

    public BookXMLSax(Book book) {
        super();
        this.book = book;
    }

    @Override
    public void startElement(String uri, String name, String qName, Attributes atb){
     switch (name) {
         case "Book":
             break;
         case "Attributes":
             if(atb.getValue("orientation").equals("left")) {
                 this.book.setReadingOrientation(ReadingOrientation.LEFT);
             }
             else {
                 this.book.setReadingOrientation(ReadingOrientation.RIGHT);
             }
             this.book.setLastPosition(Integer.parseInt(atb.getValue("lastPosition")), true);
             this.book.setName(atb.getValue("name"));
             break;
         case "Chapter":
             this.book.addChapter(atb.getValue("name"),
                     Integer.parseInt(atb.getValue("pageIndex")),
                     Boolean.parseBoolean(atb.getValue("isRead")));
             break;
     }
    }
}
