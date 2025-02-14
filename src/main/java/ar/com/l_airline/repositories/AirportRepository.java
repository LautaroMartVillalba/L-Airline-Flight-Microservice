package ar.com.l_airline.repositories;

import ar.com.l_airline.domain.airport.Airport;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AirportRepository extends Neo4jRepository<Airport, UUID> {

    List<Airport> findByName(String name);
    Optional<Airport> findByLatitudeAndLongitude(String latitude, String longitude);
    List<Airport> findByCityContaining(String city);

}
