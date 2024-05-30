package project.back.dto.recipe;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.back.entity.recipe.RecipeParts;

@Data
@NoArgsConstructor
public class RecipePartsDto {
    private String partsName;
    private String partsQuantity;

    @Builder
    public RecipePartsDto(String partsName, String partsQuantity) {
        this.partsName = partsName;
        this.partsQuantity = partsQuantity;
    }

    public static RecipePartsDto partsDto(RecipeParts parts) {
        return RecipePartsDto.builder()
                .partsName(parts.getPartsName())
                .partsQuantity(parts.getPartsQuantity())
                .build();
    }

    @Override
    public String toString() {
        return partsName;
    }
}
