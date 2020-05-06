package com.ms.assignment.ConvertCurrencyMs.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ms.assignment.model.Currency;

@FeignClient(name = "getConversionFactor", url = "http://localhost:9001")
public interface ManageConvFactorClient {
	
    @RequestMapping(method = RequestMethod.GET, value = "/ms1/curr/{countryCode}")
    Currency getConversionFactor(@PathVariable("countryCode") String countryCode); 

//    @RequestMapping(method = RequestMethod.POST, value = "/stores/{storeId}", consumes = "application/json")
//    Store update(@PathVariable("storeId") Long storeId, Store store);
}
