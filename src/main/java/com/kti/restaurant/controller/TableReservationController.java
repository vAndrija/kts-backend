package com.kti.restaurant.controller;

import com.kti.restaurant.dto.tablereservation.TableReservationDto;
import com.kti.restaurant.mapper.TableReservationMapper;
import com.kti.restaurant.model.TableReservation;
import com.kti.restaurant.service.contract.ITableReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "api/v1/table-reservations")
public class TableReservationController {
    private ITableReservationService tableReservationService;
    private TableReservationMapper tableReservationMapper;

    @Autowired
    TableReservationController(ITableReservationService tableReservationService, TableReservationMapper tableReservationMapper) {
        this.tableReservationService = tableReservationService;
        this.tableReservationMapper = tableReservationMapper;
    }

    @GetMapping("")
    public ResponseEntity<List<TableReservation>> getTableReservations() {
        return new ResponseEntity<>(tableReservationService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TableReservation> getTableReservation(@PathVariable Integer id) throws Exception {
        return new ResponseEntity<>(tableReservationService.findById(id), HttpStatus.OK);
    }


    @PostMapping("")
    public ResponseEntity<TableReservation> createTableReservation(@Valid @RequestBody TableReservationDto tableReservationDto) throws Exception {
        TableReservation tableReservation = tableReservationService.create(tableReservationMapper.fromTableReservationDtoToTableReservation(tableReservationDto));

        if (tableReservation != null) {
            return new ResponseEntity<>(tableReservation, HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TableReservation> updateTableReservation(@Valid @RequestBody TableReservationDto tableReservationDto,
                                                                   @PathVariable Integer id) throws Exception {
        TableReservation tableReservation = tableReservationMapper.fromTableReservationDtoToTableReservation(tableReservationDto);
        tableReservation.setId(id);

        return new ResponseEntity<>(tableReservationService.update(tableReservation),
                HttpStatus.OK);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    public ResponseEntity<?> deleteTableReservation(@PathVariable Integer id) throws Exception {
        tableReservationService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
