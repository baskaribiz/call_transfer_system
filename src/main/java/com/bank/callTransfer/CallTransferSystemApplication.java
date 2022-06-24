package com.bank.callTransfer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Call Transfer API", version = "1.0", description = "Call Transfer System"))
public class CallTransferSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(CallTransferSystemApplication.class, args);
	}

}
