package com.kti.restaurant.service.implementation;

import com.kti.restaurant.exception.BadLogicException;
import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.TableReservation;
import com.kti.restaurant.repository.TableReservationRepository;
import com.kti.restaurant.service.contract.ITableReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    	if(!checkTableReservations(-1, tableReservation.getTable().getId(), tableReservation.getDurationStart(), tableReservation.getDurationEnd())) {
    		throw new BadLogicException("Cannot reserve same table in same time period.");
    	}
    	
    	return tableReservationRepository.save(tableReservation);
    }

    @Override
    public TableReservation update(TableReservation tableReservation, Integer id) throws Exception {
        TableReservation tableReservationToUpdate = this.findById(id);

        if(!checkTableReservations(id, tableReservation.getTable().getId(), tableReservation.getDurationStart(), tableReservation.getDurationEnd())) {
    		throw new BadLogicException("Cannot reserve same table in same time period.");
    	}
        
        tableReservationToUpdate.setTable(tableReservation.getTable());
        tableReservationToUpdate.setDurationStart(tableReservation.getDurationStart());
        tableReservationToUpdate.setDurationEnd(tableReservation.getDurationEnd());
        tableReservationRepository.save(tableReservationToUpdate);

        return tableReservationToUpdate;
    }

    @Override
    public void delete(Integer id) throws Exception {
        this.findById(id);
        tableReservationRepository.deleteById(id);
    }
    
    private boolean checkTableReservations(Integer id, Integer tableId, LocalDateTime startTime, LocalDateTime endTime) {
    	LocalDateTime reservationStart = startTime.minusMinutes(15);
    	LocalDateTime reservationEnd = endTime.plusMinutes(15);
        if(id == -1) {
        	if(tableReservationRepository.getTableReservationByDateAndTableId(tableId, reservationStart, reservationEnd).size() != 0) {
           	 return false;
           }
        }else {
        	for(TableReservation reservation : tableReservationRepository.getTableReservationByDateAndTableId(tableId, reservationStart, reservationEnd)) {
        		if( reservation.getId() != id) {
        			return false;
        		}
        	}
        }
    	
        return true;
    }

	@Override
	public Page<TableReservation> findAll(Pageable pageable) {
		return tableReservationRepository.findAll(pageable);
	}
}
