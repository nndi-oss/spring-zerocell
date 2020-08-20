package com.nndi_tech.spring.web.zerocell.examples;

import com.nndi_tech.spring.web.zerocell.ZerocellRequestBody;
import com.nndi_tech.spring.web.zerocell.examples.model.Customer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Allows users to upload an Excel file and get back the data as a JSON array
 */
@RestController
public class ExcelToJsonController {

    @PostMapping(value="/excel-to-json", produces = "application/json;charset=utf-8")
    public List<Customer> uploadToFilter(@ZerocellRequestBody List<Customer> request) {
        return request;
    }
}
