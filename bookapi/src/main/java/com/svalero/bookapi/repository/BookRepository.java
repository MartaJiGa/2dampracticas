package com.svalero.bookapi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.svalero.bookapi.domain.Book;

@Repository
public interface BookRepository extends CrudRepository<Book, Integer> {
}
