package com.kti.restaurant.service.implementation;

import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.MenuItem;
import com.kti.restaurant.repository.MenuItemRepository;
import com.kti.restaurant.service.contract.IMenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuItemService implements IMenuItemService {
    private MenuItemRepository menuItemRepository;

    @Autowired
    MenuItemService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    public List<MenuItem> findAll() {
        return (List<MenuItem>) menuItemRepository.findAll();
    }

    @Override
    public MenuItem findById(Integer id) throws Exception {
        return menuItemRepository.findById(id).orElse(null);
    }

    @Override
    public MenuItem create(MenuItem menuItem) throws Exception {
        return menuItemRepository.save(menuItem);
    }

    @Override
    public void delete(Integer id) throws Exception {
        menuItemRepository.deleteById(id);
    }

    @Override
    public MenuItem update(MenuItem menuItem) throws Exception {
        MenuItem menuItemToUpdate = menuItemRepository.findById(menuItem.getId()).orElse(null);

        if (menuItemToUpdate == null) {
            throw new MissingEntityException("Menu item with given id does not exist in the system.");
        }

        menuItemToUpdate.setMenu(menuItem.getMenu());
        menuItemToUpdate.setAccepted(menuItem.getAccepted());
        menuItemToUpdate.setCategory(menuItem.getCategory());
        menuItemToUpdate.setDescription(menuItem.getDescription());
        menuItemToUpdate.setName(menuItem.getName());
        menuItemToUpdate.setType(menuItem.getType());

        menuItemRepository.save(menuItemToUpdate);

        return menuItemToUpdate;
    }
}
