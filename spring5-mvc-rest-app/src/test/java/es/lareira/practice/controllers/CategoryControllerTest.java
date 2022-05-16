package es.lareira.practice.controllers;

import es.lareira.practice.domain.Category;
import es.lareira.practice.mappers.CategoryMapperImpl;
import es.lareira.practice.service.CategoryService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

  @Mock private CategoryService categoryService;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    final CategoryController categoryController =
        new CategoryController(this.categoryService, new CategoryMapperImpl());
    this.mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
  }

  @SneakyThrows
  @Test
  void listCategories() {
    final Category category1 = new Category();
    category1.setName("cat1");
    category1.setId(1L);
    final Category category2 = new Category();
    category2.setName("cat2");
    category2.setId(2L);

    when(this.categoryService.getCategories()).thenReturn(List.of(category1, category2));
    this.mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/v1/categories").accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.categories", hasSize(2)));
  }
}
