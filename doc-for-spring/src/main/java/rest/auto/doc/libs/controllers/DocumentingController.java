package rest.auto.doc.libs.controllers;

import org.springframework.ui.Model;
import rest.auto.doc.libs.services.ApiService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

public class DocumentingController {

    @Autowired
    private ApiService apiService;

    @RequestMapping
    private String getControllerNames(Model model) {
        model.addAttribute("library", apiService.getEndpointLibrary());
        return "documentation";
    }
}
