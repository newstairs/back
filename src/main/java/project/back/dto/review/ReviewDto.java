package project.back.dto.review;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
public class ReviewDto {
    private Long reviewId;
    private String reviewContent;
    private BigDecimal score;
    private String memberName;
    private Long memberId;
    private Long martId;
    private String reviewTitle;
    @Builder

    public ReviewDto(Long reviewId, String reviewContent, BigDecimal score, String memberName, Long memberId, Long martId, String reviewTitle) {
        this.reviewId = reviewId;
        this.reviewContent = reviewContent;
        this.score = score;
        this.memberName = memberName;
        this.memberId = memberId;
        this.martId = martId;
        this.reviewTitle = reviewTitle;
    }
}