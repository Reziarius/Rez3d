package it.uniroma3.it.rez3d.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.it.rez3d.model.Order;
import it.uniroma3.it.rez3d.model.OrderState;
import it.uniroma3.it.rez3d.model.User;
import it.uniroma3.it.rez3d.repository.OrderRepository;
import jakarta.transaction.Transactional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Transactional 
    public Order getOrCreateCart(User user){
        Optional<Order> cartOpt = orderRepository.findByUserAndState(user,OrderState.CART);
        if(cartOpt.isPresent())
            return cartOpt.get();
        Order newCart = new Order();
        newCart.setUser(user);
        newCart.setState(OrderState.CART);
        newCart.setDate(LocalDate.now());
        
        return orderRepository.save(newCart);
    }

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

    @Transactional 
    public List<Order> findByUser(User user){
        List<Order> result = this.orderRepository.findByUser(user);
        return result;
    }
}
