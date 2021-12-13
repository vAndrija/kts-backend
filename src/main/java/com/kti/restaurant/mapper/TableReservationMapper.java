package com.kti.restaurant.mapper;

import com.kti.restaurant.dto.tablereservation.TableReservationDto;
import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.RestaurantTable;
import com.kti.restaurant.model.TableReservation;
import com.kti.restaurant.service.contract.IRestaurantTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TableReservationMapper {
    private IRestaurantTableService restaurantTableService;

    @Autowired
    public TableReservationMapper(IRestaurantTableService restaurantTableService) {
        this.restaurantTableService = restaurantTableService;
    }

    public TableReservation fromTableReservationDtoToTableReservation(TableReservationDto tableReservationDto) throws Exception {
        RestaurantTable table = restaurantTableService.findById(tableReservationDto.getTableId());

        if (table == null) {
            throw new MissingEntityException("Table with given id does not exist in the system.");
        }

        return new TableReservation(tableReservationDto.getName(), tableReservationDto.getDurationStart(), table);
    }
    
    public TableReservationDto fromTableReservationToTableReservationDto(TableReservation tableReservation) {
        return new TableReservationDto(tableReservation.getId(), tableReservation.getName(), tableReservation.getTable().getId(), tableReservation.getDurationStart());
    }
}
