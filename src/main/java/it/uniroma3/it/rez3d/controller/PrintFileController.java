package it.uniroma3.it.rez3d.controller;

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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("/files/{id}")
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
    public String save(@Valid @ModelAttribute("file") PrintFile file, 
                       BindingResult bindingResult, 
                       @RequestParam(value = "stlFile", required = false) MultipartFile stlFile,
                       Model model) {
        if(bindingResult.hasErrors()){
            return "admin/formFile";
        }
        
        if (file.getId() != null) {
            Optional<PrintFile> existingOpt = printFileService.findById(file.getId());
            if (existingOpt.isPresent() && (stlFile == null || stlFile.isEmpty())) {
                file.setStlPath(existingOpt.get().getStlPath());
            }
        }
        
        if (stlFile != null && !stlFile.isEmpty()) {
            try {
                String originalFilename = stlFile.getOriginalFilename();
                String extension = ".stl";
                if (originalFilename != null && originalFilename.contains(".")) {
                    extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                }
                String filename = UUID.randomUUID().toString() + extension;
                Path uploadPath = Paths.get("uploads/models");
                Files.createDirectories(uploadPath);
                Path targetLocation = uploadPath.resolve(filename);
                Files.copy(stlFile.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
                file.setStlPath("/uploads/models/" + filename);
            } catch (IOException e) {
                bindingResult.reject("upload.error", "Errore durante il salvataggio del file STL: " + e.getMessage());
                return "admin/formFile";
            }
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
