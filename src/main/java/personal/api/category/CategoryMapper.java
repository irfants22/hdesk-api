package personal.api.category;

import personal.api.category.dto.CategoryDto;

public class CategoryMapper {

    public static CategoryDto toCategoryResponse(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .active(category.isActive())
                .build();
    }
}
