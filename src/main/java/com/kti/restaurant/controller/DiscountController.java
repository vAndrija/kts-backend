package com.kti.restaurant.controller;

import com.kti.restaurant.dto.discount.DiscountDto;
import com.kti.restaurant.dto.menu.MenuDto;
import com.kti.restaurant.mapper.DiscountMapper;
import com.kti.restaurant.model.Discount;
import com.kti.restaurant.model.Menu;
import com.kti.restaurant.service.contract.IDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping(value = "api/v1/discount")
public class DiscountController {
    private IDiscountService discountService;
    private DiscountMapper discountMapper;

    @Autowired
    public DiscountController(IDiscountService discountService, DiscountMapper discountMapper) {
        this.discountService = discountService;
        this.discountMapper = discountMapper;
    }

    @PostMapping("")
    public ResponseEntity<?> createDiscount(@Valid @RequestBody DiscountDto discountDto) throws Exception {
        Discount discount = discountService.create(discountMapper.fromDiscountDtoToDiscount(discountDto));

        if(discount != null) {
            return new ResponseEntity<>(discount, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiscountDto> getDiscountById(@PathVariable Integer id) throws Exception {
        return new ResponseEntity<DiscountDto>(discountMapper.fromDiscountToDiscountDto(discountService.findById(id)), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<List<DiscountDto>> getDiscounts() {
        return new ResponseEntity<>(discountService.findAll().stream().map(
                discount -> this.discountMapper.fromDiscountToDiscountDto(discount)
        ).collect(Collectors.toList()), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Discount> updateDiscount(@Valid @RequestBody DiscountDto discountDto, @PathVariable Integer id) throws Exception {
        return new ResponseEntity<Discount>(discountService.update(discountMapper.fromDiscountDtoToDiscount(discountDto), id),
                HttpStatus.OK);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    public ResponseEntity<?> deleteDiscount(@PathVariable Integer id) throws Exception {
        discountService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
