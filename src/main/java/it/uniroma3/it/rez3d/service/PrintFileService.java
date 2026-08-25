package it.uniroma3.it.rez3d.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.it.rez3d.model.PrintFile;
import it.uniroma3.it.rez3d.repository.PrintFileRepository;

/**
 * PrintFileService
 */
@Service
public class PrintFileService {
    private final PrintFileRepository printFileRepository;

    public PrintFileService(PrintFileRepository printFileRepository){
        this.printFileRepository = printFileRepository;
    }

    @Transactional
    public Iterable<PrintFile> findAll(){
        return printFileRepository.findAll();
    }

    @Transactional
    public Optional<PrintFile> findById(Long id){
        return printFileRepository.findById(id);
    }

    @Transactional
    public PrintFile save(PrintFile file){
        return printFileRepository.save(file);
    }

    @Transactional
    public void deleteById(Long id){
        printFileRepository.deleteById(id);
    }
}
