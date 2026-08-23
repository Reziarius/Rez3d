package it.uniroma3.it.rez3d.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.it.rez3d.model.User;
@Repository
public interface UserRepository extends JpaRepository<User,Long>{
    
}
