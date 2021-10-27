package com.kti.restaurant;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping(value="/")
public class Test {

    @GetMapping("")
    public String hello(){
        return "Gdje ste sokolovii";
    }
}
