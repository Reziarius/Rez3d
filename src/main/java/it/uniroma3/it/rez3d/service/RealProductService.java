package it.uniroma3.it.rez3d.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.it.rez3d.model.PrintFile;
import it.uniroma3.it.rez3d.model.RealProduct;
import it.uniroma3.it.rez3d.repository.RealProductRepository;

/**
 * PrintFileService
 */
@Service
public class RealProductService {
    private final RealProductRepository productRepository;

    public RealProductService(RealProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @Transactional
    public RealProduct creaProdotto(RealProduct product, PrintFile baseFile){
        product.setFile(baseFile);
        float prezzo = baseFile.getPrice();

        if("Media".equals(product.getSize()))
            prezzo += 5.0f;
        if("Grande".equals(product.getSize()))
            prezzo += 10.0f;

        if(product.getDipinto())
            prezzo += 15.0f;

        product.setFinalPrice(prezzo);
        return productRepository.save(product);
    }

    @Transactional
    public Iterable<RealProduct> findAll(){
        return productRepository.findAll();
    }

    @Transactional
    public Optional<RealProduct> findById(Long id){
        return productRepository.findById(id);
    }

    @Transactional
    public RealProduct save(RealProduct product){
        return productRepository.save(product);
    }

    @Transactional
    public void deleteById(Long id){
        productRepository.deleteById(id);
    }
}
