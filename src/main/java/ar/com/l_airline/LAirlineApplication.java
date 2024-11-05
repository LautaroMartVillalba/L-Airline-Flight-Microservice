package ar.com.l_airline;

import ar.com.l_airline.services.FlightService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LAirlineApplication {

	public static void main(String[] args) {
		SpringApplication.run(LAirlineApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(FlightService flightService){

		return args -> {
		};

	}

}
