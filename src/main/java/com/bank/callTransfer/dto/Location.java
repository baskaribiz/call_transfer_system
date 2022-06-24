package com.bank.callTransfer.dto;

import javax.xml.bind.annotation.XmlAttribute;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Location {
	
	private String name;
	private String number;
	private Long weight;
	private boolean enabled;
	
	@XmlAttribute
	public String getName() {
		return name;
	}	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getNumber() {
		return number;
	}	
	public void setNumber(String number) {
		this.number = number;
	}
	
	@JsonIgnore
	public Long getWeight() {
		return weight;
	}
	public void setWeight(Long weight) {
		this.weight = weight;
	}
	
	@JsonIgnore
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
