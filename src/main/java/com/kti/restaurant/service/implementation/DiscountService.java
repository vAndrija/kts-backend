package com.kti.restaurant.service.implementation;

import com.kti.restaurant.exception.BadLogicException;
import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.Discount;
import com.kti.restaurant.repository.DiscountRepository;
import com.kti.restaurant.service.contract.IDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class DiscountService implements IDiscountService {
    private DiscountRepository discountRepository;

    @Autowired
    public DiscountService(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    @Override
    public List<Discount> findAll() {
        return discountRepository.findAll();
    }

    @Override
    public Discount findById(Integer id) throws Exception {
        Discount discount = discountRepository.findById(id).orElse(null);

        if(discount == null) {
            throw new MissingEntityException("The given discount does not exist in the system.");
        }

        return discount;
    }

    @Override
    public Discount create(Discount entity) throws Exception {
        if(entity.getEndDate().isBefore(entity.getStartDate())) {
            throw new BadLogicException("The end date must be after start date.");
        }
        return discountRepository.save(entity);
    }

    @Override
    public Discount update(Discount entity, Integer id) throws Exception {
        Discount discountToUpdate = this.findById(id);

        if(entity.getEndDate().isBefore(entity.getStartDate())) {
            throw new BadLogicException("The end date must be after start date.");
        }

        discountToUpdate.setCurrent(entity.getCurrent());
        discountToUpdate.setEndDate(entity.getEndDate());
        discountToUpdate.setStartDate(entity.getStartDate());
        discountToUpdate.setValue(entity.getValue());
        discountToUpdate.setMenuItem(entity.getMenuItem());

        return discountRepository.save(discountToUpdate);
    }

    @Override
    public void delete(Integer id) throws Exception {
        this.findById(id);
        discountRepository.deleteById(id);
    }
}
