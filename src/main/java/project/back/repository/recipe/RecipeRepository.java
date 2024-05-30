package project.back.repository.recipe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.back.entity.recipe.Recipe;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    @Query("""
           SELECT r.recipeApiNo FROM Recipe r
            """)
    List<Long> findAllRecipeApiNos();
}
