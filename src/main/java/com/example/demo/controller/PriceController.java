package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.PriceDetail;
import com.example.demo.response.Response;
import com.example.demo.response.ResponseBuilder;
import com.example.demo.service.PriceService;

@RestController
@RequestMapping("/prices")
public class PriceController {
    @Autowired
    private PriceService priceService;

    @PostMapping("/addPrice")
    public Response addPriceDetails(@Valid @RequestBody PriceDetail priceDetail, Errors errors) {
        List<String> errorMessage = new ArrayList<>();
        if (errors.hasErrors()) {
            for (ObjectError objectError : errors.getAllErrors()) {
                errorMessage.add(objectError.getDefaultMessage());
            }
            return ResponseBuilder.buildFailureResponse(errorMessage);
        }
        return priceService.addPriceDetails(priceDetail);
    }

}