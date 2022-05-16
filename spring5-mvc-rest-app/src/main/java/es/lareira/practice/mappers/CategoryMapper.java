package es.lareira.practice.mappers;

import es.lareira.practice.api.v1.model.CategoryDTO;
import es.lareira.practice.domain.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
  CategoryDTO toDTO(Category category);
}
