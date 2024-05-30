package project.back.repository.recipe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.back.entity.recipe.RecipeParts;

import java.util.List;

@Repository
public interface RecipePartsRepository extends JpaRepository<RecipeParts, Long> {
    List<RecipeParts> findByRecipeRecipeId(Long recipeId);
}
