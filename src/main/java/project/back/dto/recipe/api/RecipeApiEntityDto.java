package project.back.dto.recipe.api;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.back.dto.recipe.RecipeManualDto;
import project.back.dto.recipe.RecipePartsDto;
import project.back.entity.recipe.Recipe;


import java.util.List;

@Data
@NoArgsConstructor
public class RecipeApiEntityDto {
    private String recipeName;
    private String recipeType;
    private String recipeTip;
    private List<RecipeManualDto> recipeManualList;
    private List<RecipePartsDto> recipePartsList;

    @Builder
    public RecipeApiEntityDto(String recipeName, String recipeType, String recipeTip, List<RecipeManualDto> recipeManualList, List<RecipePartsDto> recipePartsList) {
        this.recipeName = recipeName;
        this.recipeType = recipeType;
        this.recipeTip = recipeTip;
        this.recipeManualList = recipeManualList;
        this.recipePartsList = recipePartsList;
    }

    public static RecipeApiEntityDto toEntityDto(Recipe recipe, List<RecipeManualDto> manualList, List<RecipePartsDto> partsList) {
        return RecipeApiEntityDto.builder()
                .recipeName(recipe.getRecipeName())
                .recipeType(recipe.getRecipeType())
                .recipeTip(recipe.getRecipeTip())
                .recipeManualList(manualList)
                .recipePartsList(partsList)
                .build();
    }
}
