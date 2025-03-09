package ar.com.l_airline.services;

import ar.com.l_airline.domain.airport.Airport;
import ar.com.l_airline.domain.airport.AirportDTO;
import ar.com.l_airline.exceptionHandler.custom_exceptions.ExistingObjectException;
import ar.com.l_airline.exceptionHandler.custom_exceptions.MissingDataException;
import ar.com.l_airline.exceptionHandler.custom_exceptions.NotFoundException;
import ar.com.l_airline.repositories.AirportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AirportService {

    private final AirportRepository repository;

    public AirportService(AirportRepository repository) {
        this.repository = repository;
    }

    /**
     * Validates the provided AirportDTO object.
     * Throws a MissingDataException if any of the required fields are invalid or empty.
     *
     * @param dto the AirportDTO object to validate
     * @throws MissingDataException if any validation check fails
     */
    void validateAirport(AirportDTO dto){
        if (dto.getName().isEmpty()){
            throw new MissingDataException("Invalid name exception");
        }
        if (dto.getLatitude().isEmpty()){
            throw new MissingDataException("Invalid latitude exception");
        }
        if (dto.getLongitude().isEmpty()){
            throw new MissingDataException("Invalid longitude exception");
        }
        if (dto.getCity().name().isEmpty()){
            throw new MissingDataException("Invalid city exception");
        }
    }

    /**
     * Creates a new airport based on the provided AirportDTO object.
     * Validates the DTO before creating the airport.
     *
     * @param dto the AirportDTO object containing airport details
     * @return the created Airport object
     * @throws ExistingObjectException if an airport with the same latitude and longitude already exists
     */
    @Transactional
    public Airport createAirport (AirportDTO dto){
        this.validateAirport(dto);

        Optional<Airport> airportInDB = repository.findByLatitudeAndLongitude(dto.getLatitude(), dto.getLongitude());

        if (airportInDB.isPresent()){
            throw new ExistingObjectException();
        }

        Airport airport = Airport.builder()
                                 .name(dto.getName())
                                 .longitude(dto.getLongitude())
                                 .latitude(dto.getLatitude())
                                 .city(dto.getCity()).build();

        repository.save(airport);
        return  airport;
    }

    /**
     * Finds an airport by its ID.
     *
     * @param id the UUID of the airport to find
     * @return the found Airport object
     * @throws MissingDataException if the provided ID is null
     * @throws NotFoundException if no airport is found with the provided ID
     */
    public Airport findById(UUID id){
        if (id == null){
            throw new MissingDataException("No ID received.");
        }

        return repository.findByID(id).orElseThrow(NotFoundException::new);
    }

    /**
     * Finds airports by name containing the specified string.
     *
     * @param name the string to search for in the airport name
     * @return a list of Airport objects matching the search criteria
     * @throws MissingDataException if the provided name string is empty
     * @throws NotFoundException if no airports are found matching the criteria
     */
    public List<Airport> findByName(String name){
        if (name.isEmpty()){
            throw new MissingDataException("No name received.");
        }

        List<Airport> result = repository.findByNameContaining(name);

        if (result.isEmpty()){
            throw new NotFoundException();
        }

        return result;
    }

    /**
     * Finds an airport by its latitude and longitude.
     *
     * @param latitude the latitude of the airport
     * @param longitude the longitude of the airport
     * @return the found Airport object
     * @throws MissingDataException if the provided latitude or longitude is empty
     * @throws NotFoundException if no airport is found with the provided latitude and longitude
     */
    public Airport findByLatitudeAndLongitude(String latitude, String longitude){
        if (latitude.isEmpty() || longitude.isEmpty()){
            throw new MissingDataException("No ID received.");
        }

        return repository.findByLatitudeAndLongitude(latitude, longitude).orElseThrow(NotFoundException::new);
    }

    /**
     * Finds airports by city containing the specified string.
     *
     * @param city the string to search for in the city name
     * @return a list of Airport objects matching the search criteria
     * @throws MissingDataException if the provided city string is empty
     * @throws NotFoundException if no airports are found matching the criteria
     */
    public List<Airport> findByCity(String city){
        if (city.isEmpty()){
            throw new MissingDataException("No ID received.");
        }

        List<Airport> result = repository.findByCityContaining(city);

        if (result.isEmpty()){
            throw new NotFoundException();
        }
        return result;
    }

    /**
     * Updates an existing airport with the provided AirportDTO object.
     *
     * @param dto the AirportDTO object containing updated airport details
     * @param id the UUID of the airport to update
     * @return the updated Airport object
     * @throws MissingDataException if the provided ID is null
     */
    @Transactional
    public Airport updateAirport(AirportDTO dto, UUID id){
        Airport result = this.findById(id);

        if (!(dto.getName() == null)){
            result.setName(dto.getName());
        }
        if (!(dto.getLatitude() == null)){
            result.setLatitude(dto.getLatitude());
        }
        if (!(dto.getLongitude() == null)){
            result.setLongitude(dto.getLongitude());
        }
        if (!(dto.getCity() == null)){
            result.setCity(dto.getCity());
        }

        repository.save(result);
        return result;
    }

    /**
     * Deletes an airport by its ID.
     *
     * @param id the UUID of the airport to delete
     * @throws MissingDataException if the provided ID is null
     */
    @Transactional
    public void deleteAirport(UUID id){
        if (id==null){
            throw new MissingDataException("No ID received.");
        }
        this.findById(id);

        repository.deleteById(id);
    }
}