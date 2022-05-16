package es.lareira.practice.service;

import es.lareira.practice.domain.Category;
import es.lareira.practice.repositories.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

  @Mock private CategoryRepository categoryRepository;

  @InjectMocks private CategoryServiceImpl categoryService;

  @Test
  void getCategories() {
    final List<Category> expected =
        List.of(Mockito.mock(Category.class), Mockito.mock(Category.class));
    Mockito.when(this.categoryRepository.findAll()).thenReturn(expected);
    final List<Category> actual = this.categoryService.getCategories();
    Assertions.assertSame(expected, actual);
  }

  @RepeatedTest(3)
  void findCategoryByName() {
    final String name = UUID.randomUUID().toString();
    final Category expected = Mockito.mock(Category.class);
    Mockito.when(this.categoryRepository.findByName(name)).thenReturn(Optional.of(expected));
    final Optional<Category> actual = this.categoryService.findCategoryByName(name);
    Assertions.assertTrue(actual.isPresent());
    Assertions.assertSame(expected, actual.get());
  }

  @RepeatedTest(3)
  void findCategoryByNameWhenNoResultsFound() {
    final String name = UUID.randomUUID().toString();
    Mockito.when(this.categoryRepository.findByName(name)).thenReturn(Optional.empty());
    final Optional<Category> actual = this.categoryService.findCategoryByName(name);
    Assertions.assertTrue(actual.isEmpty());
  }
}
