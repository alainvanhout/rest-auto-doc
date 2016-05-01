package rest.auto.doc.dtos;

public class ResourcePath {

    private String path;
    private String method;
    private Class requestBody;
    private Class responseBody;

    public String getPath() {
        return this.path;
    }

    public ResourcePath path(String path) {
        this.path = path;
        return this;
    }

    public String getMethod() {
        return this.method;
    }

    public ResourcePath method(String method) {
        this.method = method;
        return this;
    }

    public Class getRequestBody() {
        return this.requestBody;
    }

    public ResourcePath requestBody(Class requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    public Class getResponseBody() {
        return this.responseBody;
    }

    public ResourcePath responseBody(Class responseBody) {
        this.responseBody = responseBody;
        return this;
    }
}
