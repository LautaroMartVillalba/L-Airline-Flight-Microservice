package ar.com.l_airline.domain.ticket;

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
public class TicketCreateDTO {
    private UUID code;
    private int seat;
    private LocalDateTime schedule;
    private UUID airplaneID;
    private UUID originAirportID;
    private UUID destinyAirportID;
}
