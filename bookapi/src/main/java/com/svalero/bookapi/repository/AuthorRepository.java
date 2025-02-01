package com.svalero.bookapi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.svalero.bookapi.domain.Author;

@Repository
public interface AuthorRepository extends CrudRepository<Author, Integer> {
}
