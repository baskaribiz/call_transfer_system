package com.bank.callTransfer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.callTransfer.dao.entity.CallDistribution;
import com.bank.callTransfer.repository.CallDistributionRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ViewCallsDistributionService {

	@Autowired
	private CallDistributionRepository callDistributionRepository;
	
	public List<CallDistribution> fetchCallDistribution()
	{
		log.info("Fetches all the data from call distribution table.");
		return callDistributionRepository.findAll();
	}
}
