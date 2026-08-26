package it.uniroma3.it.rez3d.controller;

import org.springframework.stereotype.Controller;

import it.uniroma3.it.rez3d.service.RealProductService;

@Controller
public class RealProductController {
    private final RealProductService realProductService;

    public RealProductController(RealProductService realProductService){
        this.realProductService = realProductService;
    }

    
}
