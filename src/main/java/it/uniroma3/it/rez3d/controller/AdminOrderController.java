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
import it.uniroma3.it.rez3d.model.User;
import it.uniroma3.it.rez3d.service.OrderService;
import it.uniroma3.it.rez3d.service.UserService;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

    private final OrderService orderService;
    private final UserService userService;

    public AdminOrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping
    public String visualizzaDashboard(@RequestParam(value = "state", required = false) OrderState state,
                                       @RequestParam(value = "userId", required = false) Long userId,
                                       Model model) {
        User selectedUser = (userId != null) ? userService.getUser(userId) : null;
        List<Order> ordini = orderService.getOrdiniFiltrati(state, selectedUser);
        List<User> utenti = userService.findAllSorted();

        model.addAttribute("ordini", ordini);
        model.addAttribute("utenti", utenti);
        model.addAttribute("selectedState", state);
        model.addAttribute("selectedUserId", userId);

        return "admin/dashboardOrdini";
    }

    @PostMapping("/{id}/update-state")
    public String aggiornaStatoOrdine(@PathVariable("id") Long id, @RequestParam("state") OrderState state) {
        orderService.aggiornaStato(id, state);
        return "redirect:/admin/orders";
    }
}