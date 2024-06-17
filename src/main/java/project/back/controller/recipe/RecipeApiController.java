package project.back.controller.recipe;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.back.dto.recipe.api.RecipeApiEntityDto;
import project.back.service.recipe.RecipeApiService;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/recipe")
@RequiredArgsConstructor
public class RecipeApiController {

    private final RecipeApiService recipeService;

    // TODO. End Point 설정 관련 수정

    /**
     * 메뉴 이름으로 가져온 레시피를 저장
     */
    @GetMapping("/menu")
    public Mono<List<RecipeApiEntityDto>> fetchAndSaveRecipes(@RequestParam("menu") String menu) {
        return recipeService.fetchAndSaveRecipesByMenu(menu);
    }

    /**
     * 메뉴 종류로 가져온 레시피를 저장
     */
    @GetMapping("/type")
    public Mono<List<RecipeApiEntityDto>> fetchAndSaveRecipesByType(@RequestParam("type") String type) {
        return recipeService.fetchAndSaveRecipesByType(type);
    }
}
