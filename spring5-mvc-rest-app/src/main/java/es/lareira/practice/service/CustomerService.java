package es.lareira.practice.service;

import es.lareira.practice.domain.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
  List<Customer> getCustomers();

  Optional<Customer> findCustomerById(Long customerId);

  Customer createCustomer(Customer customer);

  Customer saveCustomer(Long id, Customer customer);

  void deleteCustomerById(Long id);
}
