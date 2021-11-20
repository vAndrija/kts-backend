package com.kti.restaurant.service.implementation;

import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.Salary;
import com.kti.restaurant.repository.SalaryRepository;
import com.kti.restaurant.service.contract.ISalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalaryService implements ISalaryService {

    private SalaryRepository salaryRepository;

    @Autowired
    public SalaryService(SalaryRepository salaryRepository) {
        this.salaryRepository = salaryRepository;
    }

    @Override
    public List<Salary> findAll() {
        return salaryRepository.findAll();
    }

    @Override
    public Salary findById(Integer id) throws Exception {
        Salary salary = salaryRepository.findById(id).orElse(null);

        if (salary == null) {
            throw new MissingEntityException("Salary with given id does not exist in the system.");
        }

        return salary;
    }

    @Override
    public Salary create(Salary salary) throws Exception {
        return salaryRepository.save(salary);
    }

    @Override
    public Salary update(Salary salary, Integer id) throws Exception {
        Salary salaryToUpdate = this.findById(id);

        salaryToUpdate.setValue(salary.getValue());
        salaryToUpdate.setStartDate(salary.getStartDate());
        salaryToUpdate.setEndDate(salary.getEndDate());

        salaryRepository.save(salaryToUpdate);

        return salaryToUpdate;
    }

    @Override
    public void delete(Integer id) throws Exception {
        this.findById(id);
        salaryRepository.deleteById(id);
    }
}
