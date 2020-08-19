package com.nndi_tech.spring.web.zerocell.examples;

import com.nndi_tech.spring.web.zerocell.ZerocellRequestBody;
import com.nndi_tech.spring.web.zerocell.examples.Customer;
import net.cinnom.nanocuckoo.NanoCuckooFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Allows users to upload an Excel file whose records are stored in a CuckooFilter
 * that's used to query whether record exists or not, by a unique key ofcourse.
 */
@RestController
public class CuckooFilterController {
    // Use Builder to create a NanoCuckooFilter. Only required parameter is capacity.
    final NanoCuckooFilter cuckooFilter = new NanoCuckooFilter.Builder( 1024 )
        .withCountingEnabled( true ) // Enable counting
        .build();
    final ConcurrentHashMap<String, Customer> customerDb = new ConcurrentHashMap<>();
    @PostMapping("/bloom/upload")
    public void uploadToFilter(@ZerocellRequestBody List<Customer> request) {
        request.forEach(customer-> {
            customerDb.put(customer.getAccountNumber(), customer);
            cuckooFilter.insert(customer.getAccountNumber());
        });
    }

    @GetMapping("/bloom/query")
    public Customer uploadToFilter(@RequestParam(name="s") String custAccountNumber) {
        if (cuckooFilter.contains(custAccountNumber)) {
            return customerDb.get(custAccountNumber);
        }
        return null;
    }
}
