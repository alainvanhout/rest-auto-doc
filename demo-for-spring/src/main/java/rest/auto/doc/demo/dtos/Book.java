package rest.auto.doc.demo.dtos;

import rest.auto.doc.libs.annotations.Resource;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Resource
public class Book {

    @NotNull
    private String id;
    @Size(min = 5, max = 50)
    private String name;
    private String author;

    public String getId() {
        return id;
    }

    public Book id(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Book name(String name) {
        this.name = name;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public Book author(String author) {
        this.author = author;
        return this;
    }
}
