package it.uniroma3.it.rez3d.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.it.rez3d.model.Order;
import it.uniroma3.it.rez3d.repository.OrderRepository;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public Iterable<Order> findAll(){
        return this.orderRepository.findAll();
    }

    public Order findById(Long id){
        return this.orderRepository.findById(id).orElse(null);
    }

    public Order save(Order order){
        return this.orderRepository.save(order);
    }

    public void deleteById(Long id){
        this.orderRepository.deleteById(id);
    }
}
