package project.back.service.recipe;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.back.dto.recipe.RecipeManualDto;
import project.back.dto.recipe.RecipePartsDto;
import project.back.entity.recipe.Recipe;
import project.back.entity.recipe.RecipeManual;
import project.back.entity.recipe.RecipeParts;
import project.back.repository.recipe.RecipeManualRepository;
import project.back.repository.recipe.RecipePartsRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeDataAccessService {

    private final RecipeManualRepository manualRepository;
    private final RecipePartsRepository partsRepository;

    public void saveManuals(List<RecipeManualDto> manuals, Recipe savedRecipe) {
        manuals.forEach(manualDto -> {
            RecipeManual manual = RecipeManual.builder()
                    .step((long) manualDto.getStep())
                    .manualContent(manualDto.getManualContent())
                    .manualImgUrl(manualDto.getManualImgUrl())
                    .recipe(savedRecipe)
                    .build();
            manualRepository.save(manual);
        });
    }

    public void saveParts(List<RecipePartsDto> parts, Recipe savedRecipe) {
        parts.forEach(part -> {
            RecipeParts recipePart = RecipeParts.builder()
                    .partsName(String.valueOf(part))
                    .partsQuantity(part.getPartsQuantity())
                    .recipe(savedRecipe)
                    .build();
            partsRepository.save(recipePart);
        });
    }

    public List<RecipeManualDto> findManualsByRecipe(Recipe recipe) {
        List<RecipeManual> manuals = manualRepository.findByRecipeRecipeId(recipe.getRecipeId());
        return manuals.stream()
                .map(manual -> new RecipeManualDto(
                        manual.getStep().intValue(),
                        manual.getManualContent(),
                        manual.getManualImgUrl()))
                .toList();
    }

    public List<RecipePartsDto> findPartsByRecipe(Recipe recipe) {
        List<RecipeParts> parts = partsRepository.findByRecipeRecipeId(recipe.getRecipeId());
        return parts.stream()
                .map(part -> new RecipePartsDto(
                        part.getPartsName(),
                        part.getPartsQuantity()))
                .toList();
    }
}