package com.example.EmployeeManagementSystem;

import com.example.EmployeeManagementSystem.entities.Employee;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EmployeeManagementSystemApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String getRootUrl() {
        return "http://localhost:8080/api/v1/employees".replace("8080", String.valueOf(port));
    }

    private String getUrl(int id) {
        return getRootUrl() + "/" + id;
    }

    @Test
    @Order(1)
    void contextLoads() {
    }

    @Test
    @Order(10)
    void create() {
        Employee employee = new Employee();
        employee.setFirstName("Admin");
        employee.setLastName("System");
        employee.setEmail("admin@gmail.com");

        ResponseEntity<Employee> postResponse = restTemplate.postForEntity(
                getRootUrl(),
                employee,
                Employee.class);

        assertNotNull(postResponse);
        assertEquals(HttpStatus.CREATED, postResponse.getStatusCode());
        Employee responseBody = postResponse.getBody();
        assertNotNull(responseBody);
        assertEquals(responseBody.getFirstName(), employee.getFirstName());
        assertEquals(responseBody.getLastName(), employee.getLastName());
        assertEquals(responseBody.getEmail(), employee.getEmail());
    }

    @Test
    @Order(11)
    void createInvalidEmployee() {
        Employee employee = new Employee();
        employee.setLastName("System");
        employee.setEmail("admin@gmail.com");

        ResponseEntity<Employee> postResponse = restTemplate.postForEntity(
                getRootUrl(),
                employee,
                Employee.class);

        assertNotNull(postResponse);
        assertEquals(HttpStatus.BAD_REQUEST, postResponse.getStatusCode());
    }

    @Test
    @Order(20)
    void getBy() throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Employee> entity = new HttpEntity<>(null, headers);

        int id = 1;
        ResponseEntity<Employee> response = restTemplate.exchange(
                getUrl(id),
                HttpMethod.GET,
                entity,
                Employee.class);

        assertNotNull(response.getBody());
        assertEquals(response.getBody().getFirstName(), "Admin");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(21)
    void getByInvalidId() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        int id = 1009;
        ResponseEntity<String> response = restTemplate.exchange(
                getUrl(id),
                HttpMethod.GET,
                entity,
                String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(30)
    void getAll() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                getRootUrl(),
                HttpMethod.GET,
                entity,
                String.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(40)
    void update() {
        int id = 1;
        String url = getUrl(id);

        Employee employee = restTemplate.getForObject(url, Employee.class);
        employee.setFirstName("Administrator");

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Employee> entity = new HttpEntity<>(employee, headers);

        Map<String, Integer> param = new HashMap<>();
        param.put("id", id);

        ResponseEntity<Employee> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                entity,
                Employee.class,
                param);

        assertNotNull(response.getBody());
        assertEquals(response.getBody().getFirstName(), "Administrator");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(50)
    void delete() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        int id = 1;
        Map<String, Integer> param = new HashMap<>();
        param.put("id", id);

        ResponseEntity<String> response = restTemplate.exchange(
                getUrl(id),
                HttpMethod.DELETE,
                entity,
                String.class,
                param);

        assertNull(response.getBody());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @Order(51)
    void deleteInvalidId() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        int id = 1009;
        Map<String, Integer> param = new HashMap<>();
        param.put("id", id);

        ResponseEntity<String> response = restTemplate.exchange(
                getUrl(id),
                HttpMethod.DELETE,
                entity,
                String.class,
                param);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
