package com.kti.restaurant.controller;


import com.kti.restaurant.dto.priceitem.CreatePriceItemDto;
import com.kti.restaurant.dto.priceitem.UpdatePriceItemDto;
import com.kti.restaurant.mapper.PriceItemMapper;
import com.kti.restaurant.model.PriceItem;
import com.kti.restaurant.service.contract.IPriceItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> createPriceItem(@Valid @RequestBody CreatePriceItemDto priceItemDto) {
        PriceItem priceItem = priceItemService.create(priceItemMapper.fromCreatePriceItemDtoToPriceItem(priceItemDto));

        if(priceItem != null) {
            return new ResponseEntity<>(priceItem, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PriceItem> getPriceItemById(@PathVariable Integer id) {
        return new ResponseEntity<>(priceItemService.findById(id), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<List<PriceItem>> gePriceItems() {
        return new ResponseEntity<>((List<PriceItem>) priceItemService.findAll(), HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<?> updatePriceItem(@Valid @RequestBody UpdatePriceItemDto updatePriceItemDto) throws Exception {
        return new ResponseEntity<>(priceItemService.update
                (priceItemMapper.fromUpdatePriceItemDtoToPriceItem(updatePriceItemDto)),
                HttpStatus.OK);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    public ResponseEntity<?> deletePriceItem(@PathVariable Integer id) {
        priceItemService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
