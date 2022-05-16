package es.lareira.practice.service;

import es.lareira.practice.domain.Category;
import es.lareira.practice.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
  private final CategoryRepository categoryRepository;

  public CategoryServiceImpl(final CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @Override
  public List<Category> getCategories() {
    return this.categoryRepository.findAll();
  }

  @Override
  public Optional<Category> findCategoryByName(final String name) {
    return this.categoryRepository.findByName(name);
  }
}
