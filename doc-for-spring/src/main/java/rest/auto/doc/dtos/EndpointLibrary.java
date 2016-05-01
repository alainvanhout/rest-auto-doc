package rest.auto.doc.dtos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EndpointLibrary {

    private Map<String, Endpoint> endpoints = new HashMap<>();

    public Map<String, Endpoint> getEndpoints() {
        return endpoints;
    }

    public EndpointLibrary addAction(Action action) {
        String path = action.getPath();
        if (!endpoints.containsKey(path)){
            endpoints.put(path, new Endpoint());
        }
        endpoints.get(path).add(action);
        return this;
    }
}
