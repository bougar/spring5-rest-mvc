package es.lareira.practice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import es.lareira.practice.domain.Customer;
import es.lareira.practice.mappers.CustomerMapper;
import es.lareira.practice.mappers.CustomerMapperImpl;
import es.lareira.practice.model.CustomerDTO;
import es.lareira.practice.service.CustomerService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerMvcTest {

  @Mock private CustomerService customerService;

  private final CustomerMapper customerMapper = new CustomerMapperImpl();

  private final ObjectMapper objectMapper = new ObjectMapper();

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    final CustomerController customerController =
        new CustomerController(this.customerService, this.customerMapper);
    this.mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
  }

  @ParameterizedTest
  @ValueSource(longs = {1, 2, 5, 434})
  @SneakyThrows
  void updateCustomer(final Long customerId) {
    final CustomerDTO customerDTO = new CustomerDTO();
    customerDTO.setFirstname(UUID.randomUUID().toString());
    customerDTO.setLastname(UUID.randomUUID().toString());
    customerDTO.setCustomerUrl(UUID.randomUUID().toString());

    final Customer savedCustomer = this.customerMapper.toDomain(customerDTO);
    savedCustomer.setId(customerId);
    when(this.customerService.saveCustomer(eq(customerId), any())).thenReturn(savedCustomer);

    final MockHttpServletRequestBuilder requestBuilder =
        MockMvcRequestBuilders.put("/api/v1/customers/" + customerId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(this.objectMapper.writeValueAsString(customerDTO));
    this.mockMvc
        .perform(requestBuilder)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.firstname", equalTo(customerDTO.getFirstname())))
        .andExpect(jsonPath("$.lastname", equalTo(customerDTO.getLastname())))
        .andExpect(jsonPath("$.id", equalTo(Integer.valueOf(customerId.toString()))))
        .andExpect(jsonPath("$.customerUrl", equalTo("/api/v1/customers/" + customerId)));
  }

  @Test
  @SneakyThrows
  void getCustomers() {
    final Customer customer1 = new Customer();
    customer1.setId(1L);
    customer1.setFirstname("name1");
    customer1.setLastname("lastname1");
    final CustomerDTO customerDTO1 = this.customerMapper.toDTO(customer1);
    final Customer customer2 = new Customer();
    customer2.setId(2L);
    customer2.setFirstname("name2");
    customer2.setLastname("lastname2");
    final CustomerDTO customerDTO2 = this.customerMapper.toDTO(customer2);

    when(this.customerService.getCustomers()).thenReturn(List.of(customer1, customer2));
    final MockHttpServletRequestBuilder requestBuilder =
        MockMvcRequestBuilders.get("/api/v1/customers").accept(MediaType.APPLICATION_JSON);
    this.mockMvc
        .perform(requestBuilder)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.customers", hasSize(2)))
        .andExpect(
            jsonPath(
                "$.customers[0]",
                equalTo(JsonPath.read(this.objectMapper.writeValueAsString(customerDTO1), "$"))))
        .andExpect(
            jsonPath(
                "$.customers[1]",
                equalTo(JsonPath.read(this.objectMapper.writeValueAsString(customerDTO2), "$"))));
  }

  @ParameterizedTest
  @ValueSource(longs = {1, 2, 5, 434})
  @SneakyThrows
  void getCustomerById(final Long customerId) {
    final CustomerDTO customerDTO = new CustomerDTO();
    customerDTO.setId(customerId);
    customerDTO.setFirstname(UUID.randomUUID().toString());
    customerDTO.setLastname(UUID.randomUUID().toString());
    customerDTO.setCustomerUrl(UUID.randomUUID().toString());

    final Customer savedCustomer = this.customerMapper.toDomain(customerDTO);

    when(this.customerService.findCustomerById(customerId)).thenReturn(Optional.of(savedCustomer));

    final MockHttpServletRequestBuilder requestBuilder =
        MockMvcRequestBuilders.get("/api/v1/customers/" + customerId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(this.objectMapper.writeValueAsString(customerDTO));

    final MvcResult mvcResult =
        this.mockMvc
            .perform(requestBuilder)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstname", equalTo(customerDTO.getFirstname())))
            .andExpect(jsonPath("$.lastname", equalTo(customerDTO.getLastname())))
            .andExpect(jsonPath("$.id", equalTo(Integer.valueOf(customerId.toString()))))
            .andExpect(jsonPath("$.customerUrl", equalTo("/api/v1/customers/" + customerId)))
            .andReturn();
  }

  @ParameterizedTest
  @ValueSource(longs = {1, 2, 5, 434})
  @SneakyThrows
  void createCustomer(final Long customerId) {
    final CustomerDTO customerDTO = new CustomerDTO();
    customerDTO.setFirstname(UUID.randomUUID().toString());
    customerDTO.setLastname(UUID.randomUUID().toString());
    customerDTO.setCustomerUrl(UUID.randomUUID().toString());

    final Customer savedCustomer = this.customerMapper.toDomain(customerDTO);
    savedCustomer.setId(customerId);

    when(this.customerService.createCustomer(any())).thenReturn(savedCustomer);

    final MockHttpServletRequestBuilder requestBuilder =
        MockMvcRequestBuilders.post("/api/v1/customers")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(this.objectMapper.writeValueAsString(customerDTO));
    this.mockMvc
        .perform(requestBuilder)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.firstname", equalTo(customerDTO.getFirstname())))
        .andExpect(jsonPath("$.lastname", equalTo(customerDTO.getLastname())))
        .andExpect(jsonPath("$.id", equalTo(Integer.valueOf(customerId.toString()))))
        .andExpect(jsonPath("$.customerUrl", equalTo("/api/v1/customers/" + customerId)));
  }

  @ParameterizedTest
  @ValueSource(longs = {1, 2, 5, 434})
  @SneakyThrows
  void deleteById(final Long customerId) {
    final MockHttpServletRequestBuilder requestBuilder = delete("/api/v1/customers/" + customerId);
    this.mockMvc.perform(requestBuilder).andExpect(status().isNoContent());
  }
}
