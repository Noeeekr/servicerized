package com.github.noeeekr.servicerized.product.service.category;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.noeeekr.servicerized.product.repository.CategoryRepository;
import com.github.noeeekr.servicerized.product.repository.models.entities.CategoryEntity;
import com.github.noeeekr.servicerized.product.service.category.models.command.ListCategoryCommand;
import com.github.noeeekr.servicerized.response.Response;
import com.github.noeeekr.servicerized.response.failure.Failures;
import jakarta.transaction.Transactional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * findCategory() returns true if the category specified in the request interface exists. Since
     * category name cannot be null, null values are ignored on filter if present.
     * 
     * @return
     */
    @Transactional
    public Response<List<CategoryEntity>> listCategories(ListCategoryCommand request) {
        List<CategoryEntity> categories = new ArrayList<>();
        try {
            Response<List<CategoryEntity>> response;

            if (!request.categoryIdExists() && !request.categoryNameExists()) {
                response = categoryRepository.findCategories();
            } else if (request.categoryNameExists() && request.categoryIdExists()) {
                response = categoryRepository.findCategory(request.getCategoryName(),
                        request.getCategoryId());
            } else if (request.categoryNameExists()) {
                response = categoryRepository.findCategories(request.categoryName());
            } else {
                response = categoryRepository.findCategory(request.categoryId());
            }

            if (!response.isSuccess())
                return response;
            categories.addAll(response.getPayload());
        } catch (Exception e) {
            return Response.fromFailure(new Failures.UnhandledException(e));
        }

        return Response.success(categories);
    }

}
