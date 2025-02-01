package com.svalero.bookapi.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookOutDto {
    private Integer id;
    private String title;
    private String genre;
    private Date publicationDate;
    private Boolean available;
    private AuthorOutDto author;
}
