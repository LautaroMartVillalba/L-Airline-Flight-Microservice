package ar.com.l_airline;

import ar.com.l_airline.services.AirplaneService;
import ar.com.l_airline.services.AirportService;
import ar.com.l_airline.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class LAirlineApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(LAirlineApplication.class, args);
	}
	@Autowired
	TicketService ticketService;
	@Autowired
	AirportService airportService;
	@Autowired
	AirplaneService airplaneService;

	@Override
	public void run(String... args) throws Exception {

	}
}