package com.svalero.bookapi.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorOutDto {
    private Integer id;
    private String authorName;
    private Boolean activeAuthor;
    private String nationality;
    private Date birthDate;
    private List<BookOutDto> books;
}
