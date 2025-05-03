package ar.com.l_airline.domain.airport;

import ar.com.l_airline.domain.City;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AirportDTO {
    private UUID code;
    private String name;
    private String latitude;
    private String longitude;
    private City city;
}
