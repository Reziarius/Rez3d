package it.uniroma3.it.rez3d.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.it.rez3d.model.Order;
@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    
}
