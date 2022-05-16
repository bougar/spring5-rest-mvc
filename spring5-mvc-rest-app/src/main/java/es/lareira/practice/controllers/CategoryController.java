package es.lareira.practice.controllers;

import es.lareira.practice.api.v1.model.CategoryDTO;
import es.lareira.practice.api.v1.model.CategoryListDTO;
import es.lareira.practice.mappers.CategoryMapper;
import es.lareira.practice.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

  private final CategoryService categoryService;

  private final CategoryMapper categoryMapper;

  public CategoryController(
      final CategoryService categoryService, final CategoryMapper categoryMapper) {
    this.categoryService = categoryService;
    this.categoryMapper = categoryMapper;
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public CategoryListDTO listCategories() {
    final List<CategoryDTO> categoryDTOList =
        this.categoryService.getCategories().stream()
            .map(this.categoryMapper::toDTO)
            .collect(Collectors.toList());
    return new CategoryListDTO(categoryDTOList);
  }
}
