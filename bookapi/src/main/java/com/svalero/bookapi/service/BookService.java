package com.svalero.bookapi.service;

import com.svalero.bookapi.domain.Author;
import com.svalero.bookapi.domain.Book;
import com.svalero.bookapi.domain.dto.BookInDto;
import com.svalero.bookapi.domain.dto.BookOutDto;
import com.svalero.bookapi.exception.AuthorNotFoundException;
import com.svalero.bookapi.exception.BookNotFoundException;
import com.svalero.bookapi.repository.AuthorRepository;
import com.svalero.bookapi.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

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
        Optional<Author> authorOpt = authorRepository.findById(bookInDto.getAuthorId());
        if (authorOpt.isPresent()) {
            Book book = modelMapper.map(bookInDto, Book.class);
            book.setAuthor(authorOpt.get());
            Book savedBook = bookRepository.save(book);
            return modelMapper.map(savedBook, BookOutDto.class);
        } else {
            throw new AuthorNotFoundException("Author not found with ID: " + bookInDto.getAuthorId());
        }
    }

    public BookOutDto updateBook(Integer id, BookInDto bookInDto) {
        Optional<Book> bookOpt = bookRepository.findById(id);
        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();
            modelMapper.map(bookInDto, book);
            Optional<Author> authorOpt = authorRepository.findById(bookInDto.getAuthorId());
            authorOpt.ifPresent(book::setAuthor);
            Book updatedBook = bookRepository.save(book);
            return modelMapper.map(updatedBook, BookOutDto.class);
        } else {
            throw new BookNotFoundException("Book not found with ID: " + id);
        }
    }

    public void deleteBook(Integer id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
        } else {
            throw new BookNotFoundException("Book not found with ID: " + id);
        }
    }
}

