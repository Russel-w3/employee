package com.example.employee;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("employees")
public class EmployeeController {

    private List<Employee> employees = new ArrayList<>();


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee create(@RequestBody Employee employee) {
        Employee newEmployee = new Employee(employees.size() + 1, employee.name(), employee.age(), employee.gender(), employee.salary());
        employees.add(newEmployee);
        return newEmployee;
    }

    @GetMapping("/{id}")
    public Employee findById(@PathVariable int id) {
        for (Employee employee : employees) {
            if (employee.id().equals(id)) {
                return employee;
            }
        }
        return null;
    }

    @GetMapping
    public List<Employee> index(@RequestParam(required = false) String gender, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        List<Employee> result = new ArrayList<>();
        if (page != null && size != null) {
            int start = (page - 1) * size;
            int end = page * size;
            for (Employee employee : employees) {
                if (employee.id() > start && employee.id() <= end) {
                    result.add(employee);
                }
            }
            return result;
        }

        if (gender == null) {
            return employees;
        }

        for (Employee employee : employees) {
            if (employee.gender().compareToIgnoreCase(gender) == 0) {
                result.add(employee);
            }
        }
        return result;
    }

    @PutMapping("/{id}")
    public Employee update(@PathVariable int id) {
        Employee oldEmployee = null;
        for (Employee employee : employees) {
            if (employee.id() == id) {
                oldEmployee = employee;
            }
        }
        if (oldEmployee == null) {
            return null;
        }
        Employee newEmployee = new Employee(oldEmployee.id(), "felix", oldEmployee.age(), oldEmployee.gender(), 3000.0);
        employees.remove(oldEmployee);
        employees.add(newEmployee);
        return newEmployee;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        employees.removeIf(employee -> employee.id() == id);
    }

    public void clear() {
        employees.clear();
    }
}
