package project.back.repository.recipe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.back.entity.recipe.RecipeManual;

import java.util.List;

@Repository
public interface RecipeManualRepository extends JpaRepository<RecipeManual, Long> {
    List<RecipeManual> findByRecipeRecipeId(Long recipeId);
}
