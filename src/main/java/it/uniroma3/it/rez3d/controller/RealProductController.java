package it.uniroma3.it.rez3d.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import it.uniroma3.it.rez3d.model.Order;
import it.uniroma3.it.rez3d.model.PrintFile;
import it.uniroma3.it.rez3d.model.RealProduct;
import it.uniroma3.it.rez3d.model.User;
import it.uniroma3.it.rez3d.service.OrderService;
import it.uniroma3.it.rez3d.service.PrintFileService;
import it.uniroma3.it.rez3d.service.RealProductService;
import it.uniroma3.it.rez3d.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RealProductController {
    private final RealProductService realProductService;
    private final PrintFileService printFileService;
    private final UserService userService;
    private final OrderService orderService;

    public RealProductController(RealProductService realProductService, PrintFileService printFileService,UserService userService,OrderService orderService) {
        this.realProductService = realProductService;
        this.printFileService = printFileService;
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping("files/{id}/personalizza")
    public String formPersonalizzazione(@PathVariable Long id, Model model) {
        RealProduct product = new RealProduct();

        System.out.println("NUOVO PRODUCT = " + product);
        System.out.println("NUOVO PRODUCT ID = " + product.getId());
        
        PrintFile file = printFileService.findById(id).get();

        model.addAttribute("file", file);
        model.addAttribute("product", new RealProduct());
        return "/products/formPersonalizzaProdotto";
    }

    @PostMapping("files/{fileId}/personalizza")
    public String salvaProdottoPersonalizzato(@PathVariable("fileId") Long fileId, 
                                                @ModelAttribute("product") RealProduct product,
                                                Principal principal) {
        
        PrintFile file = printFileService.findById(fileId).get();
        String username = principal.getName();
        User loggedUser = userService.findByUsername(username);
        Order carrello = orderService.getOrCreateCart(loggedUser);
        product.setOrder(carrello);
        realProductService.creaProdotto(product, file);
        return "redirect:/cart";
    }

}
