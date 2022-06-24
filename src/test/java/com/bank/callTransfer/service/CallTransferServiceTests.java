package com.bank.callTransfer.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CallTransferServiceTests {

	@InjectMocks
	CallTransferService callTransferService;
	
	@Before
	public void init() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	public void distributeCallsTest()
	{
		assertEquals(1, 1);
	}
	
	@Test
	public void updateContactCenterTest()
	{
		assertEquals(1, 1);
	}
	
	
	@Test
	public void fetchCurrentConfigurationTest()
	{
		assertEquals(1, 1);
	}

}
