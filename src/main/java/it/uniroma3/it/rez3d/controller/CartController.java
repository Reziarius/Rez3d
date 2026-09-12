package it.uniroma3.it.rez3d.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.it.rez3d.model.Order;
import it.uniroma3.it.rez3d.model.OrderLine;
import it.uniroma3.it.rez3d.model.OrderState;
import it.uniroma3.it.rez3d.model.User;
import it.uniroma3.it.rez3d.service.OrderLineService;
import it.uniroma3.it.rez3d.service.OrderService;
import it.uniroma3.it.rez3d.service.UserService;

@Controller 
public class CartController {
    private final OrderLineService orderLineService;
    private final OrderService orderService;
    private final UserService userService;

    public CartController(OrderService orderService, UserService userService, OrderLineService orderLineService) {
        this.orderService = orderService;
        this.userService = userService;
        this.orderLineService = orderLineService;
    }

    @GetMapping("/cart")
    public String mostraCarrello(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        User utente = userService.findByUsername(principal.getName());
        if (utente == null) {
            return "redirect:/login";
        }

        Order carrello = orderService.getOrCreateCart(utente);
        float totale = 0.0f;

        if (carrello.getItems() != null) {
            for (OrderLine line : carrello.getItems()) {
                if (line.getProduct() != null) {
                    totale += line.getProduct().getFinalPrice() * line.getQuantity();
                }
            }
        }
        model.addAttribute("carrello", carrello);
        model.addAttribute("lines", carrello.getItems());
        model.addAttribute("totale", totale);

        return "cart/showCart";
    }

    @PostMapping("/cart/update/{lineId}")
    public String aggiornaQuantità(@PathVariable("lineId") Long lineId, @RequestParam("quantity") int nuovaQuantità, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        User utente = userService.findByUsername(principal.getName());
        OrderLine line = this.orderLineService.findById(lineId);

        if (line != null && line.getOrder() != null && line.getOrder().getUser() != null && utente != null 
                && utente.getId().equals(line.getOrder().getUser().getId())
                && OrderState.CART.equals(line.getOrder().getState())) {
            if (nuovaQuantità <= 0) {
                Order carrello = line.getOrder();
                if (carrello.getItems() != null) {
                    carrello.getItems().remove(line);
                }
                line.setOrder(null);
                orderLineService.deleteById(lineId);
                orderService.save(carrello);
            } else {
                line.setQuantity(nuovaQuantità);
                orderLineService.save(line);
            }
        }
        return "redirect:/cart";
    }
    
    @PostMapping("/cart/remove/{lineId}")
    public String rimuoviElemento(@PathVariable("lineId") Long lineId, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        User utente = userService.findByUsername(principal.getName());
        System.out.println(principal.getName());
        System.out.println(utente.getUsername());

        OrderLine line = this.orderLineService.findById(lineId);

        if (line != null && line.getOrder() != null && line.getOrder().getUser() != null && utente != null 
                && utente.getId().equals(line.getOrder().getUser().getId()) 
                && OrderState.CART.equals(line.getOrder().getState())) {
            Order carrello = line.getOrder();
            if (carrello.getItems() != null) {
                carrello.getItems().remove(line);
            }
            line.setOrder(null);
            orderLineService.deleteById(lineId);
            orderService.save(carrello);
        }
        return "redirect:/cart";
    }

    @PostMapping("/checkout")
    public String elaboraCheckout(@RequestParam("indirizzo") String indirizzo, @RequestParam("citta") String citta, @RequestParam("cap") String cap, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        User utente = userService.findByUsername(principal.getName());
        if (utente == null) {
            return "redirect:/login";
        }
        Order carrello = orderService.getOrCreateCart(utente);
        
        if (carrello.getItems() == null || carrello.getItems().isEmpty()) {
            return "redirect:/cart?error=empty";
        }

        carrello.setIndirizzoSpedizione(indirizzo);
        carrello.setCitta(citta);
        carrello.setCap(cap);
        carrello.setState(OrderState.PENDING);

        orderService.save(carrello);
        return "redirect:/successOrder";
    }

    @GetMapping("/successOrder")
    public String ordineCompletato() {
        return "cart/success";
    }
}