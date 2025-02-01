package com.svalero.bookapi.service;

import com.svalero.bookapi.domain.Author;
import com.svalero.bookapi.domain.dto.AuthorInDto;
import com.svalero.bookapi.domain.dto.AuthorOutDto;
import com.svalero.bookapi.exception.AuthorNotFoundException;
import com.svalero.bookapi.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<AuthorOutDto> getAllAuthors() {
        List<Author> authors = (List<Author>) authorRepository.findAll();
        return authors.stream()
                .map(author -> modelMapper.map(author, AuthorOutDto.class))
                .collect(Collectors.toList());
    }

    public AuthorOutDto getAuthorDetails(Integer id) {
        Optional<Author> authorOpt = authorRepository.findById(id);
        return authorOpt.map(author -> modelMapper.map(author, AuthorOutDto.class))
                .orElseThrow(() -> new AuthorNotFoundException("Author not found with ID: " + id));
    }

    public AuthorOutDto createAuthor(AuthorInDto authorInDto) {
        Author author = modelMapper.map(authorInDto, Author.class);
        Author savedAuthor = authorRepository.save(author);
        return modelMapper.map(savedAuthor, AuthorOutDto.class);
    }

    public AuthorOutDto updateAuthor(Integer id, AuthorInDto authorInDto) {
        Optional<Author> authorOpt = authorRepository.findById(id);
        if (authorOpt.isPresent()) {
            Author author = authorOpt.get();
            modelMapper.map(authorInDto, author);
            Author updatedAuthor = authorRepository.save(author);
            return modelMapper.map(updatedAuthor, AuthorOutDto.class);
        } else {
            throw new AuthorNotFoundException("Author not found with ID: " + id);
        }
    }

    public void deleteAuthor(Integer id) {
        if (authorRepository.existsById(id)) {
            authorRepository.deleteById(id);
        } else {
            throw new AuthorNotFoundException("Author not found with ID: " + id);
        }
    }
}
