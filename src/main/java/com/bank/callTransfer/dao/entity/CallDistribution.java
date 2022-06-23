package com.bank.callTransfer.dao.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Entity
@Table(name="TBL_CALL_DISTRIBUTIONS")
public class CallDistribution {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateTime;
	
	private String reason;
	private String locationName;
	private String telephoneNumber;
	
	public CallDistribution setId(long id) {
		this.id = id;
		return this;
	}
	public CallDistribution setDateTime(Date dateTime) {
		this.dateTime = dateTime;
		return this;
	}
	public CallDistribution setReason(String reason) {
		this.reason = reason;
		return this;
	}
	public CallDistribution setLocationName(String locationName) {
		this.locationName = locationName;
		return this;
	}
	public CallDistribution setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
		return this;
	}

}
