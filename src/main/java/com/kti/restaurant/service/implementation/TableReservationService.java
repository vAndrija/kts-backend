package com.kti.restaurant.service.implementation;

import com.kti.restaurant.exception.BadLogicException;
import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.TableReservation;
import com.kti.restaurant.repository.TableReservationRepository;
import com.kti.restaurant.service.contract.ITableReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TableReservationService implements ITableReservationService {
    private TableReservationRepository tableReservationRepository;

    @Autowired
    TableReservationService(TableReservationRepository tableReservationRepository) {
        this.tableReservationRepository = tableReservationRepository;
    }

    @Override
    public List<TableReservation> findAll() {
        return tableReservationRepository.findAll();
    }

    @Override
    public TableReservation findById(Integer id) throws Exception {
        TableReservation tableReservation = tableReservationRepository.findById(id).orElse(null);

        if (tableReservation == null) {
            throw new MissingEntityException("Table reservation with given id does not exist in the system.");
        }
        return tableReservation;
    }

    @Override
    public TableReservation create(TableReservation tableReservation) throws Exception {
    	if(!checkTableReservations(tableReservation.getTable().getId(), tableReservation.getDurationStart())) {
    		throw new BadLogicException("Cannot reserve same table in same time period.");
    	}
    	
    	return tableReservationRepository.save(tableReservation);
    }

    @Override
    public TableReservation update(TableReservation tableReservation, Integer id) throws Exception {
        TableReservation tableReservationToUpdate = this.findById(id);

        if(!checkTableReservations(tableReservation.getTable().getId(), tableReservation.getDurationStart())) {
    		throw new BadLogicException("Cannot reserve same table in same time period.");
    	}
        
        tableReservationToUpdate.setTable(tableReservation.getTable());
        tableReservationToUpdate.setDurationStart(tableReservation.getDurationStart());

        tableReservationRepository.save(tableReservationToUpdate);

        return tableReservationToUpdate;
    }

    @Override
    public void delete(Integer id) throws Exception {
        this.findById(id);
        tableReservationRepository.deleteById(id);
    }
    
    private boolean checkTableReservations(Integer id, LocalDateTime checkDate) {
    	LocalDateTime startDay = checkDate.toLocalDate().atStartOfDay();
    	LocalDateTime endDay = checkDate.toLocalDate().plusDays(1).atStartOfDay();
        
    	if(tableReservationRepository.getTableReservationByDateAndTableId(id, startDay, endDay).size() != 0) {
        	 return false;
        }
    	
        return true;
    }
}
