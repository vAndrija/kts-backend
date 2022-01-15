package com.kti.restaurant.service.implementation;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.RestaurantTable;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class RestaurantTableServiceIntegrationTests {

    @Autowired
    private RestaurantTableService tableService;

    private static final String message = "Restaurant with given id does not exist in the system.";

    @Test
    public void findAll_ReturnsValidRestaurantTables() {
        List<RestaurantTable> tables = tableService.findAll();

        assertEquals(10, tables.size());
    }

    @Test
    public void findById_ValidId_ReturnsValidTable() throws Exception {
        RestaurantTable table = tableService.findById(1);

        assertEquals(Integer.valueOf(1), table.getId());
        assertEquals(Integer.valueOf(4), table.getCapacity());
        assertEquals(Integer.valueOf(989), table.getxCoordinate());
        assertEquals(Integer.valueOf(41), table.getyCoordinate());
    }

    @Test
    public void findById_InvalidId_ThrowsMissingEntityException() {
        Exception exception = assertThrows(MissingEntityException.class, () -> {
            tableService.findById(-1);
        });

        assertEquals(message, exception.getMessage());
    }

    @Test
    @Rollback
    public void create_ValidId_ReturnsCreatedRestaurantTable() throws Exception {
        RestaurantTable table = tableService.create(new RestaurantTable(false, 1, 4, 500, 500));

        assertEquals(Integer.valueOf(11), table.getId());
        assertEquals(Integer.valueOf(4), table.getCapacity());
        assertEquals(Integer.valueOf(500), table.getxCoordinate());
        assertEquals(Integer.valueOf(500), table.getyCoordinate());

    }

    @Test
    @Rollback
    public void update_ValidId_ReturnsUpdatedRestaurantTable() throws Exception {
        RestaurantTable tableToUpdate = new RestaurantTable(false, 1, 5, 1, 1);
        tableToUpdate.setId(1);

        RestaurantTable table = tableService.update(tableToUpdate, 1);

        assertEquals(tableToUpdate.getId(), table.getId());
        assertEquals(tableToUpdate.getCapacity(), table.getCapacity());
        assertEquals(tableToUpdate.getxCoordinate(), table.getxCoordinate());
        assertEquals(tableToUpdate.getyCoordinate(), table.getyCoordinate());
    }

    @Test
    public void update_InvalidId_ThrowsMissingEntityException() {
        Exception exception = assertThrows(MissingEntityException.class, () -> {
            tableService.update(null, -1);
        });

        assertEquals(message, exception.getMessage());
    }

    @Test
    @Rollback
    public void delete_ValidId() throws Exception {
        tableService.delete(1);

        Exception exception = assertThrows(MissingEntityException.class, () -> {
            tableService.findById(1);
        });

        assertEquals(message, exception.getMessage());
    }

    @Test
    public void delete_InvalidId_ThrowsMissingEntityException() {
        Exception exception = assertThrows(MissingEntityException.class, () -> {
            tableService.delete(-1);
        });

        assertEquals(message, exception.getMessage());
    }
}
