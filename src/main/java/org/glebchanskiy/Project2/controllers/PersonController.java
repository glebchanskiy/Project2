package org.glebchanskiy.Project2.controllers;

import org.glebchanskiy.Project2.models.Person;
import org.glebchanskiy.Project2.services.BooksService;
import org.glebchanskiy.Project2.services.PeopleService;
import org.glebchanskiy.Project2.util.PersonValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/people")
public class PersonController {


    private final PeopleService peopleService;
    private final BooksService booksService;

    private final PersonValidator personValidator;

    public PersonController(PeopleService peopleService, BooksService booksService, PersonValidator personValidator) {
        this.peopleService = peopleService;
        this.booksService = booksService;
        this.personValidator = personValidator;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("people", peopleService.findAll());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        Person person = peopleService.findById(id);

        if (person != null) {
            model.addAttribute("person", person);
            model.addAttribute("books", peopleService.getBookByPersonId(id));
        }
        return "people/show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person,Model model) {
        return "people/new";
    }

    @PostMapping()
    public String save(@ModelAttribute("person") Person person,
                       BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            System.out.println("err");
            return "people/new";
        }
        System.out.println("ok");
        peopleService.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", peopleService.findById(id));
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String editPerson(@PathVariable("id") int id,
                             @ModelAttribute("person") Person person,
                             BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);

        if(bindingResult.hasErrors())
            return "people/edit";

        peopleService.update(id, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        peopleService.delete(id);
        return "redirect:/people";
    }

}
