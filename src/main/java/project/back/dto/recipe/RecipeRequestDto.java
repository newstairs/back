package project.back.dto.recipe;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RecipeRequestDto {
    private String recipeName;
    private String recipeType;
    private String recipeTip;
    private List<ManualResDto> recipeManualList;
    private List<RecipePartsDto> recipePartsList;

    @Builder
    public RecipeRequestDto(String recipeName, String recipeType, String recipeTip, List<ManualResDto> recipeManualList, List<RecipePartsDto> recipePartsList) {
        this.recipeName = recipeName;
        this.recipeType = recipeType;
        this.recipeTip = recipeTip;
        this.recipeManualList = recipeManualList;
        this.recipePartsList = recipePartsList;
    }
}
