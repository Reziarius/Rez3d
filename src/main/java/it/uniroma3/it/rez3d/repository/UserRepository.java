package it.uniroma3.it.rez3d.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.it.rez3d.model.User;

public interface UserRepository extends CrudRepository<User,Long>{
    
}
