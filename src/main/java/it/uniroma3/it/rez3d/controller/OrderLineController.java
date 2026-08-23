package it.uniroma3.it.rez3d.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.it.rez3d.service.OrderLineService;


@Controller
public class OrderLineController {
    private OrderLineService orderLineService;

    @GetMapping("/orderLines")
    public String getOrderLines(Model model) {
        model.addAttribute("orderLines",this.orderLineService.findAll());
        return "orderLines.html";
    }
    
}
