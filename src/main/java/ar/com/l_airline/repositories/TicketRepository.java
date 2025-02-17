package ar.com.l_airline.repositories;

import ar.com.l_airline.domain.City;
import ar.com.l_airline.domain.ticket.Ticket;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TicketRepository extends Neo4jRepository<Ticket, UUID> {

    List<Ticket> findByOriginContaining(String city);
    List<Ticket> findByDestinyContaining(String city);
    List<Ticket> findByPriceBetween(double min, double max);
    List<Ticket> findByScheduleBetween(LocalDateTime from, LocalDateTime to);
    List<Ticket> findByAirlineNameContaining(String airline);
    List<Ticket> findByAirplane(UUID airplaneID);
    List<Ticket> findByOriginAirportID(UUID airportID);
    List<Ticket> findByDestinyAirportID(UUID airportID);
    Optional<Ticket> findByAirplaneIDAndSeat(UUID airplaneID, int seat);
}
