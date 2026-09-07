package it.uniroma3.it.rez3d.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.it.rez3d.model.Order;
import it.uniroma3.it.rez3d.model.OrderLine;
import it.uniroma3.it.rez3d.model.User;
import it.uniroma3.it.rez3d.service.OrderService;
import it.uniroma3.it.rez3d.service.UserService;

@Controller 
public class CartController{
    private final OrderService orderService;
    private final UserService userService;

    public CartController(OrderService orderService, UserService userService){
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/cart")
    public String mostraCarrello(Model model, Principal principal) {
        String username = principal.getName();
        User utente = userService.findByUsername(username);

        Order carrello = orderService.getOrCreateCart(utente);
        float totale = 0.0f;

        if(carrello.getItems() != null){
            for(OrderLine line : carrello.getItems()){
                totale += line.getProduct().getFinalPrice() * line.getQuantity();
            }
        }
        model.addAttribute("carrello",carrello);
        model.addAttribute("lines",carrello.getItems());
        model.addAttribute("totale",totale);

        return "cart/showCart";

    }
    
}