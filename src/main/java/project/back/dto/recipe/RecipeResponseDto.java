package project.back.dto.recipe;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.back.entity.recipe.Recipe;

import java.util.List;

@Data
@NoArgsConstructor
public class RecipeResponseDto {
    private Long memberId;
    private Long recipeId;
    private String recipeName;
    private String recipeType;
    private String recipeTip;
    private List<ManualResDto> recipeManualList;
    private List<RecipePartsDto> recipePartsList;

    @Builder
    public RecipeResponseDto(Long memberId, Long recipeId, String recipeName, String recipeType, String recipeTip, List<ManualResDto> recipeManualList, List<RecipePartsDto> recipePartsList) {
        this.memberId = memberId;
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.recipeType = recipeType;
        this.recipeTip = recipeTip;
        this.recipeManualList = recipeManualList;
        this.recipePartsList = recipePartsList;
    }

    public static RecipeResponseDto toResponseDto(Recipe recipe, List<ManualResDto> manualList, List<RecipePartsDto> partsList) {
        return RecipeResponseDto.builder()
                .recipeId(recipe.getRecipeId())
                .recipeName(recipe.getRecipeName())
                .recipeType(recipe.getRecipeType())
                .recipeTip(recipe.getRecipeTip())
                .recipeManualList(manualList)
                .recipePartsList(partsList)
                .build();
    }

    public static RecipeResponseDto toResponseByMemeberId(Long memberId, Recipe recipe, List<ManualResDto> manualList, List<RecipePartsDto> partsList) {
        return RecipeResponseDto.builder()
                .memberId(memberId)
                .recipeId(recipe.getRecipeId())
                .recipeName(recipe.getRecipeName())
                .recipeType(recipe.getRecipeType())
                .recipeTip(recipe.getRecipeTip())
                .recipeManualList(manualList)
                .recipePartsList(partsList)
                .build();
    }
}
