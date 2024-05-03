package project.back.entitiy;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Entity
@Data
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long member_id;


    @Column(name="email")
    private String email;


    @Column(name="name")
    private String name;

    public Member(String email, String name) {
        this.email = email;
        this.name = name;
    }
}
