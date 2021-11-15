package com.kti.restaurant.service.implementation;

import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.TableReservation;
import com.kti.restaurant.repository.TableReservationRepository;
import com.kti.restaurant.service.contract.ITableReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return tableReservationRepository.findById(id).orElse(null);
    }

    @Override
    public TableReservation create(TableReservation tableReservation) throws Exception {
        return tableReservationRepository.save(tableReservation);
    }

    @Override
    public TableReservation update(TableReservation tableReservation) throws Exception {
        TableReservation tableReservationToUpdate = tableReservationRepository.findById(tableReservation.getId()).orElse(null);

        if (tableReservationToUpdate == null) {
            throw new MissingEntityException("Table reservation with given id does not exist in the system");
        }

        tableReservationToUpdate.setTable(tableReservation.getTable());
        tableReservationToUpdate.setDurationStart(tableReservation.getDurationStart());

        tableReservationRepository.save(tableReservationToUpdate);

        return tableReservationToUpdate;
    }

    @Override
    public void delete(Integer id) throws Exception {
        tableReservationRepository.deleteById(id);
    }
}
