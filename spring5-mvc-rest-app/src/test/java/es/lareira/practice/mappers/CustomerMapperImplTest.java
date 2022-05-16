package es.lareira.practice.mappers;

import es.lareira.practice.domain.Customer;
import es.lareira.practice.model.CustomerDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import java.util.Random;
import java.util.UUID;

class CustomerMapperImplTest {

  private CustomerMapperImpl customerMapper;

  @BeforeEach
  void setUp() {
    this.customerMapper = new CustomerMapperImpl();
  }

  @RepeatedTest(3)
  void toDTO() {
    final long customerId = new Random().nextLong();
    final Customer customer = new Customer();
    customer.setFirstname(UUID.randomUUID().toString());
    customer.setLastname(UUID.randomUUID().toString());
    customer.setId(customerId);
    final CustomerDTO customerDTO = this.customerMapper.toDTO(customer);
    Assertions.assertEquals(customer.getFirstname(), customerDTO.getFirstname());
    Assertions.assertEquals(customer.getLastname(), customerDTO.getLastname());
    Assertions.assertEquals(customer.getId(), customerDTO.getId());
    Assertions.assertEquals(
        CustomerMapper.CUSTOMER_PREFIX + customer.getId(), customerDTO.getCustomerUrl());
  }

  @RepeatedTest(3)
  void toDomain() {
    final long customerId = new Random().nextLong();
    final CustomerDTO customerDTO = new CustomerDTO();
    customerDTO.setLastname(UUID.randomUUID().toString());
    customerDTO.setFirstname(UUID.randomUUID().toString());
    customerDTO.setId(customerId);
    customerDTO.setCustomerUrl(UUID.randomUUID().toString());
    final Customer customer = this.customerMapper.toDomain(customerDTO);
    Assertions.assertEquals(customerDTO.getFirstname(), customer.getFirstname());
    Assertions.assertEquals(customerDTO.getLastname(), customer.getLastname());
    Assertions.assertEquals(customerDTO.getId(), customer.getId());
  }
}
