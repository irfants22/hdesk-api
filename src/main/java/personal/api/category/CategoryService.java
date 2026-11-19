package personal.api.category;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;
import personal.api.category.dto.CategoryDto;
import personal.api.category.dto.CreateCategoryRequestDto;
import personal.api.category.dto.UpdateCategoryRequestDto;
import personal.api.category.dto.UpdateCategoryStatusRequestDto;
import personal.api.util.ValidationUtil;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ValidationUtil validationUtil;

    public CategoryDto createCategory(CreateCategoryRequestDto request) {
        validationUtil.validate(request);

        if (categoryRepository.existsByNameIgnoreCase(request.getName().trim())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Category name already exists");
        }

        Category category = new Category();
        category.setName(request.getName().trim());
        category.setDescription(request.getDescription());
        category.setActive(request.getActive());
        categoryRepository.save(category);

        return CategoryMapper.toCategoryResponse(category);
    }

    public CategoryDto updateCategory(UUID categoryId, UpdateCategoryRequestDto request) {
        validationUtil.validate(request);

        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        if (categoryRepository.existsByNameIgnoreCaseAndIdNot(request.getName().trim(), categoryId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Category name already exists");
        }

        String description = request.getDescription() == null || request.getDescription().isEmpty()
                ? null
                : request.getDescription().trim();

        category.setName(request.getName().trim());
        category.setDescription(description);
        categoryRepository.save(category);

        return CategoryMapper.toCategoryResponse(category);
    }

    public CategoryDto updateCategoryStatus(UUID categoryId, UpdateCategoryStatusRequestDto request) {
        validationUtil.validate(request);

        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        category.setActive(request.getActive());
        categoryRepository.save(category);

        return CategoryMapper.toCategoryResponse(category);
    }

    public CategoryDto getCategoryDetail(UUID categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        return CategoryMapper.toCategoryResponse(category);
    }

    public List<CategoryDto> getCategories(String search) {
        List<Category> categories = search == null || search.isBlank()
                ? categoryRepository.findAllByOrderByNameAsc()
                : categoryRepository.findAllByNameContainingIgnoreCaseOrderByNameAsc(search.trim());

        return categories.stream().map(CategoryMapper::toCategoryResponse).toList();
    }
}
