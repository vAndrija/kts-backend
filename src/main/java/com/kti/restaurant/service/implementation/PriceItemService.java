package com.kti.restaurant.service.implementation;

import com.kti.restaurant.exception.BadLogicException;
import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.PriceItem;
import com.kti.restaurant.repository.PriceItemRepository;
import com.kti.restaurant.service.contract.IPriceItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PriceItemService implements IPriceItemService {
    private PriceItemRepository priceItemRepository;

    @Autowired
    public PriceItemService(PriceItemRepository priceItemRepository) {
        this.priceItemRepository = priceItemRepository;
    }

    @Override
    public List<PriceItem> findAll() {
        return priceItemRepository.findAll();
    }

    @Override
    public PriceItem findById(Integer id) throws Exception {
        PriceItem priceItem = priceItemRepository.findById(id).orElse(null);

        if (priceItem == null) {
            throw new MissingEntityException("The price item with given id does not exist in the system.");
        }

        return priceItem;
    }

    @Override
    public PriceItem create(PriceItem entity) throws Exception {
        entity.setStartDate(LocalDate.now());
        entity.setEndDate(null);
        PriceItem previousPriceItem = priceItemRepository.findPriceItemForDate(LocalDate.now(), entity.getMenuItem().getId());
        if(previousPriceItem != null) {
            update(previousPriceItem, previousPriceItem.getId());
        }

        return priceItemRepository.save(entity);
    }

    @Override
    public PriceItem update(PriceItem entity, Integer id) throws Exception {
        PriceItem priceItemToUpdate = this.findById(id);

        priceItemToUpdate.setCurrent(false);
        priceItemToUpdate.setEndDate(LocalDate.now());

        return priceItemRepository.save(priceItemToUpdate);
    }

    @Override
    public void delete(Integer id) throws Exception {
        this.findById(id);
        priceItemRepository.deleteById(id);
    }

    @Override
    public PriceItem findPriceForDate(LocalDate date, Integer menuItemId) {
        return priceItemRepository.findPriceItemForDate(date, menuItemId);
    }
}
