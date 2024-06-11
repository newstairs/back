package project.back.dto.mart;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MartResDto {
    private Long martId;
    private String martName;
    private String martAddress;

    @Builder
    public MartResDto(Long martId, String martName, String martAddress) {
        this.martId = martId;
        this.martName = martName;
        this.martAddress = martAddress;
    }

    public static MartResDto resDto(Long martId, String martName, String martAddress) {
        return MartResDto.builder()
                .martId(martId)
                .martName(martName)
                .martAddress(martAddress)
                .build();
    }
}
