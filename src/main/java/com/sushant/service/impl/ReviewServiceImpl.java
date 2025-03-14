package com.sushant.service.impl;

import com.sushant.model.Product;
import com.sushant.model.Review;
import com.sushant.model.User;
import com.sushant.repository.ReviewRepo;
import com.sushant.request.CreateReviewRequest;
import com.sushant.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
//@RequiredArgsConstructor

public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepo reviewRepo;

    public ReviewServiceImpl(ReviewRepo reviewRepo) {
        this.reviewRepo = reviewRepo;
    }

    @Override
    public Review createReview(CreateReviewRequest req, User user, Product product) {

        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setReviewText(req.getReviewText());
        review.setRating(req.getReviewRating());
        review.setProductImages(req.getProductImages());

        product.getReviews().add(review);
        return reviewRepo.save(review);


    }

    @Override
    public List<Review> getReviewByProductId(Long productId) {
        return reviewRepo.findByProductId(productId);
    }

    @Override
    public Review updateReview(Long reviewId, String reviewText, double rating, Long userId) throws Exception {


        Review review = getReviewById(reviewId);

        if (review.getUser().getId().equals(userId)) {
            review.setReviewText(reviewText);
            review.setRating(rating);
            return reviewRepo.save(review);
        }
            throw new Exception("you can't update this review");

    }

        @Override
        public void deleteReview (Long reviewId, Long userId) throws Exception {
            Review review = getReviewById(reviewId);
            if (!review.getUser().getId().equals(userId)) {
                throw new Exception("you can't delete this review");
            }
            reviewRepo.delete(review);


        }

        @Override
        public Review getReviewById (Long reviewId) throws Exception {
            return reviewRepo.findById(reviewId).orElseThrow(() -> new Exception("review not found"));
        }
    }
