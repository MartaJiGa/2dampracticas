package com.svalero.bookapi.controller;

import com.svalero.bookapi.domain.dto.BookInDto;
import com.svalero.bookapi.domain.dto.BookOutDto;
import com.svalero.bookapi.service.BookService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;
    private final Logger logger = LoggerFactory.getLogger(BookController.class);

    @ApiResponse(responseCode = "200", description = "Successful retrieval of books")
    @GetMapping
    public ResponseEntity<List<BookOutDto>> getAllBooks() {
        logger.info("getAllBooks");
        List<BookOutDto> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book found"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BookOutDto> getBookDetails(@PathVariable Integer id) {
        logger.info("getBookDetails");
        BookOutDto book = bookService.getBookDetails(id);
        return ResponseEntity.ok(book);
    }

    @ApiResponse(responseCode = "201", description = "Book created successfully")
    @PostMapping
    public ResponseEntity<BookOutDto> createBook(@Valid @RequestBody BookInDto bookInDto) {
        logger.info("createBook");
        BookOutDto createdBook = bookService.createBook(bookInDto);
        return ResponseEntity.status(201).body(createdBook);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book updated successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<BookOutDto> updateBook(@PathVariable Integer id, @RequestBody BookInDto bookInDto) {
        logger.info("updateBook");
        BookOutDto updatedBook = bookService.updateBook(id, bookInDto);
        return ResponseEntity.ok(updatedBook);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Integer id) {
        logger.info("deleteBook");
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
