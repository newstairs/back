package project.back.dto.mart;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MartResDto {
    private Long martId;
    private String martName;
    private String martAddress;

    public MartResDto(Long martId, String martName, String martAddress) {
        this.martId = martId;
        this.martName = martName;
        this.martAddress = martAddress;
    }
}
