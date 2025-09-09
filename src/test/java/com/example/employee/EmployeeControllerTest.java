package com.example.employee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeController employeeController;

    @BeforeEach
    public void setUp() {
        employeeController.clear();
    }

    @Test
    public void should_return_created_employee_when_post() throws Exception {
        //Given
        String requestBody = """
                {
                    "name": "John Smith",
                    "age": 32,
                    "gender": "Male",
                    "salary": 5000.0
                }
                """;
        MockHttpServletRequestBuilder request = post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);
        //When-Then
        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("John Smith"))
                .andExpect(jsonPath("$.age").value(32))
                .andExpect(jsonPath("$.gender").value("Male"))
                .andExpect(jsonPath("$.salary").value(5000.0));
    }

    @Test
    public void should_return_employee_when_get_employee_with_id_exists() throws Exception {
        Employee employee = new Employee(null, "John Smith", 32, "Male", 5000.0);
        Employee expectedEmployee = employeeController.create(employee);

        MockHttpServletRequestBuilder request = get("/employees/" + expectedEmployee.id())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedEmployee.id()))
                .andExpect(jsonPath("$.name").value(expectedEmployee.name()))
                .andExpect(jsonPath("$.age").value(expectedEmployee.age()))
                .andExpect(jsonPath("$.gender").value(expectedEmployee.gender()))
                .andExpect(jsonPath("$.salary").value(expectedEmployee.salary()));
    }

    @Test
    public void should_return_males_employee_when_list_by_male() throws Exception {
        Employee expectedEmployee = employeeController.create(new Employee(null, "John Smith", 32, "Male", 5000.0));
        employeeController.create(new Employee(null, "Lily", 22, "Female", 5000.0));
        MockHttpServletRequestBuilder request = get("/employees?gender=male")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(expectedEmployee.id()))
                .andExpect(jsonPath("$[0].name").value(expectedEmployee.name()))
                .andExpect(jsonPath("$[0].age").value(expectedEmployee.age()))
                .andExpect(jsonPath("$[0].gender").value(expectedEmployee.gender()))
                .andExpect(jsonPath("$[0].salary").value(expectedEmployee.salary()));
    }

    @Test
    public void should_return_employee_list_when_get_employees() throws Exception {
        employeeController.create(new Employee(null, "John Smith", 32, "Male", 5000.0));
        employeeController.create(new Employee(null, "Lily", 22, "Female", 5000.0));
        MockHttpServletRequestBuilder request = get("/employees")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void should_return_new_employee_when_update_an_employee() throws Exception {
        Employee expectedEmployee = employeeController.create(new Employee(null, "John Smith", 32, "Male", 5000.0));
        MockHttpServletRequestBuilder request = put("/employees/" + expectedEmployee.id())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("felix"))
                .andExpect(jsonPath("$.salary").value(3000.0));
    }

    @Test
    public void should_return_204_when_delete_an_employee() throws Exception {
        Employee expectedEmployee = employeeController.create(new Employee(null, "John Smith", 32, "Male", 5000.0));
        MockHttpServletRequestBuilder request = delete("/employees/" + expectedEmployee.id())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNoContent());
    }

    @Test
    public void should_return_page_when_query_page() throws Exception {
        Employee employee1 = employeeController.create(new Employee(null, "John Smith", 32, "Male", 5000.0));
        Employee employee2 = employeeController.create(new Employee(null, "John", 32, "Male", 5000.0));
        Employee employee3 = employeeController.create(new Employee(null, "John2", 32, "Male", 5000.0));

        MockHttpServletRequestBuilder request = get("/employees?page=1&size=2")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

}
