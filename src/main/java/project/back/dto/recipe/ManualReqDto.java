package project.back.dto.recipe;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.back.entity.recipe.RecipeManual;

@Data
@NoArgsConstructor
public class ManualReqDto {
    private int step;
    private String manualContent;
    private String manualImgUrl;

    @Builder
    public ManualReqDto(int step, String manualContent, String manualImgUrl) {
        this.step = step;
        this.manualContent = manualContent;
        this.manualImgUrl = manualImgUrl;
    }

    public static ManualReqDto manualReqDto(RecipeManual manual) {
        return ManualReqDto.builder()
                .step(Math.toIntExact(manual.getStep()))
                .manualContent(manual.getManualContent())
                .manualImgUrl(manual.getManualImgUrl())
                .build();
    }
}
