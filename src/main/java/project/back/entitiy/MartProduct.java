package project.back.entitiy;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MartProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long martProductId;

    @Column(nullable = false)
    private Long stock;

    @Column(nullable = false)
    private Long price;
}
