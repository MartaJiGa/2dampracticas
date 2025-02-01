package com.svalero.bookapi.service;

import com.svalero.bookapi.controller.BookController;
import com.svalero.bookapi.domain.Author;
import com.svalero.bookapi.domain.Book;
import com.svalero.bookapi.domain.dto.AuthorInDto;
import com.svalero.bookapi.domain.dto.AuthorOutDto;
import com.svalero.bookapi.domain.dto.BookInDto;
import com.svalero.bookapi.domain.dto.BookOutDto;
import com.svalero.bookapi.exception.AuthorNotFoundException;
import com.svalero.bookapi.exception.BookNotFoundException;
import com.svalero.bookapi.repository.AuthorRepository;
import com.svalero.bookapi.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final Logger logger = LoggerFactory.getLogger(BookService.class);

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<BookOutDto> getAllBooks() {
        List<Book> books = (List<Book>) bookRepository.findAll();
        return books.stream()
                .map(book -> modelMapper.map(book, BookOutDto.class))
                .collect(Collectors.toList());
    }

    public BookOutDto getBookDetails(Integer id) {
        Optional<Book> bookOpt = bookRepository.findById(id);
        return bookOpt.map(book -> modelMapper.map(book, BookOutDto.class))
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + id));
    }

    public BookOutDto createBook(BookInDto bookInDto) {
        Book book = new Book();
        modelMapper.map(bookInDto, book);
        book.setId(null);
        Book savedBook = bookRepository.save(book);
        return modelMapper.map(savedBook, BookOutDto.class);
    }

    public BookOutDto updateBook(Integer id, BookInDto bookInDto) {
        Optional<Book> bookOpt = bookRepository.findById(id);
        if (bookOpt.isEmpty()) {
            throw new BookNotFoundException("Book not found with ID: " + id);
        }

        Book book = bookOpt.get();

        if (bookInDto.getAuthorId() != null && !bookInDto.getAuthorId().equals(book.getAuthor().getId())) {
            Optional<Author> authorOpt = authorRepository.findById(bookInDto.getAuthorId());
            if (authorOpt.isEmpty()) {
                throw new AuthorNotFoundException("Author not found with ID: " + bookInDto.getAuthorId());
            }
            book.setAuthor(authorOpt.get());
        }

        book.setId(id);

        book.setTitle(bookInDto.getTitle());
        book.setGenre(bookInDto.getGenre());
        book.setPublicationDate(bookInDto.getPublicationDate());
        book.setAvailable(bookInDto.getAvailable());

        Book updatedBook = bookRepository.save(book);
        return modelMapper.map(updatedBook, BookOutDto.class);
    }


    public void deleteBook(Integer id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
        } else {
            throw new BookNotFoundException("Book not found with ID: " + id);
        }
    }
}

