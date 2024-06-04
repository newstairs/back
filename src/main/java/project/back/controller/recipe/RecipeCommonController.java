package project.back.controller.recipe;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.back.dto.ApiResponse;
import project.back.dto.recipe.RecipeListDto;
import project.back.dto.recipe.RecipeResponseDto;
import project.back.service.recipe.RecipeCommonService;

import java.util.List;

@RestController
@RequestMapping("/recipe")
@RequiredArgsConstructor
public class RecipeCommonController {

    private final RecipeCommonService commonService;

    /**
     * 레시피 페이징 처리해서 가져오기
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<RecipeListDto>>> getAllRecipe(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        ApiResponse<List<RecipeListDto>> recipeList = commonService.getAllRecipe(page, size);
        return ResponseEntity.ok(recipeList);
    }

    /**
     * 해당 레시피 정보 가져오기
     */
    @GetMapping("/{recipeId}")
    public ResponseEntity<ApiResponse<RecipeResponseDto>> getRecipeId(
            @PathVariable("recipeId") Long recipeId
    ) {
        ApiResponse<RecipeResponseDto> recipe = commonService.getRecipeId(recipeId);
        return ResponseEntity.ok(recipe);
    }
}
