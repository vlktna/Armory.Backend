package blur.tech.armory.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = {"*"}, allowedHeaders = {"*"})
@RestController
public class MainController {

    @GetMapping("/main")
    public String mainPage() {
        return "oh shit";
    }

    @GetMapping("/")
    public String testPage() {
        return "you need to zaregistrirovat'sia";
    }
}
