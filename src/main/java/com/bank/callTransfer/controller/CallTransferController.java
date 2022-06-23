package com.bank.callTransfer.controller;

import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bank.callTransfer.service.CallTransferService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class CallTransferController {
	
	@Autowired
	private CallTransferService callTransferService;
	
	@GetMapping("/transfer")
	@ResponseBody
	public String transfer(@RequestParam(required=true) String reason)
	{
		log.info("Transfer Api Called.....");
		
		try {
			callTransferService.distributeCalls(reason);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return reason;
	}
	
	@PutMapping("/enable")
	@ResponseBody
	public String enable()
	{
		return "enable";
	}
	
	@GetMapping("/viewcallsdistribution")
	@ResponseBody
	public String viewCallsDistribution(@RequestParam(required=false) String date)
	{
		return date;
	}
	
	@GetMapping("/viewcurrentconfiguration")
	@ResponseBody
	public String viewCurrentConfiguration()
	{
		return "viewcurrentconfiguration";
	}
}
