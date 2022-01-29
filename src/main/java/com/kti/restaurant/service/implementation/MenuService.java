package com.kti.restaurant.service.implementation;

import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.Menu;
import com.kti.restaurant.repository.MenuRepository;
import com.kti.restaurant.service.contract.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService implements IMenuService {
    private MenuRepository menuRepository;

    @Autowired
    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public List<Menu> findAll() {
        return menuRepository.findAll();
    }

    @Override
    public Menu findById(Integer id) throws Exception {
        Menu menu = menuRepository.findById(id).orElse(null);

        if (menu == null) {
            throw new MissingEntityException("Menu with given id does not exist in the system.");
        }

        return menu;
    }

    @Override
    public Menu create(Menu entity) throws Exception {
        return menuRepository.save(entity);
    }

    @Override
    public Menu update(Menu entity, Integer id) throws Exception {
        Menu menuToUpdate = this.findById(id);

        menuToUpdate.setName(entity.getName());
        menuToUpdate.setDurationStart(entity.getDurationStart());
        menuToUpdate.setDurationEnd(entity.getDurationEnd());

        menuRepository.save(menuToUpdate);

        return menuToUpdate;
    }

    @Override
    public void delete(Integer id) throws Exception {
        this.findById(id);
        menuRepository.deleteById(id);
    }
}
