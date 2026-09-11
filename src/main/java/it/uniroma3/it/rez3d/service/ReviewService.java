package it.uniroma3.it.rez3d.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.it.rez3d.model.PrintFile;
import it.uniroma3.it.rez3d.model.Review;
import it.uniroma3.it.rez3d.model.User;
import it.uniroma3.it.rez3d.repository.OrderRepository;
import it.uniroma3.it.rez3d.repository.ReviewRepository;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;

    public ReviewService(ReviewRepository reviewRepository, OrderRepository orderRepository) {
        this.reviewRepository = reviewRepository;
        this.orderRepository = orderRepository;
    }
    
    public List<Review> getReviewsByFile(PrintFile file) {
        return reviewRepository.findByFile(file);
    }

    @Transactional
    public Review saveReview(Review review, User user, PrintFile file) throws Exception {
        // 1. Verifichiamo che l'utente abbia effettivamente comprato il file
        boolean hasPurchased = orderRepository.hasUserPurchasedFile(user, file);
        if (!hasPurchased) {
            throw new Exception("Non puoi recensire un prodotto che non hai acquistato.");
        }

        // 2. Opzionale: Verifichiamo che non abbia già lasciato una recensione
        if (reviewRepository.existsByAuthorAndFile(user, file)) {
            throw new Exception("Hai già recensito questo prodotto.");
        }

        // 3. Associamo i dati e salviamo
        review.setAuthor(user);
        review.setFile(file);
        
        return reviewRepository.save(review);
    }
}