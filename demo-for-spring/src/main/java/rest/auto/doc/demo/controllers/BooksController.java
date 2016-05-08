package rest.auto.doc.demo.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import rest.auto.doc.libs.ApiDocumented;
import rest.auto.doc.demo.dtos.Author;
import rest.auto.doc.demo.dtos.Book;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("books")
public class BooksController implements ApiDocumented {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    private List<Book> findAll(){
        return Arrays.asList(new Book().id("1").name("Moby Dick").author("2"),
                new Book().id("2").name("Moby Dick - The Sequel").author("3"));
    }

    @RequestMapping(value = "/", method = {RequestMethod.POST, RequestMethod.PUT})
    private Book create(@Valid Author author){
        return new Book().id("1").name("Moby Dick").author("2");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    private Book find(@PathVariable("id") String id){
        return new Book().id(id).name("Moby Dick").author("2");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    private void update(@PathVariable("id") String id, @Valid Book author){
    }
}
