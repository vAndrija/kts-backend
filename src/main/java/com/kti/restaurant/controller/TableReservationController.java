package com.kti.restaurant.controller;

import com.kti.restaurant.dto.tablereservation.TableReservationDto;
import com.kti.restaurant.mapper.TableReservationMapper;
import com.kti.restaurant.model.TableReservation;
import com.kti.restaurant.service.contract.ITableReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<List<TableReservationDto>> getTableReservations() {
    	 return new ResponseEntity<>(tableReservationService.findAll().stream().map(
                 reservation -> this.tableReservationMapper.fromTableReservationToTableReservationDto(reservation)
         ).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TableReservationDto> getTableReservation(@PathVariable Integer id) throws Exception {
        return new ResponseEntity<>(tableReservationMapper.fromTableReservationToTableReservationDto(tableReservationService.findById(id)), HttpStatus.OK);
    }


    @PostMapping("")
    @PreAuthorize("hasAnyRole('WAITER')")
    public ResponseEntity<TableReservationDto> createTableReservation(@Valid @RequestBody TableReservationDto tableReservationDto) throws Exception {
        TableReservation tableReservation = tableReservationService.create(tableReservationMapper.fromTableReservationDtoToTableReservation(tableReservationDto));

        if (tableReservation != null) {
            return new ResponseEntity<>(tableReservationMapper.fromTableReservationToTableReservationDto(tableReservation), HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('WAITER')")
    public ResponseEntity<TableReservationDto> updateTableReservation(@Valid @RequestBody TableReservationDto tableReservationDto,
                                                                   @PathVariable Integer id) throws Exception {
        TableReservation tableReservation = tableReservationMapper.fromTableReservationDtoToTableReservation(tableReservationDto);

        return new ResponseEntity<>(tableReservationMapper.fromTableReservationToTableReservationDto(tableReservationService.update(tableReservation, id)),
                HttpStatus.OK);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    @PreAuthorize("hasAnyRole('WAITER')")
    public ResponseEntity<?> deleteTableReservation(@PathVariable Integer id) throws Exception {
        tableReservationService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
