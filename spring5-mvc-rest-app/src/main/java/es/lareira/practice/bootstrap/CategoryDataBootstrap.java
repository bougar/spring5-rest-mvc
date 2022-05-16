package es.lareira.practice.bootstrap;

import es.lareira.practice.domain.Category;
import es.lareira.practice.repositories.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CategoryDataBootstrap implements CommandLineRunner {

  private final CategoryRepository categoryRepository;

  public CategoryDataBootstrap(final CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @Override
  public void run(final String... args) throws Exception {
    final Category fruits = new Category();
    fruits.setName("Fruits");

    final Category exotic = new Category();
    exotic.setName("Exotic");

    final Category nuts = new Category();
    nuts.setName("Nuts");
    this.categoryRepository.save(exotic);
    this.categoryRepository.save(nuts);
    this.categoryRepository.save(fruits);
  }
}
