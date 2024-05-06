package project.back.entitiy;

import jakarta.persistence.*;
import lombok.Data;
import org.yaml.snakeyaml.tokens.FlowEntryToken;

import java.math.BigDecimal;

@Entity
@Data
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId;

    @Column(name = "review_content")
    private String reviewContent;

    @Column(name = "score", precision = 2, scale = 1, nullable = false)
    private BigDecimal score;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="mart_id")
    private Mart mart;




}
