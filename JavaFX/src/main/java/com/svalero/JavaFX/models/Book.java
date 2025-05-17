package com.svalero.JavaFX.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    private int id;
    private String title;
    private String genre;
    private String publicationDate;
    private boolean available;
    private Author author;

    public String getAuthorName(){
        return author != null ? author.getAuthorName() : "Sin autor";
    }
}
