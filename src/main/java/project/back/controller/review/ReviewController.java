package project.back.controller.review;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.back.dto.review.ReviewDto;
import project.back.entity.review.Review;
import project.back.service.review.ReviewService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    //리뷰  + 평점 조회 get
    @GetMapping("/{martId}")
    public ResponseEntity<List<ReviewDto>> getReview(@PathVariable Long martId){
        List<ReviewDto> reviews = reviewService.getReviewByMartId(martId);
        if (reviews.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reviews);
    }

    //리뷰 + 평점 작성 post
    @PostMapping("/")
    public ResponseEntity<Review> writeReview(@RequestBody ReviewDto reviewDto){
        Review review = reviewService.writeReview(reviewDto);
        return ResponseEntity.ok(review);
    }

    //리뷰 + 평점 삭제 delete
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId){
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok("삭제 완료했습니다.");
    }

    //리뷰 + 평점 수정 patch
    @PatchMapping("/{reviewId}")
    public ResponseEntity<ReviewDto> updateReview( @PathVariable Long reviewId,@RequestBody ReviewDto reviewDto) {
        ReviewDto updatedReview = reviewService.updateReview(reviewId, reviewDto.getReviewContent(), reviewDto.getScore());
        return ResponseEntity.ok(updatedReview);
    }
}
