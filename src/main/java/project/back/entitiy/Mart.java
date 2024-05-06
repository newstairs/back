package project.back.entitiy;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Mart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mart_id")
    private Long martId;

    @Column(name = "mart_name")
    private String martName;

    @Column(name = "mart_address")
    private String martAddress;
}
