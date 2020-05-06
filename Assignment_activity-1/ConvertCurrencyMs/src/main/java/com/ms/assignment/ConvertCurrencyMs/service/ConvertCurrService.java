package com.ms.assignment.ConvertCurrencyMs.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.ms.assignment.model.Currency;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

/**
 * @author w2cluster03
 *
 */
@Component
public class ConvertCurrService {
	
	private static Logger log = LoggerFactory.getLogger(ConvertCurrService.class);
	
	@Autowired
	LoadBalancerClient lbClient;

	@Bean
	RestTemplate createRestTemplate() {
		return (new RestTemplateBuilder()).build();
	}
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	ManageConvFactorClient manageConvFactorClient;
	
	@Autowired
	ManageConvFactorEurekaClient manageConvFactorEurekaClient;
	
	/**
	 * @param countryCode
	 * @param amount
	 * @return
	 */
	public String getConversionFactorFeign(String countryCode, Double amount) {
		log.info("getConversionFactorFeign()..|"+countryCode+"|"+amount);
		Currency currency = manageConvFactorClient.getConversionFactor(countryCode);
		if(currency != null) {
			Double convertedAmount = amount * currency.getConverFactor(); 
			log.info("getConversionFactorFeign()..result-"+convertedAmount);
			return amount+" Indian currency = "+convertedAmount+" "+countryCode+ " currency.";
		}
		return "Conversion Factor not found! for "+countryCode;
	}
	
	/**
	 * @param countryCode
	 * @param amount
	 * @return
	 */
	public String getConversionFactorFeignEureka(String countryCode, Double amount) {
		log.info("getConversionFactorFeignEureka()..|"+countryCode+"|"+amount);
		Currency currency = manageConvFactorEurekaClient.getConversionFactor(countryCode);
		if(currency != null) {
			Double convertedAmount = amount * currency.getConverFactor(); 
			log.info("getConversionFactorFeignEureka()..result-"+convertedAmount);
			return amount+" Indian currency = "+convertedAmount+" "+countryCode+ " currency.";
		}
		return "Conversion Factor not found! for "+countryCode;
	}
	
	/**
	 * @param countryCode
	 * @param amount
	 * @return
	 */
	@HystrixCommand(fallbackMethod = "getConversionFactorFallback")
	public String getConversionFactorCircuitBreaker(String countryCode, Double amount) {
		log.info("getConversionFactorCircuitBreaker()..|"+countryCode+"|"+amount);
		return getConversionFactor(countryCode, amount);
	}
	
	/**
	 * @param countryCode
	 * @param amount
	 * @return
	 */
	public String getConversionFactorFallback(String countryCode, Double amount) {
		return amount+" Indian currency = "+amount+" Indian currency.";
	}


	/**
	 * @param countryCode
	 * @param amount
	 * @return
	 */
	public String getConversionFactor(String countryCode, Double amount) {
		log.info("getConversionFactor()..|"+countryCode+"|"+amount);
		ResponseEntity<Currency> currencyEntity = restTemplate.exchange("http://localhost:9001/ms1/curr/" +countryCode,
				HttpMethod.GET, null, Currency.class);
		if(currencyEntity != null) {
			Currency currency = currencyEntity.getBody();
			if(currency != null) {
				Double convertedAmount = amount * currency.getConverFactor(); 
				log.info("getConversionFactor()..result-"+convertedAmount);
				return amount+" Indian currency = "+convertedAmount+" "+countryCode+ " currency.";
			}
		}
		return "Conversion Factor not found! for "+countryCode;
	}

	/**
	 * @param countryCode
	 * @param amount
	 * @return
	 */
	public String getConversionFactorLB(String countryCode, Double amount) {
		log.info("getConversionFactorLB()..|"+countryCode+"|"+amount);
		ServiceInstance instance = lbClient.choose("MANAGECURRMS");
		String uri = "http://" + instance.getHost() + ":" + instance.getPort() + "/ms1/curr/"+countryCode;
		log.info("getConversionFactorLB()..|"+uri);
		return getConversionFactorwithURI(countryCode, amount, uri);
	}
	
	/**
	 * @param countryCode
	 * @param amount
	 * @param uri
	 * @return
	 */
	public String getConversionFactorwithURI(String countryCode, Double amount, String uri) {
		log.info("getConversionFactorwithURI()..|"+countryCode+"|"+amount);
		ResponseEntity<Currency> currencyEntity = restTemplate.exchange(uri,
				HttpMethod.GET, null, Currency.class);
		if(currencyEntity != null) {
			Currency currency = currencyEntity.getBody();
			if(currency != null) {
				Double convertedAmount = amount * currency.getConverFactor(); 
				log.info("getConversionFactor()..result-"+convertedAmount);
				return amount+" Indian currency = "+convertedAmount+" "+countryCode+ " currency.";
			}
		}
		return "Conversion Factor not found! for "+countryCode;
	}
	
}
