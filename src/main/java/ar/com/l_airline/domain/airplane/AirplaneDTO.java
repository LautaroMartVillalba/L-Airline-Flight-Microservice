package ar.com.l_airline.domain.airplane;

import ar.com.l_airline.domain.AirlineName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AirplaneDTO {
    private AirplaneName name;
    private AirlineName airlineName;
    private int capacity;
    private int tank;
    private int maxKmDistance;
}
