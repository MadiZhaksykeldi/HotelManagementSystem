package com.example.demo;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class HotelManagementApplication {
    private static Logger logger = LoggerFactory.getLogger(HotelManagementApplication.class);

    public static void main(String[] args) {
        logger.info("Starting the hotel management application...........");
        SpringApplication.run(HotelManagementApplication.class, args);
        logger.info("Application was started successfully");
    }
}
