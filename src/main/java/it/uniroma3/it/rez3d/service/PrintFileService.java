package it.uniroma3.it.rez3d.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.it.rez3d.model.PrintFile;
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

    public Iterable<PrintFile> findAll(){
        return printFileRepository.findAll();
    }

    public Optional<PrintFile> findById(Long id){
        return printFileRepository.findById(id);
    }

    public PrintFile save(PrintFile file){
        return printFileRepository.save(file);
    }

    public void deleteById(Long id){
        printFileRepository.deleteById(id);
    }
}
