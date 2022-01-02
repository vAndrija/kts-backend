package com.kti.restaurant.service.implementation;

import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.MenuItem;
import com.kti.restaurant.model.enums.MenuItemCategory;
import com.kti.restaurant.model.enums.MenuItemType;
import com.kti.restaurant.repository.MenuItemRepository;
import com.kti.restaurant.service.contract.IMenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MenuItemService implements IMenuItemService {
    private MenuItemRepository menuItemRepository;
    private MenuService menuService;

    @Autowired
    MenuItemService(MenuItemRepository menuItemRepository, MenuService menuService) {
        this.menuItemRepository = menuItemRepository;
        this.menuService = menuService;
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
        menuItemToUpdate.setPreparationTime(menuItem.getPreparationTime());

        menuItemRepository.save(menuItemToUpdate);

        return menuItemToUpdate;
    }

    @Override
    public Set<MenuItem> search(String search) {
        List<MenuItem> concatenated = new ArrayList<>();
        concatenated.addAll(menuItemRepository.findByNameAndDecription(search));
        concatenated.addAll(menuItemRepository.findByCategory(MenuItemCategory.findCategory(search)));
        concatenated.addAll(menuItemRepository.findByType(MenuItemType.findType(search)));
        return new HashSet<>(concatenated);
    }

    @Override
    public Set<MenuItem> filter(String filter) {
        return new HashSet<>(menuItemRepository.findByCategory(MenuItemCategory.findCategory(filter)));
    }

    @Override
    public Set<MenuItem> filterPageable(String filter, Pageable pageable) {
        return new HashSet<>(menuItemRepository.findByCategory(MenuItemCategory.findCategory(filter), pageable).getContent());
    }

    @Override
    public List<MenuItem> findByMenu(Integer menuId, Pageable pageable) throws Exception {
        menuService.findById(menuId);

        Page<MenuItem> page = menuItemRepository.findByMenu(menuId, pageable);
        return page.getContent();
    }

    @Override
    public List<MenuItem> findAll(Pageable pageable) {
        Page<MenuItem> page = menuItemRepository.findAll(pageable);
        return page.getContent();
    }

}
