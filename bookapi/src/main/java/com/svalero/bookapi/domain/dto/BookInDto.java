package com.svalero.bookapi.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookInDto {

    @NotBlank(message = "El título es obligatorio")
    private String title;

    private String genre;

    private Date publicationDate;

    @NotNull(message = "El campo disponible es obligatorio")
    private Boolean available;

    private Integer authorId;
}
