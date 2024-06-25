package project.back.dto.member;


import lombok.Data;

@Data
public class MemberDtoOriginLogin {


    private String password;
    private String email;


    public MemberDtoOriginLogin(String password, String email) {
        this.password = password;
        this.email =email;
    }
}
