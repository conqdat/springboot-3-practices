package com.pokemonreview.api.repository;

import com.pokemonreview.api.models.Review;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
// This annotation tells Spring Boot to look for an embedded database configuration and to configure JPA for us.
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2) // This annotation tells Spring Boot to use H2 as the embedded database.
public class ReviewRepositoryTest {

    private ReviewRepository reviewRepository;

    @Autowired
    public ReviewRepositoryTest(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Test
    public void ReviewRepository_SaveAll_ReturnSavedReview() {
        // Arrange
        Review review = Review.builder()
                .title("Pikachu")
                .content("Electric")
                .stars(5)
                .build();

        // Act
        Review savedReview = reviewRepository.save(review);

        // Assert
        Assertions.assertEquals(review.getTitle(), savedReview.getTitle());
        Assertions.assertEquals(review.getContent(), savedReview.getContent());
        Assertions.assertEquals(review.getStars(), savedReview.getStars());
    }

    @Test
    public void ReviewRepository_GetAll_ReturnAllReview() {
        // Arrange
        Review review1 = Review.builder()
                .title("Pikachu")
                .content("Electric")
                .stars(5)
                .build();

        Review review2 = Review.builder()
                .title("Charmander")
                .content("Fire")
                .stars(4)
                .build();

        reviewRepository.save(review1);
        reviewRepository.save(review2);

        // Act
        List<Review> list = reviewRepository.findAll();

        // Assert
        Assertions.assertEquals(2, list.size());
    }
}
