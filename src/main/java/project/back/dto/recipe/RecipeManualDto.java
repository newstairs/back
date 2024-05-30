package project.back.dto.recipe;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.back.entity.recipe.RecipeManual;

@Data
@NoArgsConstructor
public class RecipeManualDto {
    private int step;
    private String manualContent;
    private String manualImgUrl;

    @Builder
    public RecipeManualDto(int step, String manualContent, String manualImgUrl) {
        this.step = step;
        this.manualContent = manualContent;
        this.manualImgUrl = manualImgUrl;
    }

    public static RecipeManualDto manualDto(RecipeManual manual) {
        return RecipeManualDto.builder()
                .step(Math.toIntExact(manual.getStep()))
                .manualContent(manual.getManualContent())
                .manualImgUrl(manual.getManualImgUrl())
                .build();
    }
}
