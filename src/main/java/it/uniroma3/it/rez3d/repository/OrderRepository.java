package it.uniroma3.it.rez3d.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.it.rez3d.model.Order;
import it.uniroma3.it.rez3d.model.OrderState;
import it.uniroma3.it.rez3d.model.User;
@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    public Optional<Order> findByUserAndState(User user, OrderState state);
    
}
