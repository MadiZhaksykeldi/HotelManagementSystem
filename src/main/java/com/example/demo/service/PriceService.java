package com.example.demo.service;

import com.example.demo.model.PriceDetail;
import com.example.demo.response.Response;

public interface PriceService {
    public Response addPriceDetails(PriceDetail priceDetail);
}
