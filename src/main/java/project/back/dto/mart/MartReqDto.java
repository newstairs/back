package project.back.dto.mart;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MartReqDto {
    private String martName;
    private String martAddress;

    public MartReqDto(String martName, String martAddress) {
        this.martName = martName;
        this.martAddress = martAddress;
    }
}
