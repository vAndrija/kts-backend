package com.kti.restaurant.controller;

import com.kti.restaurant.dto.bartender.BartenderCreateDto;
import com.kti.restaurant.dto.bartender.BartenderDto;
import com.kti.restaurant.dto.bartender.BartenderUpdateDto;
import com.kti.restaurant.mapper.BartenderMapper;
import com.kti.restaurant.model.Bartender;
import com.kti.restaurant.service.contract.IBartenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping(value="/api/v1/bartender")
public class BartenderController {

    private IBartenderService bartenderService;
    private BartenderMapper bartenderMapper;

    @Autowired
    public BartenderController(IBartenderService bartenderService, BartenderMapper bartenderMapper){
        this.bartenderService = bartenderService;
        this.bartenderMapper = bartenderMapper;
    }

    @PostMapping("")
    public ResponseEntity<?> createBartender(@RequestBody BartenderCreateDto bartenderCreateDto) throws Exception {
        Bartender bartender =  bartenderService.create(bartenderMapper.fromBartenderCreateDtoToBartender(bartenderCreateDto));
        return new ResponseEntity<>(bartenderMapper.fromBartenderToBartenderDto(bartender), HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<?> getBartenders(){
        List<BartenderDto> bartenderDtos = bartenderService.findAll().stream()
                .map(bartender->this.bartenderMapper.fromBartenderToBartenderDto(bartender)).collect(Collectors.toList());
        return new ResponseEntity<>(bartenderDtos,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBartender(@PathVariable Integer id) throws Exception {
        Bartender bartender =  bartenderService.findById(id);
        return new ResponseEntity<>(bartenderMapper.fromBartenderToBartenderDto(bartender), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBartender(@RequestBody BartenderUpdateDto bartenderUpdateDto, @PathVariable Integer id) throws Exception {
        Bartender bartender = bartenderService.update(bartenderMapper.fromBartenderUpdateDtoToBartender(bartenderUpdateDto), id);
        return new ResponseEntity<>(bartenderMapper.fromBartenderToBartenderDto(bartender),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBartender(@PathVariable Integer id) throws Exception {
        bartenderService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
