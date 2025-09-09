package com.example.employee;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("employees")
public class EmployeeController {

    private List<Employee> employees = new ArrayList<>();

    private int id = 0;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee create(@RequestBody Employee employee){
        int id = ++this.id;
        Employee newEmployee = new Employee(id, employee.name(), employee.age(), employee.gender(), employee.salary());
        employees.add(newEmployee);
        return newEmployee;
    }

    @GetMapping("/{id}")
    public Employee findById(@PathVariable int id){
        for (Employee employee : employees) {
            if(employee.id().equals(id)){
                return employee;
            }
        }
        return null;
    }

    @GetMapping
    public List<Employee> index(@RequestParam(required = false) String gender){
        if(gender == null){
            return employees;
        }
        List<Employee> result = new ArrayList<>();
        for (Employee employee : employees) {
            if (employee.gender().compareToIgnoreCase(gender) == 0) {
                result.add(employee);
            }
        }
        return result;
    }

    public void clear(){
        employees.clear();
    }
}
