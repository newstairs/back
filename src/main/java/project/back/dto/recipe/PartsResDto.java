package project.back.dto.recipe;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.back.entity.recipe.RecipeParts;

@Data
@NoArgsConstructor
public class PartsResDto {
    private Long partsId;
    private String partsName;
    private String partsQuantity;

    @Builder
    public PartsResDto(Long partsId, String partsName, String partsQuantity) {
        this.partsId = partsId;
        this.partsName = partsName;
        this.partsQuantity = partsQuantity;
    }

    public static PartsResDto partsResDto(RecipeParts parts) {
        return PartsResDto.builder()
                .partsId(parts.getPartsId())
                .partsName(parts.getPartsName())
                .partsQuantity(parts.getPartsQuantity())
                .build();
    }
}
