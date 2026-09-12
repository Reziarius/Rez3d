package it.uniroma3.it.rez3d.controller;

import org.springframework.web.bind.annotation.RestController;

import it.uniroma3.it.rez3d.model.PrintFile;
import it.uniroma3.it.rez3d.model.Review;
import it.uniroma3.it.rez3d.model.User;
import it.uniroma3.it.rez3d.service.PrintFileService;
import it.uniroma3.it.rez3d.service.ReviewService;
import it.uniroma3.it.rez3d.service.UserService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;




@RestController 
@RequestMapping("/api/files/{fileId}/reviews")
public class ReviewRestController {
    private final ReviewService reviewService;
    private final PrintFileService printFileService;
    private final UserService userService;

    public ReviewRestController(ReviewService reviewService, PrintFileService printFileService, UserService userService) {
        this.reviewService = reviewService;
        this.printFileService = printFileService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getReviews(@PathVariable("fileId") Long fileId) {
        PrintFile file = printFileService.findById(fileId).orElse(null);
        if(file==null){
            return ResponseEntity.notFound().build();
        }

        List<Review> reviews = reviewService.getReviewsByFile(file);

        //un DTO è tipo una scatola vuota in cui inseriamo solo ciò che vogliamo noi
        List<ReviewDTO> response = reviews.stream()
            .map(r -> new ReviewDTO(r.getId(),r.getTitle(),r.getText(),r.getRating(),r.getAuthor().getUsername()))
            .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> addReview(@PathVariable("fileId") Long fileId,@Valid @RequestBody Review newReview, Principal principal) {
        //TODO: process POST request
        if(principal == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Devi effettuare il login per recensire");
        }
        PrintFile file = printFileService.findById(fileId).orElse(null);
        User user = userService.findByUsername(principal.getName());

        if(file==null || user==null){
            return ResponseEntity.badRequest().body("Dati non validi.");
        }
        try{
            Review savedReview = reviewService.saveReview(newReview, user, file);
            ReviewDTO response = new ReviewDTO(savedReview.getId(), savedReview.getTitle(), savedReview.getText(), savedReview.getRating(), user.getUsername());
            return ResponseEntity.ok(response);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
    


    public static class ReviewDTO {
        public Long id;
        public String title;
        public String text;
        public Integer rating;
        public String authorName;

        public ReviewDTO(Long id, String title, String text, Integer rating, String authorName) {
            this.id = id;
            this.title = title;
            this.text = text;
            this.rating = rating;
            this.authorName = authorName;
        }
    }
    
}
