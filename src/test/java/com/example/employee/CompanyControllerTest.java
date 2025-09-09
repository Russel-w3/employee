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
public class CompanyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyController companyController;

    @BeforeEach
    public void setUp() throws Exception {
        companyController.clear();
    }

    @Test
    public void should_return_created_companies_when_post() throws Exception {
        //Given
        String requestBody = """
                {
                    "name":"spring"
                }
                """;
        MockHttpServletRequestBuilder request = post("/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);
        //When-Then
        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("spring"));
    }

    @Test
    public void should_return_company_when_get_company_with_id_exists() throws Exception {
        Company company = new Company(null,"spring");
        Company expectedCompany = companyController.createCompany(company);

        MockHttpServletRequestBuilder request = get("/companies/" + expectedCompany.id())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedCompany.id()))
                .andExpect(jsonPath("$.name").value(expectedCompany.name()));
    }

    @Test
    public void should_return_company_list_when_get_company_with_no_param() throws Exception {
        Company company = new Company(null,"spring");
        Company company1 = new Company(null,"oracle");
        companyController.createCompany(company);
        companyController.createCompany(company1);
        MockHttpServletRequestBuilder request = get("/companies")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void should_return_new_company_when_update_an_company() throws Exception {
        Company company = new Company(null,"spring");
        Company expectedCompany = companyController.createCompany(company);
        MockHttpServletRequestBuilder request = put("/companies/" + expectedCompany.id())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("oracle"));
    }

}
