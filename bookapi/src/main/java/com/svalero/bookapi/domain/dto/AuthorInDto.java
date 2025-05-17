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
public class AuthorInDto {

    @NotBlank(message = "El nombre del autor es obligatorio")
    private String authorName;

    @NotNull(message = "El campo activo es obligatorio")
    private Boolean activeAuthor;

    private String nationality;

    private Date birthDate;
}