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
import java.util.UUID;

@Service
public class AirportService {

    private final AirportRepository repository;

    public AirportService(AirportRepository repository) {
        this.repository = repository;
    }

    boolean validateAirport(AirportDTO dto){
        if (dto.getName().isEmpty()){
            throw new RuntimeException("Invalid name exception");
        }
        if (dto.getLatitude().isEmpty()){
            throw new RuntimeException("Invalid latitude exception");
        }
        if (dto.getLongitude().isEmpty()){
            throw new RuntimeException("Invalid longitude exception");
        }
        if (dto.getCity().name().isEmpty()){
            throw new RuntimeException("Invalid city exception");
        }
        return true;
    }

    @Transactional
    public Airport createAirport (AirportDTO dto){
        this.validateAirport(dto);

        repository.findByName(dto.getName()).forEach(airport -> {
            if (dto.getLatitude() == airport.getLatitude() && dto.getLongitude() == airport.getLongitude()){
                throw new ExistingObjectException();
            }
        });

        Airport airport = Airport.builder()
                                 .name(dto.getName())
                                 .longitude(dto.getLongitude())
                                 .latitude(dto.getLatitude())
                                 .city(dto.getCity()).build();

        repository.save(airport);
        return  airport;
    }

    public Airport findById(UUID id){
        if (id == null){
            throw new MissingDataException();
        }

        return repository.findById(id).orElseThrow(NotFoundException::new);
    }

    public List<Airport> findByName(String name){
        if (name.isEmpty()){
            throw new MissingDataException();
        }

        List<Airport> result = repository.findByName(name);

        if (result.isEmpty()){
            throw new NotFoundException();
        }

        return result;
    }

    public Airport findByLatitudeAndLongitude(String latitude, String longitude){
        if (latitude.isEmpty() || longitude.isEmpty()){
            throw new MissingDataException();
        }

        return repository.findByLatitudeAndLongitude(latitude, longitude).orElseThrow(NotFoundException::new);
    }

    public List<Airport> findByCity(String city){
        if (city.isEmpty()){
            throw new MissingDataException();
        }

        List<Airport> result = repository.findByCityContaining(city);

        if (result.isEmpty()){
            throw new NotFoundException();
        }
        return result;
    }

    @Transactional
    public Airport updateAirport(AirportDTO dto, UUID id){
        this.validateAirport(dto);

        Airport result = this.findById(id);

        if (!dto.getName().isEmpty()){
            result.setName(dto.getName());
        }
        if (!dto.getLatitude().isEmpty()){
            result.setLatitude(dto.getLatitude());
        }
        if (!dto.getLongitude().isEmpty()){
            result.setLongitude(dto.getLongitude());
        }
        if (!dto.getCity().name().isEmpty()){
            result.setCity(dto.getCity());
        }

        repository.save(result);
        return result;
    }

    @Transactional
    public void deleteAirport(UUID id){
        if (id==null){
            throw new MissingDataException();
        }
        this.findById(id);

        repository.deleteById(id);
    }
}