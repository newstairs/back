package project.back.dto.mart;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MartDto {
    private String martName;
    private String martAddress;

    public MartDto(String martName, String martAddress) {
        this.martName = martName;
        this.martAddress = martAddress;
    }
}
