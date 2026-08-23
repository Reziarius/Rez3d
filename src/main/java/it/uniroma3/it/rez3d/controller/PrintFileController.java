package it.uniroma3.it.rez3d.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.it.rez3d.model.PrintFile;
import it.uniroma3.it.rez3d.service.PrintFileService;


@Controller
public class PrintFileController {
    @Autowired
    private PrintFileService printFileService;

    @GetMapping("/products")
    public String getFiles(Model model){
        model.addAttribute("products",this.printFileService.findAll());
        return "products/listProduct";
    }

    @GetMapping("products/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        Optional<PrintFile> optional = this.printFileService.findById(id);
        if(optional.isEmpty()){
            return "redirect:/products";
        }

        model.addAttribute("product",optional.get());
        return "products/showProduct";
    }
    
}
