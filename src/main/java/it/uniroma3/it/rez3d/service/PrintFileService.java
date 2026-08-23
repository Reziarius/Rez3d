package it.uniroma3.it.rez3d.service;

import org.springframework.stereotype.Service;

import it.uniroma3.it.rez3d.repository.PrintFileRepository;

/**
 * PrintFileService
 */
@Service
public class PrintFileService {
    private PrintFileRepository printFileRepository;

    public PrintFileService(PrintFileRepository printFileRepository){
        this.printFileRepository = printFileRepository;
    }
}
