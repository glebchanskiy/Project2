package org.glebchanskiy.Project2.services;

import org.glebchanskiy.Project2.models.Book;
import org.glebchanskiy.Project2.models.Person;
import org.glebchanskiy.Project2.repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Person findById(int id) {
        return peopleRepository.findById(id).orElse(null);
    }

    public Person findByName(String name) {
        return peopleRepository.findByName(name);
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson) {
        updatedPerson.setId(id);
        peopleRepository.save(updatedPerson);
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }

    public List<Book> getBookByPersonId(int id) {
        Optional<Person> person = peopleRepository.findById(id);

        if (person.isPresent()) {
            person.get().getBooks().forEach(book -> {
                long diff = Math.abs(book.getAssignDate().getTime() - new Date().getTime());
                if (diff > 864000000)
                    book.setOverdue(true);
            });
            return person.get().getBooks();
        }
        else {
            return Collections.emptyList();
        }
    }
}
