package com.n3c3.rentroom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import vn.payos.PayOS;

@SpringBootApplication
public class RentRoomApplication {

	@Value("${rent-room.payos.client-id}")
	private String cliendId;

	@Value("${rent-room.payos.api-key}")
	private String apiKey;

	@Value("${rent-room.payos.checksum-key}")
	private String checksumKey;

	@Bean
	public PayOS payOS() {
		return new PayOS(cliendId, apiKey, checksumKey);
	}

	public static void main(String[] args) {
		SpringApplication.run(RentRoomApplication.class, args);
	}

}
