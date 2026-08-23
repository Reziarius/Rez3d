package it.uniroma3.it.rez3d.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.it.rez3d.model.PrintFile;
import it.uniroma3.it.rez3d.repository.PrintFileRepository;

/**
 * PrintFileService
 */
@Service
public class PrintFileService {
    @Autowired
    private PrintFileRepository printFileRepository;

    public Iterable<PrintFile> findAll(){
        return this.printFileRepository.findAll();
    }

    public PrintFile findById(Long id){
        return this.printFileRepository.findById(id).orElse(null);
    }

    public PrintFile save(PrintFile file){
        return this.printFileRepository.save(file);
    }

    public void deleteById(Long id){
        this.printFileRepository.deleteById(id);
    }
}
