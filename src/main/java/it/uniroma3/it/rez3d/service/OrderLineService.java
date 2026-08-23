package it.uniroma3.it.rez3d.service;

import org.springframework.stereotype.Service;

import it.uniroma3.it.rez3d.model.OrderLine;
import it.uniroma3.it.rez3d.repository.OrderLineRepository;

@Service
public class OrderLineService{
    private OrderLineRepository orderLineRepository;

    public Iterable<OrderLine> findAll(){
        return this.orderLineRepository.findAll();
    }

    public OrderLine findById(Long id){
        return this.orderLineRepository.findById(id).orElse(null);
    }

    public OrderLine save(OrderLine orderLine){
        return this.orderLineRepository.save(orderLine);
    }

    public void deleteById(Long id){
        this.orderLineRepository.deleteById(id);
    }
}