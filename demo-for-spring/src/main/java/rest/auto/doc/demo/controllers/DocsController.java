package rest.auto.doc.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import rest.auto.doc.libs.controllers.DocumentingController;

@Controller
@RequestMapping("docs")
public class DocsController extends DocumentingController {
}
