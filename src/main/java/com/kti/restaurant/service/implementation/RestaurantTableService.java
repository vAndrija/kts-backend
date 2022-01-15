package com.kti.restaurant.service.implementation;

import com.kti.restaurant.exception.BadLogicException;
import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.RestaurantTable;
import com.kti.restaurant.repository.RestaurantTableRepository;
import com.kti.restaurant.service.contract.IRestaurantTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantTableService implements IRestaurantTableService {
    private RestaurantTableRepository restaurantTableRepository;

    @Autowired
    RestaurantTableService(RestaurantTableRepository restaurantTableRepository) {
        this.restaurantTableRepository = restaurantTableRepository;
    }

    @Override
    public List<RestaurantTable> findAll() {
        return restaurantTableRepository.findAll();
    }

    @Override
    public RestaurantTable findById(Integer id) throws Exception {
        RestaurantTable table = restaurantTableRepository.findById(id).orElse(null);

        if (table == null) {
            throw new MissingEntityException("Restaurant with given id does not exist in the system.");
        }

        return table;
    }

    @Override
    public RestaurantTable create(RestaurantTable restaurantTable) throws Exception {
        if (restaurantTable.getCapacity() == null || restaurantTable.getTableNumber() == null) {
            throw new BadLogicException("Capacity and table number cannot be null.");
        }

        if (restaurantTable.getCapacity() < 1) {
            throw new BadLogicException("Capacity should be greater than 1.");
        }
        if (!check(restaurantTable.getxCoordinate(), restaurantTable.getyCoordinate())) {
            throw new BadLogicException("Error ");
        }

        return restaurantTableRepository.save(restaurantTable);
    }

    @Override
    public boolean check(Integer x, Integer y) {
        List<RestaurantTable> restaurantTables = this.findAll();
        for (RestaurantTable table : restaurantTables) {
            if (Math.abs(table.getxCoordinate() - x) < 80 && Math.abs(table.getyCoordinate() - y) < 80) {
                return false;
            }
        }
        return true;
    }

    @Override
    public RestaurantTable update(RestaurantTable restaurantTable, Integer id) throws Exception {
        RestaurantTable restaurantTableToUpdate = this.findById(id);

        if (restaurantTable.getCapacity() == null || restaurantTable.getTableNumber() == null) {
            throw new BadLogicException("Capacity and table number cannot be null.");
        }

        if (restaurantTable.getCapacity() < 1) {
            throw new BadLogicException("Capacity should be greater than 1.");
        }

        restaurantTableToUpdate.setTableNumber(restaurantTable.getTableNumber());
        restaurantTableToUpdate.setCapacity(restaurantTable.getCapacity());
        restaurantTableToUpdate.setxCoordinate(restaurantTable.getxCoordinate());
        restaurantTableToUpdate.setyCoordinate(restaurantTable.getyCoordinate());

        restaurantTableRepository.save(restaurantTableToUpdate);

        return restaurantTableToUpdate;
    }

    @Override
    public void delete(Integer id) throws Exception {
        this.findById(id);
        restaurantTableRepository.deleteById(id);
    }
}
