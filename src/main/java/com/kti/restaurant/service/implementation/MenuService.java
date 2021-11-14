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
    public Menu findById(Integer id) {
        return menuRepository.findById(id).orElse(null);
    }

    @Override
    public Menu create(Menu entity) {
        return menuRepository.save(entity);
    }

    @Override
    public Menu update(Menu entity) throws Exception {
        Menu menuToUpdate = menuRepository.findById(entity.getId()).orElse(null);

        if(menuToUpdate == null) {
            throw new MissingEntityException("Menu with given id does not exist in the system.");
        }

        menuToUpdate.setName(entity.getName());
        menuToUpdate.setDurationStart(entity.getDurationStart());
        menuToUpdate.setDurationEnd(entity.getDurationEnd());

        menuRepository.save(menuToUpdate);

        return menuToUpdate;
    }

    @Override
    public void delete(Integer id) {
        menuRepository.deleteById(id);
    }
}
