package rest.auto.doc.demo.dtos;

import rest.auto.doc.libs.annotations.Resource;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Resource
public class Author {

    @NotNull
    private String id;
    @Size(min = 5, max = 50)
    private String name;

    public String getId() {
        return this.id;
    }

    public Author id(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Author name(String name) {
        this.name = name;
        return this;
    }
}
