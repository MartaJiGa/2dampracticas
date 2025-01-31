package com.svalero.bookapi.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookInDto {
    private String title;
    private String genre;
    private Date publicationDate;
    private Boolean available;
    private Integer authorId;
}
