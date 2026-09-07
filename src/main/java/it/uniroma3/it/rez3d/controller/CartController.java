package it.uniroma3.it.rez3d.controller;

import it.uniroma3.it.rez3d.service.OrderLineService;
import java.security.Principal;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.it.rez3d.model.Order;
import it.uniroma3.it.rez3d.model.OrderLine;
import it.uniroma3.it.rez3d.model.User;
import it.uniroma3.it.rez3d.service.OrderService;
import it.uniroma3.it.rez3d.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@Controller 
public class CartController{
    private final OrderLineService orderLineService;
    private final OrderService orderService;
    private final UserService userService;

    public CartController(OrderService orderService, UserService userService, OrderLineService orderLineService){
        this.orderService = orderService;
        this.userService = userService;
        this.orderLineService = orderLineService;
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

    @PostMapping("/cart/update/{lineId}")
    public String aggiornaQuantità(@PathVariable("lineId") Long lineId,@RequestParam("quantity") int nuovaQuantità) {
        OrderLine line = this.orderLineService.findById(lineId);

        if(line!=null){
            if(nuovaQuantità<=0){
                orderLineService.deleteById(lineId);
            }
            else{
                line.setQuantity(nuovaQuantità);
                orderLineService.save(line);
            }
        }
        return "redirect:/cart";
    }
    
    @PostMapping("/cart/remove/{lineId}")
    public String aggiornaQuantità(@PathVariable("lineId") Long lineId) {
        orderLineService.deleteById(lineId);
        return "redirect:/cart";
    }
    
}