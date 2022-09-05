package org.glebchanskiy.Project2.repositories;

import org.glebchanskiy.Project2.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BooksRepository extends JpaRepository<Book, Integer> {
    List<Book> findAllByTitleStartingWith(String startWith);
}
