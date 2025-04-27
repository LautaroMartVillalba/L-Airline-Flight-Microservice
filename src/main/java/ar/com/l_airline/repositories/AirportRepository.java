package ar.com.l_airline.repositories;

import ar.com.l_airline.domain.airport.Airport;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AirportRepository extends Neo4jRepository<Airport, UUID> {

    @Query("MATCH (a:Airport {code: $code}) RETURN a")
    Optional<Airport> findByID(UUID code);
    @Query("MATCH (a: Airport) WHERE a.name CONTAINS $name RETURN a")
    List<Airport> findByNameContaining(String name);
    @Query("MATCH (a: Airport) WHERE a.latitude=$latitude AND a.longitude=$longitude RETURN a")
    Optional<Airport> findByLatitudeAndLongitude(String latitude, String longitude);
    @Query("MATCH (a: Airport) WHERE a.city CONTAINS $city RETURN a")
    List<Airport> findByCityContaining(String city);

}
