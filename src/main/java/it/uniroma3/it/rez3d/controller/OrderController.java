package it.uniroma3.it.rez3d.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.it.rez3d.model.Order;
import it.uniroma3.it.rez3d.model.OrderLine;
import it.uniroma3.it.rez3d.model.OrderState;
import it.uniroma3.it.rez3d.model.User;
import it.uniroma3.it.rez3d.service.OrderService;
import it.uniroma3.it.rez3d.service.UserService;

import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/orders")
    public String getOrders(Model model) {
        model.addAttribute("orders", this.orderService.findAll());
        return "orders.html";
    }

    @GetMapping("/storico")
    public String storicoOrdini(Model model, Principal principal) {
        String username = principal.getName();
        User utente = userService.findByUsername(username);

        // recuperiamo tutti gli ordini dell'utente
        List<Order> tuttiGliOrdini = orderService.findByUser(utente);
        List<Order> ordiniPassati = tuttiGliOrdini.stream().filter(o -> o.getState() != OrderState.CART).toList();

        model.addAttribute("ordini", ordiniPassati);
        return "orders/storico";
    }

    @GetMapping("/storico/{id}")
    public String dettaglioOrdine(@PathVariable("id") Long id, Model model, Principal principal) {
        Order ordine = orderService.findById(id); // Assicurati di avere questo metodo nel Service

        // Sicurezza: blocchiamo l'accesso se l'ordine non esiste o è di un altro utente
        if (ordine == null || !ordine.getUser().getUsername().equals(principal.getName())) {
            return "redirect:/storico";
        }

        // Calcoliamo il totale dell'ordine (se non hai già un campo total nell'entità)
        float totale = 0;
        if (ordine.getItems() != null) {
            for (OrderLine line : ordine.getItems()) {
                totale += (line.getQuantity() * line.getProduct().getFinalPrice());
            }
        }

        model.addAttribute("ordine", ordine);
        model.addAttribute("totale", totale);

        return "/orders/orderDetails"; // Rimanda al nuovo file HTML
    }

}
