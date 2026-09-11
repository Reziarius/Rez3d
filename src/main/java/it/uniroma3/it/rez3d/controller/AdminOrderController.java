package it.uniroma3.it.rez3d.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.it.rez3d.model.Order;
import it.uniroma3.it.rez3d.model.OrderState;
import it.uniroma3.it.rez3d.service.OrderService;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

    private final OrderService orderService;

    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String visualizzaDashboard(Model model) {
        List<Order> ordini = orderService.getOrdiniRicevuti();
        model.addAttribute("ordini", ordini);
        return "admin/dashboardOrdini";
    }

    @PostMapping("/{id}/update-state")
    public String aggiornaStatoOrdine(@PathVariable("id") Long id, @RequestParam("state") OrderState state) {
        orderService.aggiornaStato(id, state);
        return "redirect:/admin/orders";
    }
}