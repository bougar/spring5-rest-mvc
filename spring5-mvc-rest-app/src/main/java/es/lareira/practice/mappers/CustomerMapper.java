package es.lareira.practice.mappers;

import es.lareira.practice.domain.Customer;
import es.lareira.practice.model.CustomerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

  public static final String CUSTOMER_PREFIX = "/api/v1/customers/";

  @Mapping(target = "customerUrl", qualifiedByName = "getCustomerUrl", source = "id")
  CustomerDTO toDTO(Customer customer);

  @Named(value = "getCustomerUrl")
  default String getCustomerUrl(final Long id) {
    if (id == null) {
      return null;
    }
    return CUSTOMER_PREFIX + id;
  }

  Customer toDomain(CustomerDTO customerDTO);
}
