package ar.com.l_airline.repositories;

import ar.com.l_airline.domain.ticket.Ticket;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@EnableNeo4jRepositories
public interface TicketRepository extends Neo4jRepository<Ticket, UUID> {

    @Query("MATCH (a:flight_ticket) WHERE a.id CONTAINS id RETURN a")
    Optional<Ticket> findByID(UUID id);
    @Query("MATCH (a:flight_ticket) WHERE a.origin CONTAINS $city RETURN a")
    List<Ticket> findByOriginContaining(String city);
    @Query("MATCH (a:flight_ticket) WHERE a.destiny CONTAINS $city RETURN a")
    List<Ticket> findByDestinyContaining(String city);
    @Query("MATCH (a:flight_ticket) WHERE a.price >= $min AND a.price <= $max RETURN a")
    List<Ticket> findByPriceBetween(double min, double max);
    @Query("MATCH (a:flight_ticket) WHERE a.schedule >= $from AND a.schedule <= $to RETURN a")
    List<Ticket> findByScheduleBetween(LocalDateTime from, LocalDateTime to);
    @Query("MATCH (a:flight_ticket) WHERE a.airline_name CONTAINS $airline RETURN a")
    List<Ticket> findByAirlineNameContaining(String airline);
    @Query("MATCH (a:flight_ticket {airplaneID: $airplaneID}) RETURN a")
    List<Ticket> findByAirplane(UUID airplaneID);
    @Query("MATCH (a:flight_ticket {originAirportID: $airportID}) RETURN a")
    List<Ticket> findByOriginAirportID(UUID airportID);
    @Query("MATCH (a:flight_ticket {destinyAirportID: $airportID}) RETURN a")
    List<Ticket> findByDestinyAirportID(UUID airportID);
    @Query("MATCH (a:flight_ticket) WHERE a.airplaneID=$airplaneID AND a.seat=$seat RETURN a")
    Optional<Ticket> findByAirplaneIDAndSeat(UUID airplaneID, int seat);
}
