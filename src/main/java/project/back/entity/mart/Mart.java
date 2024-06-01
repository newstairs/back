package project.back.entity.mart;

import jakarta.persistence.*;
import lombok.*;
import project.back.entity.product.JoinMart;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mart_id")
    private Long id;

    @Column(name = "mart_name", nullable = false)
    private String martName;

    @Column(name = "mart_address", nullable = false)
    private String martAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "join_id")
    private JoinMart joinMart;
}
