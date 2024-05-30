package project.back.controller.recipe;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.back.dto.recipe.RecipeResponseDto;
import project.back.service.recipe.RecipeCustomService;

@RestController
@RequestMapping("/recipe")
@RequiredArgsConstructor
public class RecipeCustomController {

    private final RecipeCustomService customService;

    @PostMapping("/create")
    public RecipeResponseDto createRecipe(@RequestBody RecipeResponseDto recipeDto) {
        return customService.saveRecipe(recipeDto);
    }
}
