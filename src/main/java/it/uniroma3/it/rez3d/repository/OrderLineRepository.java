package it.uniroma3.it.rez3d.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.it.rez3d.model.OrderLine;
@Repository
public interface OrderLineRepository extends CrudRepository<OrderLine,Long>{
    
}
