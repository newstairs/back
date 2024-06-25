package project.back.dto.member;

import lombok.Data;

@Data
public class MemberDto {

    private String email;
    private String password;
    private String name;
    public MemberDto(String email, String password,String name) {
        this.email = email;
        this.password = password;
        this.name=name;

    }
}
