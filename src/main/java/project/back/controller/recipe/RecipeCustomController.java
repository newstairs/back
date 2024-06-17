package project.back.controller.recipe;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.back.configuration.annotation.LoginUser;
import project.back.dto.ApiResponse;
import project.back.dto.recipe.RecipeRequestDto;
import project.back.dto.recipe.RecipeResponseDto;
import project.back.service.recipe.RecipeCustomService;

import java.util.List;

@RestController
@RequestMapping("/recipe")
@RequiredArgsConstructor
public class RecipeCustomController {

    private final RecipeCustomService customService;

    /**
     * 레시피 관련 정보와 이미지 파일들 생성
     */
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<RecipeResponseDto>> createRecipe(
            @LoginUser Long memberId,
            @RequestParam("recipeData") String recipeData,
            @RequestParam("files") List<MultipartFile> files
    ) throws JsonProcessingException {
        RecipeRequestDto requestDto = new ObjectMapper().readValue(recipeData, RecipeRequestDto.class);
        ApiResponse<RecipeResponseDto> response = customService.createRecipe(memberId, requestDto, files);
        return ResponseEntity.ok(response);
    }

    /**
     * 해당 레시피의 작성자가 레시피 삭제
     */
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteRecipe(
            @LoginUser Long memberId,
            @RequestParam Long recipeId
    ) {
        return ResponseEntity.ok(customService.deleteRecipe(memberId, recipeId));
    }
}
