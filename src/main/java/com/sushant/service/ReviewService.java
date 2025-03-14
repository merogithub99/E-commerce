package com.sushant.service;

import com.sushant.model.Product;
import com.sushant.model.Review;
import com.sushant.model.User;
import com.sushant.request.CreateReviewRequest;

import java.util.List;

public interface ReviewService {
    Review createReview(CreateReviewRequest req,
                        User user,
                        Product product);

    List<Review> getReviewByProductId(Long productId);

    Review updateReview(Long reviewId,String reviewText, double rating,Long userId) throws Exception;
    void deleteReview(Long reviewId,Long userId) throws Exception;

    Review getReviewById(Long reviewId) throws Exception;

}
