package com.svalero.bookapi.controller;

import com.svalero.bookapi.domain.dto.AuthorInDto;
import com.svalero.bookapi.domain.dto.AuthorOutDto;
import com.svalero.bookapi.service.AuthorService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    @Autowired
    private AuthorService authorService;
    private final Logger logger = LoggerFactory.getLogger(BookController.class);

    @ApiResponse(responseCode = "200", description = "Successful retrieval of authors")
    @GetMapping
    public ResponseEntity<List<AuthorOutDto>> getAllAuthors() {
        logger.info("getAllAuthors");
        List<AuthorOutDto> authors = authorService.getAllAuthors();
        return ResponseEntity.ok(authors);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Author found"),
            @ApiResponse(responseCode = "404", description = "Author not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AuthorOutDto> getAuthorDetails(@PathVariable Integer id) {
        logger.info("getAuthorDetails");
        AuthorOutDto author = authorService.getAuthorDetails(id);
        return ResponseEntity.ok(author);
    }

    @ApiResponse(responseCode = "201", description = "Author created successfully")
    @PostMapping
    public ResponseEntity<AuthorOutDto> createAuthor(@Valid @RequestBody AuthorInDto authorInDto) {
        logger.info("createAuthor");
        AuthorOutDto createdAuthor = authorService.createAuthor(authorInDto);
        return ResponseEntity.status(201).body(createdAuthor);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Author updated successfully"),
            @ApiResponse(responseCode = "404", description = "Author not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AuthorOutDto> updateAuthor(@PathVariable Integer id, @Valid @RequestBody AuthorInDto authorInDto) {
        logger.info("updateAuthor");
        AuthorOutDto updatedAuthor = authorService.updateAuthor(id, authorInDto);
        return ResponseEntity.ok(updatedAuthor);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Author deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Author not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Integer id) {
        logger.info("deleteAuthor");
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }
}

