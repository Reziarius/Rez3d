package it.uniroma3.it.rez3d.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.it.rez3d.service.OrderService;

public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/orders")
    public String getOrders(Model model){
        model.addAttribute("orders",this.orderService.findAll());
        return "orders.html";
    }
}
