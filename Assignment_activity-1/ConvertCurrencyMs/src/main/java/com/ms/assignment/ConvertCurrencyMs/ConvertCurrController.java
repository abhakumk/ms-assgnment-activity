package com.ms.assignment.ConvertCurrencyMs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ms.assignment.ConvertCurrencyMs.service.ConvertCurrService;

/**
 * @author w2cluster03
 *
 */
@RestController
@RibbonClient(name = "MANAGECURRMS")
@RequestMapping("/ms2")
public class ConvertCurrController {
	
	@Autowired
	ConvertCurrService convertCurrService;
	
	/**
	 * Activity-2: Invoke the MS-1 microservice from MS-2 using Feign client
     *
	 * @param countryCode
	 * @param amount
	 * @return
	 */
	@GetMapping(path = "/conv/FC/{countryCode}/{amount}")
	public ResponseEntity<String> getConvertCurrencyFC(@PathVariable String countryCode, @PathVariable Double amount) {
		try {
			String convertedCurr =  convertCurrService.getConversionFactorFeign(countryCode, amount);
			if(convertedCurr != null) {
				return new ResponseEntity<String>(convertedCurr, HttpStatus.OK);
			} 
				
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.EXPECTATION_FAILED); 
		}
		return new ResponseEntity<String>(HttpStatus.EXPECTATION_FAILED);
	}
	
	/**
	 * Activity-2: Update the Feign client for MS-1 to point to the Discovery Service and ensure MS-2 run successfully
     *
	 * @param countryCode
	 * @param amount
	 * @return
	 */
	@GetMapping(path = "/conv/FCES/{countryCode}/{amount}")
	public ResponseEntity<String> getConvertCurrencyFCES(@PathVariable String countryCode, @PathVariable Double amount) {
		try {
			String convertedCurr =  convertCurrService.getConversionFactorFeignEureka(countryCode, amount);
			if(convertedCurr != null) {
				return new ResponseEntity<String>(convertedCurr, HttpStatus.OK);
			} 
				
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.EXPECTATION_FAILED); 
		}
		return new ResponseEntity<String>(HttpStatus.EXPECTATION_FAILED);
	}
	
	/**
	 * Activity-2: Enable the Hystrix Circuit Breaker for MS-1
     *
	 * @param countryCode
	 * @param amount
	 * @return
	 */
	@GetMapping(path = "/conv/CB/{countryCode}/{amount}")
	public ResponseEntity<String> getConvertCurrencyCB(@PathVariable String countryCode, @PathVariable Double amount) {
		try {
			String convertedCurr =  convertCurrService.getConversionFactorCircuitBreaker(countryCode, amount);
			if(convertedCurr != null) {
				return new ResponseEntity<String>(convertedCurr, HttpStatus.OK);
			} 
				
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.EXPECTATION_FAILED); 
		}
		return new ResponseEntity<String>(HttpStatus.EXPECTATION_FAILED);
	}
	
	/**
	 * Activity-2: Set up 2 instances of MS-1 and load balance the call from MS-2 to MS-1 using Ribbon Load Balancer
     *
	 * @param countryCode
	 * @param amount
	 * @return
	 */
	@GetMapping(path = "/conv/LB/{countryCode}/{amount}")
	public ResponseEntity<String> getConvertCurrencyLB(@PathVariable String countryCode, @PathVariable Double amount) {
		try {
			String convertedCurr =  convertCurrService.getConversionFactorLB(countryCode, amount);
			if(convertedCurr != null) {
				return new ResponseEntity<String>(convertedCurr, HttpStatus.OK);
			} 
				
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.EXPECTATION_FAILED); 
		}
		return new ResponseEntity<String>(HttpStatus.EXPECTATION_FAILED);
	}
	
	/**
	 * Activity-2: Invoke the MS-1 microservice from MS-2 without using Feign client
     *
	 * @param countryCode
	 * @param amount
	 * @return
	 */
	@GetMapping(path = "/conv/{countryCode}/{amount}")
	public ResponseEntity<String> getConvertCurrency(@PathVariable String countryCode, @PathVariable Double amount) {
		try {
			String convertedCurr =  convertCurrService.getConversionFactor(countryCode, amount);
			if(convertedCurr != null) {
				return new ResponseEntity<String>(convertedCurr, HttpStatus.OK);
			} 
				
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.EXPECTATION_FAILED); 
		}
		return new ResponseEntity<String>(HttpStatus.EXPECTATION_FAILED);
	}

}
