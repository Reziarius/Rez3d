package it.uniroma3.it.rez3d.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import it.uniroma3.it.rez3d.model.PrintFile;
import it.uniroma3.it.rez3d.model.RealProduct;
import it.uniroma3.it.rez3d.service.PrintFileService;
import it.uniroma3.it.rez3d.service.RealProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class RealProductController {
    private final RealProductService realProductService;
    private final PrintFileService printFileService;

    public RealProductController(RealProductService realProductService,PrintFileService printFileService){
        this.realProductService = realProductService;
        this.printFileService = printFileService;
    }

    @GetMapping("files/{id}/personalizza")
    public String formPersonalizzazione(@PathVariable Long id, Model model) {
        PrintFile file = printFileService.findById(id).get();

        model.addAttribute("file",file);
        model.addAttribute("product", new RealProduct());
        return "/products/formPersonalizzaProdotto";
    }



    @PostMapping("files/{id}/personalizza")
    public String salvaProdottoPersonalizzato(@PathVariable Long id, @ModelAttribute("product") RealProduct product) {
        PrintFile file = printFileService.findById(id).get();

        realProductService.creaProdotto(product, file);
        return "redirect:/";
    }
    

}
