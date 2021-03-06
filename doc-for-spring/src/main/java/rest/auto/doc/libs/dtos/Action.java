package rest.auto.doc.libs.dtos;

import java.util.Map;

public class Action {

    private String method;
    private Map<String, ?> requestBody;
    private Map<String, ?> responseBody;
    private String path;

    public String getMethod() {
        return this.method;
    }

    public Action method(String method) {
        this.method = method;
        return this;
    }

    public Map<String, ?> getRequestBody() {
        return requestBody;
    }

    public Action requestBody(Map<String, ?> requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    public Map<String, ?> getResponseBody() {
        return responseBody;
    }

    public Action responseBody(Map<String, ?> responseBody) {
        this.responseBody = responseBody;
        return this;
    }

    public Action path(String path) {
        this.path = path;
        return this;
    }

    public String getPath() {
        return path;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setRequestBody(Map<String, ?> requestBody) {
        this.requestBody = requestBody;
    }

    public void setResponseBody(Map<String, ?> responseBody) {
        this.responseBody = responseBody;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
