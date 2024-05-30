package project.back.service.recipe;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.back.dto.recipe.RecipeResponseDto;
import project.back.entity.recipe.Recipe;
import project.back.repository.recipe.RecipeRepository;

@Service
@RequiredArgsConstructor
public class RecipeCustomService {

    private final RecipeRepository recipeRepository;
    private final RecipeDataAccessService dataAccessService;

    // TODO. 이미지 업로드 방식 변경하기
    @Transactional
    public RecipeResponseDto saveRecipe(RecipeResponseDto dto) {
        Recipe recipe = Recipe.builder()
                .recipeName(dto.getRecipeName())
                .recipeType(dto.getRecipeType())
                .recipeTip(dto.getRecipeTip())
                .build();
        recipeRepository.save(recipe);
        dataAccessService.saveManuals(dto.getRecipeManualList(), recipe);
        dataAccessService.saveParts(dto.getRecipePartsList(), recipe);

        return RecipeResponseDto.toResponseDto(
                recipe,
                dto.getRecipeManualList(),
                dto.getRecipePartsList());
    }
}