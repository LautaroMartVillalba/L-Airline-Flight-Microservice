package ar.com.l_airline.domain.airport;

import ar.com.l_airline.domain.City;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AirportDTO {
    private String name;
    private String latitude;
    private String longitude;
    private City city;
}
