package project.back.dto.member;

import lombok.Data;

@Data
public class MemberDto2 {

    private String email;
    private String name;

    public MemberDto2(String email, String name) {
        this.email = email;
        this.name = name;

    }
}
