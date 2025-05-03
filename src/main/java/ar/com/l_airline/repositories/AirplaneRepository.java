package ar.com.l_airline.repositories;

import ar.com.l_airline.domain.airplane.Airplane;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AirplaneRepository extends Neo4jRepository<Airplane, UUID> {

    @Query("MATCH (a:Airplane) WHERE a.code = $code RETURN a")
    Optional<Airplane> findByID(String code);
    @Query("MATCH (a:Airplane) WHERE a.name CONTAINS $name RETURN a")
    List<Airplane> findByNameContaining(String name);
    @Query("MATCH (a:Airplane) WHERE a.airlineName CONTAINS $airline RETURN a")
    List<Airplane> findByAirlineNameContaining(String airline);
    @Query("MATCH (a:Airplane) WHERE a.capacity >= $min AND a.capacity <= $max RETURN a")
    List<Airplane> findByCapacityBetween(int min, int max);
    @Query("MATCH (a:Airplane) WHERE a.tank >= $min AND a.tank <= $max RETURN a")
    List<Airplane> findByTankBetween(int min, int max);
    @Query("MATCH (a:Airplane) WHERE a.maxKmDistance >= $min AND a.maxKmDistance <= $max RETURN a")
    List<Airplane> findByMaxKmDistanceBetween(int min, int max);

}
