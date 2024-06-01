package project.back.controller.recipe;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.back.dto.recipe.RecipeRequestDto;
import project.back.dto.recipe.RecipeResponseDto;
import project.back.service.recipe.RecipeCustomService;

import java.util.List;

@RestController
@RequestMapping("/recipe")
@RequiredArgsConstructor
public class RecipeCustomController {

    private final RecipeCustomService customService;

    @PostMapping("/create")
    public ResponseEntity<RecipeResponseDto> createRecipe(
            @RequestParam("recipeData") String recipeData,
            @RequestParam("files") List<MultipartFile> files
    ) throws JsonProcessingException {
        RecipeRequestDto requestDto = new ObjectMapper().readValue(recipeData, RecipeRequestDto.class);
        RecipeResponseDto response = customService.saveRecipe(requestDto, files);
        return ResponseEntity.ok(response);
    }
}
