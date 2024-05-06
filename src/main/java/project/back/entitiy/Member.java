package project.back.entitiy;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Entity
@Data
public class Member extends UpdateCreateTime{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long member_id;


    @Column(name="email")
    private String email;


    @Column(name="name")
    private String name;


    @Column(name="address")
    private String address;

    public Member(String email, String name) {
        this.email = email;
        this.name = name;
    }
}
