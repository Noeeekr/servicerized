package com.github.noeeekr.servicerized.product.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.noeeekr.servicerized.product.repository.CategoryRepository;
import com.github.noeeekr.servicerized.product.repository.models.CategoryEntity;
import com.github.noeeekr.servicerized.product.service.request.FindCategoryRequest;
import com.github.noeeekr.servicerized.response.Response;
import com.github.noeeekr.servicerized.response.ResponseBuilder;
import com.github.noeeekr.servicerized.response.failure.Failures;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * findCategory() returns true if the category specified in the request interface exists. Since
     * category name cannot be null, null values are ignored on filter if present.
     * 
     * @return
     */
    public Response<Optional<CategoryEntity>> findCategory(FindCategoryRequest request) {
        ResponseBuilder<Optional<CategoryEntity>> responseBuilder = Response.builder();

        if (request.getCategoryId() == null && request.getCategoryName() == null) {
            return responseBuilder.success(Optional.<CategoryEntity>empty()).build();
        }

        List<CategoryEntity> categories = new ArrayList<>();
        try {
            List<CategoryEntity> fetchedCategories;
            if (request.getCategoryName() == null) {
                fetchedCategories = categoryRepository.findCategory(request.categoryId());
            } else if (request.getCategoryId() == null) {
                fetchedCategories = categoryRepository.findCategory(request.categoryName());
            } else {
                fetchedCategories = categoryRepository.findCategory(request.getCategoryName(),
                        request.getCategoryId());
            }
            categories.addAll(fetchedCategories);
        } catch (Exception e) {
            return responseBuilder.fail(new Failures.UnhandledException(e)).build();
        }

        if (categories.size() == 0) {
            return responseBuilder.success(Optional.empty()).build();
        }

        return responseBuilder.success(Optional.of(categories.get(0))).build();
    }

}
