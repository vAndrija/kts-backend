package com.kti.restaurant.service.implementation;

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
    public MenuItem findById(Integer id) {
        return menuItemRepository.findById(id).orElseGet(null);
    }

    @Override
    public MenuItem create(MenuItem menuItem) throws Exception {
        return menuItemRepository.save(menuItem);
    }

    @Override
    public void delete(Integer id) {
        menuItemRepository.deleteById(id);
    }

    @Override
    public MenuItem update(MenuItem menuItem) throws Exception {
        MenuItem menuItemToUpdate = menuItemRepository.findById(menuItem.getId()).get();

        if(menuItemToUpdate == null) {
            throw new Exception("Entity with given id does not exist in the system.");
        }

//        kad dodas menu servis ovde staviti pronalazenje menija i njegovo umetanje
//        menuItemToUpdate.setMenu(updateMenuItemDto.getMenu());
        menuItemToUpdate.setAccepted(menuItem.getAccepted());
        menuItemToUpdate.setCategory(menuItem.getCategory());
        menuItemToUpdate.setDescription(menuItem.getDescription());
        menuItemToUpdate.setName(menuItem.getName());
        menuItemToUpdate.setType(menuItem.getType());

        menuItemRepository.save(menuItemToUpdate);

        return menuItemToUpdate;
    }
}
