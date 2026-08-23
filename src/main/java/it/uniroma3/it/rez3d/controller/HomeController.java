package it.uniroma3.it.rez3d.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class HomeController {
    
    @GetMapping("/")
    public String getHome() {
        return "index.html";
    }
    
}
