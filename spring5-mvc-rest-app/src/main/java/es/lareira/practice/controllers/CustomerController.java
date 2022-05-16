package es.lareira.practice.controllers;

import es.lareira.practice.domain.Customer;
import es.lareira.practice.mappers.CustomerMapper;
import es.lareira.practice.model.CustomerDTO;
import es.lareira.practice.model.CustomerListDTO;
import es.lareira.practice.service.CustomerService;
import es.lareira.practice.service.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

  private final CustomerService customerService;

  private final CustomerMapper customerMapper;

  public CustomerController(
      final CustomerService customerService, final CustomerMapper customerMapper) {
    this.customerService = customerService;
    this.customerMapper = customerMapper;
  }

  @GetMapping
  public ResponseEntity<CustomerListDTO> getCustomers() {
    final List<CustomerDTO> customerDTOList =
        this.customerService.getCustomers().stream()
            .map(this.customerMapper::toDTO)
            .collect(Collectors.toList());
    final CustomerListDTO customerListDTO = new CustomerListDTO();
    customerListDTO.getCustomers().addAll(customerDTOList);
    return ResponseEntity.ok(customerListDTO);
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public CustomerDTO getCustomerById(@PathVariable final Long id) {
    return this.customerService
        .findCustomerById(id)
        .map(this.customerMapper::toDTO)
        .orElseThrow(() -> new ResourceNotFoundException("Customer Not Found"));
  }

  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  public CustomerDTO createCustomer(@RequestBody final CustomerDTO customerDTO) {
    final Customer customer = this.customerMapper.toDomain(customerDTO);
    final Customer savedCustomer = this.customerService.createCustomer(customer);
    final CustomerDTO savedCustomerDTO = this.customerMapper.toDTO(savedCustomer);
    return savedCustomerDTO;
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public CustomerDTO updateCustomer(
      @PathVariable final Long id, @RequestBody final CustomerDTO customerDTO) {
    final Customer customer = this.customerMapper.toDomain(customerDTO);
    final Customer savedCustomer = this.customerService.saveCustomer(id, customer);
    final CustomerDTO savedCustomerDTO = this.customerMapper.toDTO(savedCustomer);
    return savedCustomerDTO;
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteById(@PathVariable final Long id) {
    this.customerService.deleteCustomerById(id);
  }
}
