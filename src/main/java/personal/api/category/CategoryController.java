package personal.api.category;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import personal.api.category.dto.CategoryDto;
import personal.api.category.dto.CreateCategoryRequestDto;
import personal.api.category.dto.UpdateCategoryRequestDto;
import personal.api.common.response.ApiResponse;

import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ApiResponse<CategoryDto> createCategory(@RequestBody @Valid CreateCategoryRequestDto request) {
        CategoryDto result = categoryService.createCategory(request);
        return ApiResponse.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{categoryId}")
    public ApiResponse<CategoryDto> updateCategory(@PathVariable UUID categoryId,
            @RequestBody UpdateCategoryRequestDto request) {
        CategoryDto result = categoryService.updateCategory(categoryId, request);
        return ApiResponse.ok(result);
    }
}
