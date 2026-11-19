package personal.api.category;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import personal.api.category.dto.CategoryDto;
import personal.api.category.dto.CreateCategoryRequestDto;
import personal.api.category.dto.UpdateCategoryRequestDto;
import personal.api.category.dto.UpdateCategoryStatusRequestDto;
import personal.api.common.response.ApiResponse;

import java.util.List;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{categoryId}/status")
    public ApiResponse<CategoryDto> updateCategoryStatus(@PathVariable UUID categoryId,
            @RequestBody @Valid UpdateCategoryStatusRequestDto request) {
        CategoryDto result = categoryService.updateCategoryStatus(categoryId, request);
        return ApiResponse.ok(result);
    }

    @GetMapping("/{categoryId}")
    public ApiResponse<CategoryDto> getCategoryDetail(
            @PathVariable UUID categoryId) {
        CategoryDto result = categoryService.getCategoryDetail(categoryId);
        return ApiResponse.ok(result);
    }

    @GetMapping()
    public ApiResponse<List<CategoryDto>> getCategories(
            @RequestParam(required = false) String search) {
        List<CategoryDto> result = categoryService.getCategories(search);
        return ApiResponse.ok(result);
    }

}
