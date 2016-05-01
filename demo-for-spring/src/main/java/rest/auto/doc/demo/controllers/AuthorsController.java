package rest.auto.doc.demo.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import rest.auto.doc.ApiDocumented;
import rest.auto.doc.demo.dtos.Author;
import rest.auto.doc.demo.dtos.Book;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("authors")
public class AuthorsController implements ApiDocumented {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    private List<Author> findAll() {
        return Arrays.asList(new Author().id("1").name("Alain"),
                new Author().id("2").name("Herman Melville"),
                new Author().id("3").name("John Smith"));
    }

    @RequestMapping(value = "/", method = {RequestMethod.POST, RequestMethod.PUT})
    private Author create(@Valid Author author) {
        return new Author().id("1").name("Alain");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    private Author find(@PathVariable("id") String id) {
        return new Author().id(id).name("Alain");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    private void update(@PathVariable("id") String id, @Valid Author author) {
    }

    @RequestMapping(value = "/{id}/books", method = RequestMethod.GET)
    private List<Book> books(@PathVariable("id") String id) {
        switch (id) {
            case "2":
                return Arrays.asList(new Book().id("1").name("Moby Dick").author("2"));
            case "3":
                return Arrays.asList(new Book().id("2").name("Moby Dick - The Sequel").author("3"));
            default:
                return Collections.EMPTY_LIST;
        }
    }
}
