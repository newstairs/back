package project.back.service.review;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.back.dto.review.ReviewDto;
import project.back.entity.mart.Mart;
import project.back.entity.member.Member;
import project.back.entity.review.Review;
import project.back.repository.mart.MartRepository;
import project.back.repository.member.MemberRepository;
import project.back.repository.review.ReviewRepository;

import java.math.BigDecimal;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final MartRepository martRepository;

    //review 조회 서비스 + page처리
    public Page<ReviewDto> getReviewByMartId(Long martId, Pageable pageable) {
        Page<Review> reviews = reviewRepository.findByMart_Id(martId, pageable);
        return reviews.map(review -> ReviewDto.builder()
                .reviewId(review.getReviewId())
                .reviewContent(review.getReviewContent())
                .memberName(review.getMember().getName())
                .score(review.getScore())
                .reviewTitle(review.getReviewTitle())
                .build());
    }

    //review + 평점 작성하기 서비스
    public Review writeReview(ReviewDto reviewDto, Long memberId) {
        // 필수 필드 검증
        if (reviewDto.getMartId() == null || reviewDto.getReviewContent() == null || reviewDto.getScore() == null || reviewDto.getReviewTitle() == null) {
            throw new IllegalArgumentException("요청 필드가 누락되었습니다.");
        }
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found "));

        Mart mart = martRepository.findById(reviewDto.getMartId())
                .orElseThrow(() -> new EntityNotFoundException("Mart not found "));

        Review review = Review.builder()
                .reviewContent(reviewDto.getReviewContent())
                .score(reviewDto.getScore())
                .member(member)
                .mart(mart)
                .reviewTitle(reviewDto.getReviewTitle())
                .build();

        return reviewRepository.save(review);
    }

    //리뷰 + 평점 삭제하기 서비스
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));
        reviewRepository.delete(review);
    }

    //리뷰 + 평점 수정하기 서비스
    public ReviewDto updateReview(Long reviewId, String content, BigDecimal score, String reviewTitle) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("review not find"));

        Review updatedReview = Review.builder()
                .reviewId(reviewId)
                .reviewContent(content != null ? content : review.getReviewContent())
                .score(score != null ? score : review.getScore())
                .member(review.getMember())
                .mart(review.getMart())
                .reviewTitle(reviewTitle)
                .build();

        Review savedReview = reviewRepository.save(updatedReview);
        return ReviewDto.builder()
                .score(savedReview.getScore())
                .reviewContent(savedReview.getReviewContent())
                .reviewTitle(savedReview.getReviewTitle())
                .build();
    }
}
