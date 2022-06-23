package com.bank.callTransfer.service;

import java.io.File;
import java.time.Instant;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.callTransfer.dao.entity.CallDistribution;
import com.bank.callTransfer.dto.Location;
import com.bank.callTransfer.dto.TransferLocation;
import com.bank.callTransfer.dto.TransferType;
import com.bank.callTransfer.repository.CallDistributionRepository;

@Service
public class CallTransferService {

	@Autowired
	private CallDistributionRepository callDistributionRepository;
	
	public TransferLocation readConfigXml(File file) throws JAXBException
	{
		TransferLocation transferLocation = null;
		JAXBContext context;
		
		context = JAXBContext.newInstance(TransferLocation.class);
        Unmarshaller um = context.createUnmarshaller();
        transferLocation = (TransferLocation) um.unmarshal(file);
 
        return transferLocation;
	}
	
	public void distributeCalls(String reason) throws JAXBException
	{
		HashMap<String, Long> locationCapacity = new HashMap<String, Long>();
				
		TransferLocation transferLocation = readConfigXml(new File("configuration.xml"));
		
		TransferType transferType = transferLocation.getTransferType().stream()
													  .filter(type -> reason.equals(type.getName()))
													  .findAny()
													  .orElse(null);
		
		List<Location> locations = transferType.getLocation().stream().filter(location -> location.isEnabled())
											.sorted(Comparator.comparing(Location::getWeight).reversed())
											.collect(Collectors.toList());
		
		Long overallCapacity = locations.stream().mapToLong(Location::getWeight).sum();
		
		List<CallDistribution> callDistributions = callDistributionRepository.findByReason(reason);
		
		for(Location item : locations){
			
			double div = (double)item.getWeight()/overallCapacity * 100;
			Long occupation = callDistributions.stream().filter(location -> location.getLocationName().equals(item.getName())).count();
			
			double div2 = (double)occupation/callDistributions.size() * 100;
			
			locationCapacity.put(item.getName(), Double.valueOf(div2).longValue() - Double.valueOf(div).longValue());
					
        }		
		
		Date date = new Date();
		Instant instant = date.toInstant();
		Date currentDateTime = Date.from(instant);
		
		CallDistribution callDistribution = new CallDistribution();
		callDistribution.setDateTime(currentDateTime)
						.setLocationName("London")
						.setReason("lost and stolen")
						.setTelephoneNumber("0208705040");		
		
		callDistributionRepository.save(callDistribution);
	}
}
