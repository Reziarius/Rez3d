package it.uniroma3.it.rez3d.controller;

import java.lang.foreign.Linker.Option;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.it.rez3d.model.PrintFile;
import it.uniroma3.it.rez3d.service.PrintFileService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@Controller
public class PrintFileController {
    
    private final PrintFileService printFileService;

    public PrintFileController(PrintFileService printFileService){
        this.printFileService = printFileService;
    }

    @GetMapping("/files")
    public String getFiles(Model model){
        model.addAttribute("files",this.printFileService.findAll());
        return "files/listFile";
    }

    @GetMapping("files/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        Optional<PrintFile> optional = this.printFileService.findById(id);
        if(optional.isEmpty()){
            return "redirect:/files";
        }
        model.addAttribute("file",optional.get());
        return "files/showFile";
    }

    @GetMapping("/admin/files/new")
    public String createForm(Model model) {
        model.addAttribute("file", new PrintFile());
        return "admin/formFile";
    }
    @PostMapping("/admin/files")
    public String save(@Valid @ModelAttribute("file") PrintFile file, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()){
            return "admin/formFile";
        }
        printFileService.save(file);
        
        return "redirect:/files/"+file.getId();
    }
    
    @GetMapping("/admin/files/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        Optional<PrintFile> optional = printFileService.findById(id);
        if(optional.isEmpty()){
            return "redirect:/files";
        }
        model.addAttribute("file",optional.get());
        return "admin/formFile";
    }
    
    @PostMapping("/admin/files/{id}/delete")
    public String delete(@PathVariable Long id) {
        printFileService.deleteById(id);
        return "redirect:/files";
    }

    
    
}
