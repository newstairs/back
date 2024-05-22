package project.back.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Mart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mart_id")
    private Long id;

    @Column(name = "mart_name")
    private String martName;

    @Column(name = "mart_address")
    private String martAddress;

    /* 더미 데이터 삽입에 사용 */
    public Mart(String martName, String martAddress) {
        this.martName = martName;
        this.martAddress = martAddress;
    }
}
