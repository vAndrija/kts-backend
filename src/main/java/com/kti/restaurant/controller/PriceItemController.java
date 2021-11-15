package com.kti.restaurant.controller;


import com.kti.restaurant.dto.priceitem.PriceItemDto;
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
    public ResponseEntity<?> createPriceItem(@Valid @RequestBody PriceItemDto priceItemDto) throws Exception {
        PriceItem priceItem = priceItemService.create(priceItemMapper.fromPriceItemDtoToPriceItem(priceItemDto));

        if(priceItem != null) {
            return new ResponseEntity<>(priceItem, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PriceItem> getPriceItemById(@PathVariable Integer id) throws Exception {
        return new ResponseEntity<>(priceItemService.findById(id), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<List<PriceItem>> gePriceItems() {
        return new ResponseEntity<>((List<PriceItem>) priceItemService.findAll(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePriceItem(@Valid @RequestBody PriceItemDto priceItemDto, @PathVariable Integer id) throws Exception {
        return new ResponseEntity<>(priceItemService.update
                (priceItemMapper.fromPriceItemDtoToPriceItem(priceItemDto), id),
                HttpStatus.OK);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    public ResponseEntity<?> deletePriceItem(@PathVariable Integer id) throws Exception {
        priceItemService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
