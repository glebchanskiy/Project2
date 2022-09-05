package org.glebchanskiy.Project2.util;

import org.glebchanskiy.Project2.models.Book;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class BookValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Book.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Book validBook = (Book) target;

        if (validBook.getAuthor().isEmpty() || validBook.getAuthor()==null)
            errors.rejectValue("author", "", "Укажите автора");
        if (validBook.getTitle().isEmpty() || validBook.getTitle()==null)
            errors.rejectValue("title", "", "Укажите название книги");
        if (validBook.getYear() <= 0 || validBook.getYear() >= 2022)
            errors.rejectValue("year", "", "Укажите год публикации");
    }
}
