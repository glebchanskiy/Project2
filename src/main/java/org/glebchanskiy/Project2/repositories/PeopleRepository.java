package org.glebchanskiy.Project2.repositories;

import org.glebchanskiy.Project2.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PeopleRepository extends JpaRepository<Person, Integer> {
    public Person findByName(String name);
}
