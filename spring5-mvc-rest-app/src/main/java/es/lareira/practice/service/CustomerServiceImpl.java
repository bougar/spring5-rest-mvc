package es.lareira.practice.service;

import es.lareira.practice.domain.Customer;
import es.lareira.practice.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
  private final CustomerRepository customerRepository;

  public CustomerServiceImpl(final CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  @Override
  public List<Customer> getCustomers() {
    return this.customerRepository.findAll();
  }

  @Override
  public Optional<Customer> findCustomerById(final Long customerId) {
    return this.customerRepository.findById(customerId);
  }

  @Override
  public Customer createCustomer(final Customer customer) {
    return this.customerRepository.save(customer);
  }

  @Override
  public Customer saveCustomer(final Long id, final Customer customer) {
    final Customer toSaveCustomer = customer.toBuilder().id(id).build();
    return this.customerRepository.save(toSaveCustomer);
  }

  @Override
  public void deleteCustomerById(final Long id) {
    this.customerRepository.deleteById(id);
  }
}
