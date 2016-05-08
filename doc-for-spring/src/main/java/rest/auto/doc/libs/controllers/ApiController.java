package rest.auto.doc.libs.controllers;

import rest.auto.doc.libs.services.ApiService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ApiController {

    @Autowired
    private ApiService apiService;

    @ResponseBody
    @RequestMapping("/docs")
    private String getControllerNames() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return "<pre>" + gson.toJson(apiService.getEndpointLibrary()) + "</pre>";
    }
}
