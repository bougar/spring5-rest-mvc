package es.lareira.practice.service;

import es.lareira.practice.domain.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

  List<Category> getCategories();

  Optional<Category> findCategoryByName(String name);
}
