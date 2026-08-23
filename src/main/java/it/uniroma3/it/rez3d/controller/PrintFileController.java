package it.uniroma3.it.rez3d.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import it.uniroma3.it.rez3d.service.PrintFileService;

@Controller
public class PrintFileController {
    @Autowired
    private PrintFileService printFileService;
}
