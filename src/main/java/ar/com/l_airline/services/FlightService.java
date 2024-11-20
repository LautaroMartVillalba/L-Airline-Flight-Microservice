package ar.com.l_airline.services;

import ar.com.l_airline.domain.enums.AirlineName;
import ar.com.l_airline.domain.entities.Flight;
import ar.com.l_airline.domain.dto.FlightDTO;
import ar.com.l_airline.exceptionHandler.custom_exceptions.MissingDataException;
import ar.com.l_airline.repositories.FlightRepository;
import ar.com.l_airline.domain.enums.City;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FlightService {

    private final FlightRepository repository;

    public FlightService(FlightRepository repository) {
        this.repository = repository;
    }

    /**
     * Confirm if any data is empty.
     * @param dto Flight data to creation.
     * @return False if any data are blank or empty. True if not.
     */
    public boolean validateFlight(FlightDTO dto){
        return !dto.getAirLine().name().isBlank()
                && !dto.getOrigin().name().isBlank()
                && !dto.getDestiny().name().isBlank()
                && !dto.getFlightSchedule().isBefore(LocalDateTime.now())
                && dto.getLayover() >= 0;
    }

    /**
     * Check if in the DataBase exists any flight with the given id.
     * @param id Identification number.
     * @return Optional object if any matching record exists. Empty Optional if not.
     */
    public Optional<Flight> findFlightById(Long id){
        if (id == null){
            return Optional.empty();
        }
        return repository.findById(id);
    }

    /**
     * Persist a new record in the DataBase.
     * @param dto Record to insert.
     * @return Null value if can't persist.
     */
    public Flight createFlight(FlightDTO dto) throws MissingDataException {
        if (!validateFlight(dto)){
            throw new MissingDataException();
        }
        Flight flight = Flight.builder().airLine(dto.getAirLine())
                                        .origin(dto.getOrigin())
                                        .destiny(dto.getDestiny())
                                        .flightSchedule(dto.getFlightSchedule())
                                        .price(dto.getPrice())
                                        .layover(dto.getLayover()).build();

        repository.save(flight);
        return flight;
    }

    /**
     * Search one record in the DataBase by his ID and delete if it found a matching record.
     * @param id Identification Number.
     * @return True if it can find and delete the Flight register. False if it can't find one.
     */
    public boolean deleteFlight(Long id){
        Optional<Flight> flightInDB = this.findFlightById(id);

        if (flightInDB.isEmpty()){
            return false;
        }
        repository.deleteById(flightInDB.get().getId());
        return true;
    }

    /**
     * Search some records by AirLine name matching.
     * @param airline AirLine name.
     * @return Flight list if exists in the DataBase. Empty list if not.
     */
    public List<Flight> findByAirLine(AirlineName airline){
        if (airline.name().isBlank()){
            return new ArrayList<>();
        }
        return repository.findByAirLine(airline);
    }

    /**
     * Search some records by city matching.
     * @param origin City name.
     * @return Flight list if exists in the DataBase. Empty list if not.
     */
    public List<Flight> findByOrigin(City origin){
        if (origin.name().isBlank()){
            return new ArrayList<>();
        }
        return repository.findByOrigin(origin);
    }

    /**
     * Search some records by City name matching.
     * @param destiny City name.
     * @return Flight list if exists in the DataBase. Empty list if not.
     */
    public List<Flight> findByDestiny(City destiny){
        if (destiny.name().isBlank()){
            return new ArrayList<>();
        }
        return repository.findByDestiny(destiny);
    }

    //TODO overload the method with less records
    /**
     * Map one LocalDateTime with the records and search some records with matching in the DataBase.
     * @param year Year
     * @param month Month
     * @param day Day
     * @param hour Hour
     * @param minutes Minute
     * @return Flight list if exists some records in the DataBase. Empty list if not.
     */
    public List<Flight> findByFlightSchedule (int year, int month, int day, int hour, int minutes){
        LocalDateTime schedule = LocalDateTime.of(year, month,day, hour, minutes);
        if (schedule.isBefore(LocalDateTime.now())){
            return null;
        }
        return repository.findByFlightSchedule(schedule);
    }

    /**
     * Search records in the DataBase that matching with his price between 'min' and 'max' values.
     * @param min Minimum price value.
     * @param max Maximum price value.
     * @return Flight list if exists some records in the DataBase. Empty list if not.
     */
    public List<Flight> findByPriceBetween (double min, double max){
        if (min < 0 || max < min){
            return null;
        }
        return repository.findByPriceBetween(min, max);
    }

    /**
     * Replace one or more data in the DataBase by one existing record.
     * @param id Identification Number
     * @param dto Data to upload and persist.
     * @return Flight changes info.
     */
    public Flight updateFlight (Long id, FlightDTO dto) {
        Flight findFlight = this.findFlightById(id).orElseThrow(() -> new RuntimeException("Flight not found."));

        if (dto.getAirLine() != null){
            findFlight.setAirLine(dto.getAirLine());
        }
        if (dto.getOrigin() != null){
            findFlight.setOrigin(dto.getOrigin());
        }
        if (dto.getDestiny() != null){
            findFlight.setDestiny(dto.getDestiny());
        }
        if (dto.getFlightSchedule() != null) {
            findFlight.setFlightSchedule(dto.getFlightSchedule());
        }
        if (dto.getLayover() < 0){
            findFlight.setLayover(dto.getLayover());
        }
        if (dto.getPrice() <= 0){
            findFlight.setPrice(dto.getPrice());
        }

        repository.save(findFlight);
        return  findFlight;
    }
}
