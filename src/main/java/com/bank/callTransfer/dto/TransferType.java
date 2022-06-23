package com.bank.callTransfer.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TransferType {

	private String name;
	private List<Location> location;
	
}
