package ar.com.l_airline.domain.ticket;

import ar.com.l_airline.domain.AirlineName;
import ar.com.l_airline.domain.City;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TicketRetrieveDTO {
    private UUID code;
    private City origin;
    private City destiny;
    private double price;
    private int seat;
    private LocalDateTime schedule;
    private AirlineName airlineName;
    private UUID airplaneID;
    private UUID originAirportID;
    private UUID destinyAirportID;
}
