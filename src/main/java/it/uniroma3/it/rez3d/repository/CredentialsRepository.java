package it.uniroma3.it.rez3d.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.it.rez3d.model.Credentials;




@Repository
public interface CredentialsRepository extends JpaRepository<Credentials,Long>{
    Credentials findByUsername(String username);
}
