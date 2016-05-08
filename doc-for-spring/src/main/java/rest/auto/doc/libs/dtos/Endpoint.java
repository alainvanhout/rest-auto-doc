package rest.auto.doc.libs.dtos;

import java.util.ArrayList;
import java.util.List;

public class Endpoint {

    private List<Action> actions = new ArrayList<>();

    public void add(Action action) {
        actions.add(action);
    }
}
