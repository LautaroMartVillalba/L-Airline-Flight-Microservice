package ar.com.l_airline.domain.airplane;

import ar.com.l_airline.domain.AirlineName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.UUID;

@Node
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Airplane {

    @Id
    @GeneratedValue(GeneratedValue.UUIDGenerator.class)
    private UUID id;
    private AirplaneName name;
    private AirlineName airlineName;
    private int capacity;
    private int tank;
    private int maxKmDistance;
}
