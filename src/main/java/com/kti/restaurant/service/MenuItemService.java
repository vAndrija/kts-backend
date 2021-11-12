package com.kti.restaurant.service;

import com.kti.restaurant.dto.MenuItemDto;
import com.kti.restaurant.dto.UpdateMenuItemDto;
import com.kti.restaurant.model.Menu;
import com.kti.restaurant.model.MenuItem;
import com.kti.restaurant.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class MenuItemService {
    @Autowired
    private MenuItemRepository menuItemRepository;

    public Collection<MenuItem> findAll() {
        return (Collection<MenuItem>) menuItemRepository.findAll();
    }

    public MenuItem findOne(Integer id) {
        return menuItemRepository.findById(id).orElseGet(null);
    }

    public MenuItem create(MenuItemDto menuItemDto) throws Exception {
        System.out.println("CAO");
        MenuItem menuItem = new MenuItem(menuItemDto);
        System.out.println(menuItem.getName());
        return menuItemRepository.save(menuItem);
    }

    public void delete(Integer id) {
        menuItemRepository.deleteById(id);
    }

    public MenuItem update(UpdateMenuItemDto updateMenuItemDto) throws Exception {
        MenuItem menuItemToUpdate = menuItemRepository.findById(updateMenuItemDto.getId()).orElseGet(null);
        if(menuItemToUpdate == null) {
            throw new Exception("Entity with given id does not exist in the system.");
        }

//        kad dodas menu servis ovde staviti pronalazenje menija i njegovo umetanje
//        menuItemToUpdate.setMenu(updateMenuItemDto.getMenu());
        menuItemToUpdate.setAccepted(updateMenuItemDto.getAccepted());
        menuItemToUpdate.setCategory(updateMenuItemDto.getCategory());
        menuItemToUpdate.setDescription(updateMenuItemDto.getDescription());
        menuItemToUpdate.setName(updateMenuItemDto.getName());
        menuItemToUpdate.setType(updateMenuItemDto.getType());

        menuItemRepository.save(menuItemToUpdate);

        return menuItemToUpdate;
    }
}
