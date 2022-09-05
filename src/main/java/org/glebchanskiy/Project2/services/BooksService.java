package org.glebchanskiy.Project2.services;

import org.glebchanskiy.Project2.models.Book;
import org.glebchanskiy.Project2.models.Person;
import org.glebchanskiy.Project2.repositories.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {

    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> findAll(Boolean sortByYear) {
        List<Book> books = null;

        if (sortByYear)
            books = booksRepository.findAll(Sort.by("year"));
        else
            books = booksRepository.findAll();

        return books;
    }

    public List<Book> findAllWithPage(Integer page, Integer itemsPerPage, Boolean sortByYear) {
        List<Book> books = null;
        if (sortByYear)
            books = booksRepository.findAll(PageRequest.of(page,itemsPerPage, Sort.by("year")))
                    .getContent();
        else
            books = booksRepository.findAll(PageRequest.of(page,itemsPerPage))
                    .getContent();

        return books;
    }
    public Book findById(int id) {
        Optional<Book> book = booksRepository.findById(id);
        return book.orElse(null);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook) {

        Book book = booksRepository.findById(id).get();

        updatedBook.setId(id);
        updatedBook.setOwner(book.getOwner());
        updatedBook.setAssignDate(book.getAssignDate());
        booksRepository.save(updatedBook);
    }

    @Transactional
    public void release(int id) {
        booksRepository.findById(id).ifPresent(book -> {
            book.setOwner(null);
            book.setAssignDate(null);
        });
    }

    @Transactional
    public void assign(int id, Person person) {
        booksRepository.findById(id).ifPresent(book -> {
            book.setOwner(person);
            book.setAssignDate(new Date());
        });

    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }


    public List<Book> findAllByTittleStartingWith(String startWith) {
        return booksRepository.findAllByTitleStartingWith(startWith);
    }
}
