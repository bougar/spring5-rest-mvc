package es.lareira.practice.controllers;

import es.lareira.practice.domain.Customer;
import es.lareira.practice.mappers.CustomerMapper;
import es.lareira.practice.model.CustomerDTO;
import es.lareira.practice.model.CustomerListDTO;
import es.lareira.practice.service.CustomerService;
import es.lareira.practice.service.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

  @Mock private CustomerService customerService;

  @Mock private CustomerMapper customerMapper;

  private CustomerController customerController;

  @BeforeEach
  void setUp() {
    this.customerController = new CustomerController(this.customerService, this.customerMapper);
  }

  @Test
  void getCustomers() {
    final List<Customer> customerList =
        List.of(Mockito.mock(Customer.class), Mockito.mock(Customer.class));
    final CustomerDTO customerDTO1 = Mockito.mock(CustomerDTO.class);
    when(this.customerMapper.toDTO(customerList.get(0))).thenReturn(customerDTO1);
    final CustomerDTO customerDTO2 = Mockito.mock(CustomerDTO.class);
    when(this.customerMapper.toDTO(customerList.get(1))).thenReturn(customerDTO2);
    when(this.customerService.getCustomers()).thenReturn(customerList);
    final ResponseEntity<CustomerListDTO> response = this.customerController.getCustomers();
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    final CustomerListDTO customerListDTO = response.getBody();
    Assertions.assertNotNull(customerListDTO);
    Assertions.assertNotNull(customerListDTO.getCustomers());
    Assertions.assertEquals(2, customerListDTO.getCustomers().size());
    Assertions.assertTrue(customerListDTO.getCustomers().contains(customerDTO1));
    Assertions.assertTrue(customerListDTO.getCustomers().contains(customerDTO2));
  }

  @Test
  void getCustomersWhenNoCustomersAvailable() {
    final ResponseEntity<CustomerListDTO> response = this.customerController.getCustomers();
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    final CustomerListDTO customerListDTO = response.getBody();
    Assertions.assertNotNull(customerListDTO);
    Assertions.assertNotNull(customerListDTO.getCustomers());
    Assertions.assertEquals(0, customerListDTO.getCustomers().size());
  }

  @ParameterizedTest
  @ValueSource(longs = {1, 43, 314, 999})
  void getCustomerByIdWhenNotFound(final Long customerId) {
    Assertions.assertThrows(
        ResourceNotFoundException.class, () -> this.customerController.getCustomerById(customerId));
  }

  @ParameterizedTest
  @ValueSource(longs = {1, 43, 314, 999})
  void getCustomerById(final Long customerId) {
    final Customer customer = Mockito.mock(Customer.class);
    when(this.customerService.findCustomerById(customerId)).thenReturn(Optional.of(customer));
    final CustomerDTO customerDTO = Mockito.mock(CustomerDTO.class);
    when(this.customerMapper.toDTO(customer)).thenReturn(customerDTO);
    final CustomerDTO actual = this.customerController.getCustomerById(customerId);
    Assertions.assertSame(customerDTO, actual);
  }

  @Test
  void createCustomer() {
    final CustomerDTO customerDTO = Mockito.mock(CustomerDTO.class);
    final Customer inputCustomer = Mockito.mock(Customer.class);
    when(this.customerMapper.toDomain(customerDTO)).thenReturn(inputCustomer);
    final Customer savedCustomer = Mockito.mock(Customer.class);
    when(this.customerService.createCustomer(inputCustomer)).thenReturn(savedCustomer);
    final CustomerDTO savedCustomerDTO = Mockito.mock(CustomerDTO.class);
    when(this.customerMapper.toDTO(savedCustomer)).thenReturn(savedCustomerDTO);
    final CustomerDTO actual = this.customerController.createCustomer(customerDTO);
    Assertions.assertSame(savedCustomerDTO, actual);
  }

  @ParameterizedTest
  @ValueSource(longs = {1, 34, 4343, 4432})
  void updateCustomer(final Long customerId) {
    final CustomerDTO input = Mockito.mock(CustomerDTO.class);
    final Customer customer = Mockito.mock(Customer.class);
    when(this.customerMapper.toDomain(input)).thenReturn(customer);
    final Customer savedCustomer = Mockito.mock(Customer.class);
    when(this.customerService.saveCustomer(customerId, customer)).thenReturn(savedCustomer);
    final CustomerDTO savedCustomerDTO = Mockito.mock(CustomerDTO.class);
    when(this.customerMapper.toDTO(savedCustomer)).thenReturn(savedCustomerDTO);
    final CustomerDTO actual = this.customerController.updateCustomer(customerId, input);
    Assertions.assertSame(savedCustomerDTO, actual);
  }

  @ParameterizedTest
  @ValueSource(longs = {1, 34, 4343, 4432})
  void deleteById(final Long customerId) {
    this.customerController.deleteById(customerId);
    verify(this.customerService).deleteCustomerById(customerId);
  }
}
