package org.glebchanskiy.Project2.controllers;

import org.glebchanskiy.Project2.models.Book;
import org.glebchanskiy.Project2.models.Person;
import org.glebchanskiy.Project2.services.BooksService;
import org.glebchanskiy.Project2.services.PeopleService;
import org.glebchanskiy.Project2.util.BookValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BooksService booksService;
    private final PeopleService peopleService;
    private final BookValidator bookValidator;

    public BookController(BooksService booksService, PeopleService peopleService, BookValidator bookValidator) {
        this.booksService = booksService;
        this.peopleService = peopleService;
        this.bookValidator = bookValidator;
    }


    @GetMapping("/search")
    public String search (@RequestParam(name = "start_with", required = false) String startWith,
                          @ModelAttribute("book") Book book,
                          Model model) {
        if (startWith!=null)
            model.addAttribute("foundedBooks", booksService.findAllByTittleStartingWith(startWith));

        return "books/search";
    }


    @GetMapping()
    public String index( @RequestParam(name = "page", required = false) Integer page,
                         @RequestParam(name = "books_per_page", required = false) Integer booksPerPage,
                         @RequestParam(name = "sort_by_year", required = false) Boolean sortByYear,
                         Model model) {

        if (sortByYear==null)
            sortByYear = false;


        if (page != null && booksPerPage != null) {
            model.addAttribute("books", booksService.findAllWithPage(page, booksPerPage, sortByYear));
        }
        else {
            model.addAttribute("books", booksService.findAll(sortByYear));
        }
        return "books/index";
    }


    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id,
                       @ModelAttribute("person") Person person,
                       Model model) {

        Book book = booksService.findById(id);

        if (book != null) {
            model.addAttribute("book", book);
            Person owner = book.getOwner();
            System.out.println(owner);
            if (owner!=null) {
                model.addAttribute("owner", owner);
            } else {
                model.addAttribute("persons", peopleService.findAll());
            }
        }

        return "books/show";
    }

    @GetMapping("/new")
    public String index(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping()
    public String save(@ModelAttribute("book") Book book,
                       BindingResult bindingResult) {

        bookValidator.validate(book, bindingResult);

        if(bindingResult.hasErrors())
            return "books/new";

        booksService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id,
                       Model model) {
        model.addAttribute("book", booksService.findById(id));
        return "books/edit";
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id) {
        booksService.release(id);
        return "redirect:/books/"+id;
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int book_id,
                         @ModelAttribute("person") Person person) {

        booksService.assign(book_id, person);

        return "redirect:/books/"+book_id;
    }

    @PatchMapping("/{id}")
    public String editBook(@PathVariable("id") int id,
                           @ModelAttribute("book") Book book,
                           BindingResult bindingResult) {
        bookValidator.validate(book, bindingResult);

        if (bindingResult.hasErrors())
            return "books/edit";

        booksService.update(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        booksService.delete(id);
        return "redirect:/books";
    }
}
