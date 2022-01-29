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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        return (List<MenuItem>) menuItemRepository.findAll();
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
        menuItemToUpdate.setImageName(menuItem.getImageName());

        menuItemRepository.save(menuItemToUpdate);

        return menuItemToUpdate;
    }

    @Override
    public Set<MenuItem> search(String search, LocalDateTime localDateTime) {
        List<MenuItem> concatenated = new ArrayList<>();
        concatenated.addAll(menuItemRepository.findByNameAndDescription(search, localDateTime));
        concatenated.addAll(menuItemRepository.findByCategory(MenuItemCategory.findCategory(search), localDateTime));
        concatenated.addAll(menuItemRepository.findByType(MenuItemType.findType(search), localDateTime));
        return new HashSet<>(concatenated);
    }

    @Override
    public Set<MenuItem> filter(String filter, LocalDateTime localDateTime) {
        return new HashSet<>(menuItemRepository.findByCategory(MenuItemCategory.findCategory(filter), localDateTime));
    }


    @Override
    public Page<MenuItem> pendingMenuItems(Pageable pageable) {
        return menuItemRepository.findPendingMenuItems(pageable);
    }

    @Override
    public Page<MenuItem> filterPageable(String filter, Pageable pageable, LocalDateTime localDateTime) {
        return menuItemRepository.findByCategory(MenuItemCategory.findCategory(filter), localDateTime, pageable);
    }

    @Override
    public Page<MenuItem> findByMenu(Integer menuId, Pageable pageable) throws Exception {
        menuService.findById(menuId);

        return menuItemRepository.findByMenu(menuId, pageable);
    }

    @Override
    public Page<MenuItem> findAll(Pageable pageable) {
        return menuItemRepository.findAll(pageable);
    }

    @Override
    public Page<MenuItem> findAllInActiveMenu(Pageable pageable, LocalDateTime localDateTime) {
        return menuItemRepository.findAllInActiveMenu(localDateTime, pageable);
    }

    @Override
    public Page<MenuItem> searchAndFilterMenuItems(Integer menuId, String searchParam, String category, Pageable pageable) throws Exception {
        menuService.findById(menuId);
        return menuItemRepository.searchAndFilterMenuItems(menuId, searchParam, MenuItemCategory.findCategory(category), pageable);
    }

}
