package project.back.dto.recipe;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.back.entity.recipe.Recipe;
import project.back.entity.recipe.RecipeManual;

import java.util.Optional;

@Data
@NoArgsConstructor
public class RecipeListDto {
    private Long recipeId;
    private String recipeName;
    private String recipeType;
    private String manualImgUrl;

    @Builder
    public RecipeListDto(Long recipeId, String recipeName, String recipeType, String manualImgUrl) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.recipeType = recipeType;
        this.manualImgUrl = manualImgUrl;
    }

    public static RecipeListDto recipeResList(Recipe recipe, Optional<RecipeManual> manual) {
        return RecipeListDto.builder()
                .recipeId(recipe.getRecipeId())
                .recipeName(recipe.getRecipeName())
                .recipeType(recipe.getRecipeType())
                .manualImgUrl(manual.map(RecipeManual::getManualImgUrl).orElse(null))
                .build();
    }
}
