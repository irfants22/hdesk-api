package personal.api.category;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;
import personal.api.category.dto.CategoryDto;
import personal.api.category.dto.CreateCategoryRequestDto;
import personal.api.util.ValidationUtil;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ValidationUtil validationUtil;

    public CategoryDto createCategory(CreateCategoryRequestDto request) {
        validationUtil.validate(request);

        if (categoryRepository.findByName(request.getName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category name already exists");
        }

        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setActive(request.getActive());
        categoryRepository.save(category);

        return CategoryMapper.toCategoryResponse(category);
    }
}
