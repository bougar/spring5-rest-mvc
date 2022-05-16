package es.lareira.practice.repositories;

import es.lareira.practice.domain.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
  List<Customer> findAll();
}
