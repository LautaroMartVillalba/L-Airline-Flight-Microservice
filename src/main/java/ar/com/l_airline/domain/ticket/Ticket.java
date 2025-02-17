package ar.com.l_airline.domain.ticket;

import ar.com.l_airline.domain.AirlineName;
import ar.com.l_airline.domain.City;
import ar.com.l_airline.domain.airplane.Airplane;
import ar.com.l_airline.domain.airplane.AirplaneName;
import ar.com.l_airline.domain.airport.Airport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Node("flight_ticket")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Ticket {

    @Id
    @GeneratedValue(GeneratedValue.UUIDGenerator.class)
    private UUID id;
    private City origin;
    private City destiny;
    private int seat;
    private double price;
    private LocalDateTime schedule;
    @Property("airline_name")
    private AirlineName airlineName;
    @Relationship(type = "fly_in", direction = Relationship.Direction.OUTGOING)
    private Airplane airplane;
    private UUID airplaneID;
    @Relationship(type = "origin_airport", direction = Relationship.Direction.INCOMING)
    private Airport originAirport;
    private UUID originAirportID;
    @Relationship(type = "destiny_airport", direction = Relationship.Direction.OUTGOING)
    private Airport destinyAirport;
    private UUID destinyAirportID;
}
