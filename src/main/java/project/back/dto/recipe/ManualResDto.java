package project.back.dto.recipe;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.back.entity.recipe.RecipeManual;

@Data
@NoArgsConstructor
public class ManualResDto {
    private Long manualId;
    private int step;
    private String manualContent;
    private String manualImgUrl;

    @Builder
    public ManualResDto(Long manualId, int step, String manualContent, String manualImgUrl) {
        this.manualId = manualId;
        this.step = step;
        this.manualContent = manualContent;
        this.manualImgUrl = manualImgUrl;
    }

    public static ManualResDto manualResDto(RecipeManual manual) {
        return ManualResDto.builder()
                .manualId(manual.getManualId())
                .step(Math.toIntExact(manual.getStep()))
                .manualContent(manual.getManualContent())
                .manualImgUrl(manual.getManualImgUrl())
                .build();
    }
}
