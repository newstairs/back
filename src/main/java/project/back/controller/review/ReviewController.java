package project.back.controller.review;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.back.dto.ApiResponse;
import project.back.dto.review.ReviewDto;
import project.back.entity.review.Review;
import project.back.service.review.ReviewService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    //리뷰 + 평점 조회 get
    @GetMapping("/{martId}")
    public ResponseEntity<ApiResponse<List<ReviewDto>>> getReview(@PathVariable Long martId){
        List<ReviewDto> reviews = reviewService.getReviewByMartId(martId);
        if (reviews.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse<>(false, "No reviews found", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Reviews join successfully", reviews));
    }

    //리뷰 + 평점 작성 post
    @PostMapping("/")
    public ResponseEntity<ApiResponse<Review>> writeReview(@RequestBody ReviewDto reviewDto){
        Review review = reviewService.writeReview(reviewDto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Review created successfully", review));
    }

    //리뷰 + 평점 삭제 delete
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(@PathVariable Long reviewId){
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok(new ApiResponse<>(true, "삭제 완료했습니다.", null));
    }

    //리뷰 + 평점 수정 patch
    @PatchMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewDto>> updateReview(@PathVariable Long reviewId, @RequestBody ReviewDto reviewDto) {
        ReviewDto updatedReview = reviewService.updateReview(reviewId, reviewDto.getReviewContent(), reviewDto.getScore());
        return ResponseEntity.ok(new ApiResponse<>(true, "Review updated successfully", updatedReview));
    }
}
