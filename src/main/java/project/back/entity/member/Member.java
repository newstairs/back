package project.back.entity.member;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.back.entity.UpdateCreateTime;

@Entity
@Data
@NoArgsConstructor
public class Member extends UpdateCreateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;


    @Column(name="email")
    private String email;


    @Column(name="name",nullable = true)
    private String name;


    @Column(name="password",nullable = true)
    private String password;


    @Column(name="address")
    private String address;

    public Member(String email, String password) {
        this.email = email;
        this.password = password;

    }

}
