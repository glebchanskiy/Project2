package org.glebchanskiy.Project2.util;

import org.glebchanskiy.Project2.models.Person;
import org.glebchanskiy.Project2.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {

    private final PeopleService peopleService;

    @Autowired
    public PersonValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }
    @Override
    public void validate(Object target, Errors errors) {
    Person validPerson = (Person) target;
        if (peopleService.findByName(validPerson.getName()) != null)
            errors.rejectValue("name","", "Человек с таким именем уже существует");
        if (validPerson.getBirthYear() < 1900 || validPerson.getBirthYear() > 2100)
            errors.rejectValue("birthYear", "", "Укажите валидную дату");
        if (validPerson.getName().length() < 3 || validPerson.getName().length() > 40)
            errors.rejectValue("name", "", "Имя должно быть 3-40 символов");
    }
}
