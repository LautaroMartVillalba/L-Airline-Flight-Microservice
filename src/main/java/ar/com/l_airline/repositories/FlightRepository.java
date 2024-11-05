package ar.com.l_airline.repositories;

import ar.com.l_airline.enums.AirlineName;
import ar.com.l_airline.entities.Flight;
import ar.com.l_airline.enums.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    List<Flight> findByAirLine(AirlineName airline);
    List<Flight> findByOrigin(City city);
    List<Flight> findByDestiny(City city);
    List<Flight> findByFlightSchedule(LocalDateTime time);
    List<Flight> findByPriceBetween (double min, double max);

}
