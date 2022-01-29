package com.kti.restaurant.controller;


import com.kti.restaurant.dto.priceitem.PriceItemDto;
import com.kti.restaurant.mapper.PriceItemMapper;
import com.kti.restaurant.model.PriceItem;
import com.kti.restaurant.service.contract.IPriceItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "api/v1/price-items")
public class PriceItemController {
    private IPriceItemService priceItemService;
    private PriceItemMapper priceItemMapper;

    @Autowired
    PriceItemController(IPriceItemService priceItemService, PriceItemMapper priceItemMapper) {
        this.priceItemService = priceItemService;
        this.priceItemMapper = priceItemMapper;
    }

    @PostMapping("")
    @PreAuthorize("hasAnyRole('MANAGER')")
    public ResponseEntity<?> createPriceItem(@Valid @RequestBody PriceItemDto priceItemDto) throws Exception {
        PriceItem priceItem = priceItemService.create(priceItemMapper.fromPriceItemDtoToPriceItem(priceItemDto));
        return new ResponseEntity<>(priceItem, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PriceItem> getPriceItemById(@PathVariable Integer id) throws Exception {
        return new ResponseEntity<>(priceItemService.findById(id), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<List<PriceItem>> gePriceItems() {
        return new ResponseEntity<>(priceItemService.findAll(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER')")
    public ResponseEntity<?> updatePriceItem(@Valid @RequestBody PriceItemDto priceItemDto, @PathVariable Integer id) throws Exception {
        return new ResponseEntity<>(priceItemService.update
                (priceItemMapper.fromPriceItemDtoToPriceItem(priceItemDto), id),
                HttpStatus.OK);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    @PreAuthorize("hasAnyRole('MANAGER')")
    public ResponseEntity<?> deletePriceItem(@PathVariable Integer id) throws Exception {
        priceItemService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
