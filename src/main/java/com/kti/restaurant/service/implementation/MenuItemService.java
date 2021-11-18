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
        return menuItemRepository.findAll();
    }

    @Override
    public MenuItem findById(Integer id) throws Exception {
        MenuItem menuItem = menuItemRepository.findById(id).orElse(null);

        if (menuItem == null) {
            throw new MissingEntityException("Menu item with given id does not exist in the system.");
        }

        return menuItem;
    }

    @Override
    public MenuItem create(MenuItem menuItem) throws Exception {
        return menuItemRepository.save(menuItem);
    }

    @Override
    public void delete(Integer id) throws Exception {
        this.findById(id);
        menuItemRepository.deleteById(id);
    }

    @Override
    public MenuItem update(MenuItem menuItem, Integer id) throws Exception {
        MenuItem menuItemToUpdate = this.findById(id);

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
