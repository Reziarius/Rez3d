package it.uniroma3.it.rez3d.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.it.rez3d.service.PrintFileService;

@Controller
public class PrintFileController {
    @Autowired
    private PrintFileService printFileService;

    @GetMapping("/files")
    public String getFiles(Model model){
        model.addAttribute("files",this.printFileService.findAll());
        return "files.html";
    }
}
