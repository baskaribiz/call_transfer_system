package com.bank.callTransfer.controller;

import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bank.callTransfer.dao.entity.CallDistribution;
import com.bank.callTransfer.dto.Location;
import com.bank.callTransfer.dto.LocationName;
import com.bank.callTransfer.dto.Result;
import com.bank.callTransfer.dto.Status;
import com.bank.callTransfer.dto.TransferLocation;
import com.bank.callTransfer.exception.GenericException;
import com.bank.callTransfer.exception.LocationNotFoundException;
import com.bank.callTransfer.exception.ReasonNotFoundException;
import com.bank.callTransfer.service.CallTransferService;
import com.bank.callTransfer.service.ViewCallsDistributionService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class CallTransferController {
	
	public static final String SUCCESS = "Success";
	
	@Autowired
	private CallTransferService callTransferService;
	
	@Autowired
	private ViewCallsDistributionService viewCallsDistributionService;
	
	@GetMapping("/transfer")
	@ResponseBody
	public ResponseEntity<?> transfer(@RequestParam(required=true) String reason) throws Exception
	{
		log.info("Transfer Api Called.....");
		
		Result result = new Result();
		Status status = new Status();
		result.setStatus(status);
		
		Location location = null;
		
		try {
			location = callTransferService.distributeCalls(reason);
			
			status.setCode(HttpStatus.OK.value());
			status.setMessage(SUCCESS);
			
			result.setData(location);
			
			return ResponseEntity.ok(result);
			
		} catch (JAXBException e) {
			log.error(e.getMessage());
			throw new GenericException(e.toString());
		}
		catch (ReasonNotFoundException e)
		{
			log.error(e.getMessage());
			status.setCode(HttpStatus.BAD_REQUEST.value());
			status.setMessage(e.getMessage());
			
			result.setData(null);
			
			return ResponseEntity.badRequest().body(result);
		}
	}
	
	
	/**
	 * This API takes a location name as input and enable/disable it from the distribution.
	 * @param location
	 * @return
	 * @throws Exception 
	 */
	@PutMapping("/enable")
	@ResponseBody
	public ResponseEntity<?> enable(@RequestBody LocationName location) throws Exception
	{
		log.info("enable api called...");
		log.info(location.toString());
		
		Result result = new Result();
		Status status = new Status();
		result.setStatus(status);
		
		TransferLocation transferLocation = null;
		
		try {
			transferLocation = callTransferService.updateContactCenter(location);
			
			status.setCode(HttpStatus.OK.value());
			status.setMessage(SUCCESS);
			
			result.setData(transferLocation);
			
			return ResponseEntity.ok(result);
			
		} catch (JAXBException | IOException e) {
			log.error(e.getMessage());
			throw new GenericException(e.toString());
		}
		catch (LocationNotFoundException e)
		{
			log.error(e.getMessage());
			status.setCode(HttpStatus.BAD_REQUEST.value());
			status.setMessage(e.getMessage());
			
			result.setData(null);
			
			return ResponseEntity.badRequest().body(result);
		}
	}
	
	
	/**
	 * This API displays a view of the distribution of calls to date (real time data)
	 * @param date (Optional)
	 * @return call distribution list
	 */
	@GetMapping("/viewcallsdistribution")
	@ResponseBody
	public ResponseEntity<?> viewCallsDistribution(@RequestParam(required=false) String date) throws Exception
	{
		log.info("viewCallsDistribution Api Called.....");
		
		List<CallDistribution> callDistributions = null;
		
		Result result = new Result();
		Status status = new Status();
		result.setStatus(status);
		
		status.setCode(HttpStatus.OK.value());
		status.setMessage(SUCCESS);
		
		callDistributions = viewCallsDistributionService.fetchCallDistribution();
		
		result.setData(callDistributions);
		
		return ResponseEntity.ok(result);
	}
	
	/**
	 * This API displays a view of the current live configuration.
	 * @return current xml configuration 
	 * @throws GenericException 
	 */
	@RequestMapping(value = "/viewcurrentconfiguration", method = RequestMethod.GET, produces = { "application/xml", "text/xml" })
	@ResponseBody
	public ResponseEntity<?> viewCurrentConfiguration() throws GenericException
	{
		log.info("viewcurrentconfiguration Api Called.....");
		
		TransferLocation transferLocation = null;
		
		try {
			transferLocation = callTransferService.fetchCurrentConfiguration();
		} catch (JAXBException e) {
			log.error(e.getMessage());
			throw new GenericException(e.toString());
		}
		return ResponseEntity.ok(transferLocation);
	}
}
