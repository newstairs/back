package project.back.service.recipe;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.back.configuration.annotation.LoginUser;
import project.back.dto.ApiResponse;
import project.back.dto.recipe.ManualResDto;
import project.back.dto.recipe.RecipePartsDto;
import project.back.dto.recipe.RecipeRequestDto;
import project.back.dto.recipe.RecipeResponseDto;
import project.back.entity.member.Member;
import project.back.entity.recipe.Recipe;
import project.back.entity.recipe.RecipeManual;
import project.back.entity.recipe.RecipeParts;
import project.back.etc.recipe.RecipeMessage;
import project.back.repository.member.MemberRepository;
import project.back.repository.recipe.RecipeManualRepository;
import project.back.repository.recipe.RecipePartsRepository;
import project.back.repository.recipe.RecipeRepository;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeCustomService {

    private final MemberRepository memberRepository;
    private final RecipeRepository recipeRepository;
    private final RecipeManualRepository manualRepository;
    private final RecipePartsRepository partsRepository;
    private final RecipeFileService fileService;

    /**
     * 레시피 관련 정보와 이미지 파일들 생성
     *
     * @param requestDto 저장할 정보가 담긴 DTO
     * @param files      첨부할 이미지 파일 목록
     * @return 저장된 레시피 정보 반환
     * @throws RuntimeException        이미지 파일 처리 중 오류 발생
     * @throws EntityNotFoundException 레시피를 찾을 수 없을 때 발생
     */
    @Transactional
    public ApiResponse<RecipeResponseDto> createRecipe(
            @LoginUser Long memberId, RecipeRequestDto requestDto, List<MultipartFile> files
    ) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException(RecipeMessage.NOT_FOUND_MEMBER.getMessage()));
        Recipe recipe = Recipe.builder()
                .recipeName(requestDto.getRecipeName())
                .recipeType(requestDto.getRecipeType())
                .recipeTip(requestDto.getRecipeTip())
                .member(member)
                .build();
        recipeRepository.save(recipe);

        createManuals(recipe, requestDto.getRecipeManualList(), files);
        createParts(recipe, requestDto.getRecipePartsList());

        return ApiResponse.success(
                RecipeResponseDto.toResponseByMemeberId(
                        memberId, recipe, requestDto.getRecipeManualList(), requestDto.getRecipePartsList()),
                RecipeMessage.LOADED_RECIPE.getMessage()
        );
    }

    /**
     * 레시피 작성자 확인 후 레시피 삭제
     *
     * @param memberId 해당 레시피 작성자 고유번호
     * @param recipeId 해당 레시피 고유번호
     * @return 성공했다는 메시지 반환
     */
    public ApiResponse deleteRecipe(Long memberId, Long recipeId) {
        Recipe findRecipe = verifyRecipeAuthor(memberId, recipeId);
        recipeRepository.delete(findRecipe);
        return ApiResponse.success(null, RecipeMessage.SUCCESS_DELETE.getMessage());
    }

    /**
     * 해당 레시피의 작성자가 맞는지 확인
     */
    private Recipe verifyRecipeAuthor(Long memberId, Long recipeId) {
        Recipe recipe = getRecipe(recipeId);
        if (!recipe.getMember().getMemberId().equals(memberId)) {
            throw new AccessDeniedException(RecipeMessage.NO_ACCESS_TO_RECIPE.getMessage());
        }
        return recipe;
    }

    /**
     * 레시피 메뉴얼과 이미지 파일을 목록으로 생성하고 저장
     */
    private void createManuals(
            Recipe recipe, List<ManualResDto> manualDtos, List<MultipartFile> files
    ) {
        for (int i = 0; i < manualDtos.size(); i++) {
            ManualResDto manualDto = manualDtos.get(i);
            MultipartFile file = (i < files.size()) ? files.get(i) : null;
            try {
                Recipe findRecipe = getRecipe(recipe.getRecipeId());

                String fileUrl = fileService.saveFile(recipe.getRecipeId(), (long) manualDto.getStep(), file);

                RecipeManual manual = RecipeManual.builder()
                        .step((long) manualDto.getStep())
                        .manualContent(manualDto.getManualContent())
                        .manualImgUrl(fileUrl)
                        .recipe(findRecipe)
                        .build();
                manualRepository.save(manual);

            } catch (IOException e) {
                throw new RuntimeException(
                        RecipeMessage.ERROR_RECIPE_IMG_PROCESSING.getMessage(), e);
            }
        }
    }

    /**
     * 해당 레시피 찾아서 반환
     */
    private Recipe getRecipe(Long recipeId) {
        return recipeRepository.findById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException(RecipeMessage.NOT_FOUND_RECIPE.getMessage()));
    }

    /**
     * 레시피 제료 목록을 생성하고 저장
     */
    private void createParts(Recipe savedRecipe, List<RecipePartsDto> parts) {
        parts.forEach(part -> {
            RecipeParts recipePart = RecipeParts.builder()
                    .partsName(String.valueOf(part))
                    .partsQuantity(part.getPartsQuantity())
                    .recipe(savedRecipe)
                    .build();
            partsRepository.save(recipePart);
        });
    }
}
