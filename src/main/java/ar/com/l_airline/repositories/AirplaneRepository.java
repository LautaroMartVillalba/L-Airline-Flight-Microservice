package ar.com.l_airline.repositories;

import ar.com.l_airline.domain.airplane.Airplane;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AirplaneRepository extends Neo4jRepository<Airplane, UUID> {

    List<Airplane> findByNameContaining(String name);
    List<Airplane> findByAirlineNameContaining(String airline);
    List<Airplane> findByCapacityBetween(int min, int max);
    List<Airplane> findByTankBetween(int min, int max);
    List<Airplane> findByMaxKmDistanceBetween(int min, int max);

}
