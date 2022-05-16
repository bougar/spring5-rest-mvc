package es.lareira.practice.bootstrap;

import es.lareira.practice.domain.Customer;
import es.lareira.practice.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CustomerDataBootstrap implements CommandLineRunner {
  private final CustomerRepository customerRepository;

  public CustomerDataBootstrap(final CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  @Override
  public void run(final String... args) throws Exception {
    final Customer alex = new Customer();
    alex.setFirstname("Alex");
    alex.setLastname("Miguéz");

    final Customer xurxo = new Customer();
    xurxo.setFirstname("Xurxo");
    xurxo.setLastname("Bouzas");

    this.customerRepository.save(alex);
    this.customerRepository.save(xurxo);
  }
}
