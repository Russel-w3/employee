package com.example.employee;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("companies")
public class CompanyController {

    private List<Company> companyList = new ArrayList<>();

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Company createCompany(@RequestBody Company company) {
        Company company1 = new Company(companyList.size() + 1, company.name());
        companyList.add(company1);
        return company1;
    }

    @GetMapping("/{id}")
    public Company findById(@PathVariable int id) {
        for (Company company : companyList) {
            if (company.id().equals(id)) {
                return company;
            }
        }
        return null;
    }

    @PutMapping("/{id}")
    public Company updateCompany(@PathVariable int id) {
        Company oldCompany = null;
        for(Company company : companyList) {
            if (company.id().equals(id)) {
                oldCompany = company;
            }
        }
        if(oldCompany == null) {
            return null;
        }
        Company newCompany = new Company(id, "oracle");
        companyList.remove(oldCompany);
        companyList.add(newCompany);
        return newCompany;
    }

    public void clear() {
        companyList.clear();
    }
}
