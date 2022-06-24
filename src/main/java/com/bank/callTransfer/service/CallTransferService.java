package com.bank.callTransfer.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.callTransfer.dao.entity.CallDistribution;
import com.bank.callTransfer.dto.Location;
import com.bank.callTransfer.dto.LocationName;
import com.bank.callTransfer.dto.TransferLocation;
import com.bank.callTransfer.dto.TransferType;
import com.bank.callTransfer.exception.LocationNotFoundException;
import com.bank.callTransfer.exception.ReasonNotFoundException;
import com.bank.callTransfer.repository.CallDistributionRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CallTransferService {
	
	public static final String CONFIG_FILE = "configuration.xml";

	@Autowired
	private CallDistributionRepository callDistributionRepository;
	
	private TransferLocation readConfigXml(File file) throws JAXBException
	{
		log.info("unmarshalling Implementation.");
		TransferLocation transferLocation = null;
		JAXBContext context;
		
		context = JAXBContext.newInstance(TransferLocation.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        transferLocation = (TransferLocation) unmarshaller.unmarshal(file);
 
        return transferLocation;
	}
	
	private void writeToConfigXml(File file, TransferLocation transferLocation) throws JAXBException, IOException
	{
		log.info("Marshalling Implementation.");
		JAXBContext context;
        
		BufferedWriter writer = null;
        writer = new BufferedWriter(new FileWriter(file));
        context = JAXBContext.newInstance(TransferLocation.class);
        
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(transferLocation, writer);
        
        writer.close();
	}
	
	/**
	 * Logic to select a contact centre to distribute calls.
	 * 
	 * @param locations
	 * @param reason
	 * @return Location
	 */
	private Location chooseContactCentre(List<Location> locations, String reason)
	{
		HashMap<String, Long> locationCapacity = new HashMap<String, Long>();
		
		// Adding all the enabled locations weight together and calculates overall capacity
		Long overallCapacity = locations.stream().mapToLong(Location::getWeight).sum();
		
		// Number of previous calls fetched from the call distribution table
		List<CallDistribution> callDistributions = callDistributionRepository.findByReason(reason);
		
		//Creating a HashMap with location and its occupation percentage
		for(Location item : locations){
			
			//Each location capacity percentage calculated using location weight and sum of all enabled locations weight
			double individualLocationCapacityPercentage = (double)item.getWeight()/overallCapacity * 100;
			
			//Number of calls distributed to a particular location fetched
			Long occupation = callDistributions.stream().filter(location -> location.getLocationName().equals(item.getName())).count();
			//Each location's number of distributed calls and sum of all the location's number of distributed calls used to calculate occupation percentage
			double individualLocationOccupationPercentage = (double)occupation/callDistributions.size() * 100;
			
			//Available capacity is calculated by subtracting capacity with current occupation and creates a HashMap
			locationCapacity.put(item.getName(),  Double.valueOf(individualLocationCapacityPercentage).longValue() - Double.valueOf(individualLocationOccupationPercentage).longValue());
        }		
		
		//Location which has more availability found and chosen for call distribution
		String selectedLocationName = Collections.max(locationCapacity.entrySet(), Map.Entry.comparingByValue()).getKey();
		Location selectedLocation = locations.stream()
												.filter(location -> location.getName().equals(selectedLocationName))
												.findAny()
												.orElse(null);

		return selectedLocation;
	}
	
	public Location distributeCalls(String reason) throws JAXBException, ReasonNotFoundException
	{
		log.info("Step 1: Reads the config xml file");
		TransferLocation transferLocation = readConfigXml(new File(CONFIG_FILE));
		
		log.info("Step 2: Gets the location for a given reason which are enabled");
		TransferType transferType = transferLocation.getTransferType().stream()
													  .filter(type -> reason.equals(type.getName()))
													  .findAny()
													  .orElse(null);
		
		if(null == transferType)
			throw new ReasonNotFoundException();
		
		List<Location> locations = transferType.getLocation().stream().filter(location -> location.isEnabled())
											.sorted(Comparator.comparing(Location::getWeight).reversed())
											.collect(Collectors.toList());

		log.info("Step 3: Finds a contact center to distribute call");
		Location selectedLocation = chooseContactCentre(locations, reason);
		
		log.info("Step 4: stores the call detail to table");
		Date date = new Date();
		Instant instant = date.toInstant();
		Date currentDateTime = Date.from(instant);
		
		CallDistribution callDistribution = new CallDistribution();
		callDistribution.setDateTime(currentDateTime)
						.setLocationName(selectedLocation.getName())
						.setReason(reason)
						.setTelephoneNumber(selectedLocation.getNumber());		
		
		callDistributionRepository.save(callDistribution);
		
		log.info("Step 5: returns the selected location name and telephone number");
		return selectedLocation;
	}
	
	public TransferLocation updateContactCenter(LocationName location) throws JAXBException, IOException, LocationNotFoundException
	{
		Location selectedlocation = null;
		boolean updatedFlag = false;
		
		log.info("Reads xml and unmarshalling.");
		TransferLocation transferLocation = readConfigXml(new File(CONFIG_FILE));
		
		log.info("Iterate thru reasons and find location");
		for(TransferType transferType : transferLocation.getTransferType()){
			selectedlocation = transferType.getLocation().stream()
								.filter(loc -> loc.getName().equals(location.getName()))
								.findAny()
								.orElse(null);
						
			if(null != selectedlocation)
			{
				log.info("selectedlocation is not null");
				selectedlocation.setEnabled(location.isEnabled());
				log.info("Enable changed....");
				
				updatedFlag = true;
			}
			else
			{
				log.info("selectedlocation is null");
			}
        }
		
		log.info("Throw Exception Location Not Found");
		if(!updatedFlag)
			throw new LocationNotFoundException();
		
		log.info("Writes back to config xml file");
		writeToConfigXml(new File("configuration.xml"), transferLocation);
		
		return transferLocation;
	}
	
	public TransferLocation fetchCurrentConfiguration() throws JAXBException
	{
		log.info("UnMarshelling XML File");
		TransferLocation transferLocation = readConfigXml(new File(CONFIG_FILE));
		return transferLocation;
	}
}
