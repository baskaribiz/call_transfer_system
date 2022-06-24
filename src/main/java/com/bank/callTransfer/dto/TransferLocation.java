package com.bank.callTransfer.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@XmlRootElement(name="transferLocations")
public class TransferLocation {
	
	private List<TransferType> transferType;

}
