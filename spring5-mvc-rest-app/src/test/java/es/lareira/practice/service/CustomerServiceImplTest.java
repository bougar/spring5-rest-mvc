package es.lareira.practice.service;

import es.lareira.practice.domain.Customer;
import es.lareira.practice.repositories.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

  @Mock private CustomerRepository customerRepository;

  private CustomerService customerService;

  @BeforeEach
  void setUp() {
    this.customerService = new CustomerServiceImpl(this.customerRepository);
  }

  @Test
  void getCustomers() {
    final List<Customer> customers =
        List.of(Mockito.mock(Customer.class), Mockito.mock(Customer.class));
    when(this.customerRepository.findAll()).thenReturn(customers);
    final List<Customer> actual = this.customerService.getCustomers();
    Assertions.assertEquals(customers, actual);
  }

  @Test
  void getCustomersWhenNoCustomers() {
    final List<Customer> customers = this.customerService.getCustomers();
    Assertions.assertNotNull(customers);
    Assertions.assertEquals(0, customers.size());
  }

  @ParameterizedTest
  @ValueSource(longs = {1, 5, 64, 999})
  void findCustomerById(final Long id) {
    final Customer customer = Mockito.mock(Customer.class);
    when(this.customerRepository.findById(id)).thenReturn(Optional.of(customer));
    final Optional<Customer> optionalCustomer = this.customerService.findCustomerById(id);
    Assertions.assertTrue(optionalCustomer.isPresent());
    Assertions.assertEquals(customer, optionalCustomer.get());
  }

  @ParameterizedTest
  @ValueSource(longs = {1, 5, 64, 999})
  void findCustomerByIdWhenNoData(final Long id) {
    final Optional<Customer> optionalCustomer = this.customerService.findCustomerById(id);
    Assertions.assertTrue(optionalCustomer.isEmpty());
  }

  @Test
  void createCustomer() {
    final Customer input = Mockito.mock(Customer.class);
    final Customer expected = Mockito.mock(Customer.class);
    when(this.customerRepository.save(input)).thenReturn(expected);
    final Customer actual = this.customerService.createCustomer(input);
    Assertions.assertSame(expected, actual);
  }

  @ParameterizedTest
  @ValueSource(longs = {1, 5, 64, 999})
  void saveCustomer(final Long customerId) {
    final Customer customer = new Customer();
    customer.setFirstname(UUID.randomUUID().toString());
    customer.setLastname(UUID.randomUUID().toString());
    customer.setId(null);
    final Customer savedCustomer = Mockito.mock(Customer.class);
    when(this.customerRepository.save(any())).thenReturn(savedCustomer);
    final Customer actual = this.customerService.saveCustomer(customerId, customer);
    Assertions.assertSame(savedCustomer, actual);
    final ArgumentCaptor<Customer> argumentCaptor = ArgumentCaptor.forClass(Customer.class);
    verify(this.customerRepository).save(argumentCaptor.capture());
    Assertions.assertEquals(customer.getFirstname(), argumentCaptor.getValue().getFirstname());
    Assertions.assertEquals(customer.getLastname(), argumentCaptor.getValue().getLastname());
    Assertions.assertEquals(customerId, argumentCaptor.getValue().getId());
    Assertions.assertNotSame(customer, argumentCaptor.getValue());
  }

  @ParameterizedTest
  @ValueSource(longs = {1, 5, 64, 999})
  void deleteCustomerById(final Long customerId) {
    this.customerService.deleteCustomerById(customerId);
    verify(this.customerRepository).deleteById(customerId);
  }
}
