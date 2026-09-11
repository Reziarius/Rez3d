package it.uniroma3.it.rez3d.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import it.uniroma3.it.rez3d.model.Review;
import it.uniroma3.it.rez3d.model.User;
import it.uniroma3.it.rez3d.model.PrintFile;

public interface ReviewRepository extends CrudRepository<Review, Long> {
    
    // Recupera tutte le recensioni associate a un determinato file 3D
    List<Review> findByFile(PrintFile file);
    
    // Utile per verificare se un utente ha GIÀ recensito questo file (per evitare spam)
    boolean existsByAuthorAndFile(User author, PrintFile file);
}