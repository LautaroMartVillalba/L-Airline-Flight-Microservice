package ar.com.l_airline.services;

import ar.com.l_airline.domain.airplane.Airplane;
import ar.com.l_airline.domain.airplane.AirplaneDTO;
import ar.com.l_airline.exceptionHandler.custom_exceptions.InternalServiceException;
import ar.com.l_airline.exceptionHandler.custom_exceptions.MissingDataException;
import ar.com.l_airline.exceptionHandler.custom_exceptions.NotFoundException;
import ar.com.l_airline.repositories.AirplaneRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class AirplaneService {

    private final AirplaneRepository repository;

    /**
     * Constructor for AirplaneService.
     *
     * @param repository the repository for airplane nodes
     */
    public AirplaneService(AirplaneRepository repository) {
        this.repository = repository;
    }

    /**
     * Validates an airplane DTO before persisting.
     *
     * @param dto the AirplaneDTO to validate
     * @throws MissingDataException if any validation condition fails
     */
    public void validateAirplane(AirplaneDTO dto) {
        if (dto.getName().name().isEmpty()) {
            throw new MissingDataException("Empty name exception");
        }
        if (dto.getAirlineName() == null) {
            throw new MissingDataException("Empty AirlineName exception");
        }
        if (dto.getCapacity() < 70) {
            throw new MissingDataException("Empty capacity exception");
        }
        if (dto.getTank() < 50000) {
            throw new MissingDataException("Empty fuel tank exception");
        }
        if (dto.getMaxKmDistance() < 50) {
            throw new MissingDataException("Empty max distance exception");
        }
    }

    /**
     * Creates a new airplane entity.
     *
     * @param dto the AirplaneDTO containing airplane data
     * @return the created Airplane entity
     */
    @Transactional
    public Airplane createAirplane(AirplaneDTO dto) {
        this.validateAirplane(dto);

        Airplane airplane = Airplane.builder()
                .name(dto.getName())
                .airlineName(dto.getAirlineName())
                .capacity(dto.getCapacity())
                .tank(dto.getTank())
                .maxKmDistance(dto.getMaxKmDistance()).build();

        repository.save(airplane);
        return airplane;
    }

    /**
     * Finds an airplane by its ID.
     *
     * @param id the UUID of the airplane
     * @return the found Airplane entity
     * @throws MissingDataException if the ID is null or the airplane is not found
     */
    public Airplane findById(UUID id) {
        if (id == null) {
            throw new MissingDataException("Id not received");
        }
        return repository.findByID(id).orElseThrow(NotFoundException::new);
    }

    /**
     * Finds airplanes by name.
     *
     * @param name the name to search for
     * @return a list of airplanes matching the name
     * @throws MissingDataException if the name is empty
     * @throws NotFoundException if no airplanes are found
     */
    public List<Airplane> findByName(String name) {
        if (name.isEmpty()) {
            throw new MissingDataException("Empty name exception");
        }
        List<Airplane> result = repository.findByNameContaining(name);

        if (result.isEmpty()) {
            throw new NotFoundException();
        } else return result;
    }

    /**
     * Finds airplanes by airline name.
     *
     * @param airline the airline name to search for
     * @return a list of airplanes matching the airline name
     * @throws MissingDataException if the airline name is empty
     * @throws NotFoundException if no airplanes are found
     */
    public List<Airplane> findByAirline(String airline) {
        if (airline.isEmpty()) {
            throw new MissingDataException("Empty airline exception");
        }
        List<Airplane> result = repository.findByAirlineNameContaining(airline);

        if (result.isEmpty()) {
            throw new NotFoundException();
        } else return result;
    }

    /**
     * Finds airplanes by capacity range.
     *
     * @param min the minimum capacity
     * @param max the maximum capacity
     * @return a list of airplanes within the specified capacity range
     * @throws MissingDataException if the values are out of range
     * @throws NotFoundException if no airplanes are found
     */
    public List<Airplane> findByCapacity(int min, int max) {
        if (min < 30 || max > 800) {
            throw new MissingDataException("Check passengers number");
        }

        List<Airplane> result = repository.findByCapacityBetween(min, max);

        if (result.isEmpty()) {
            throw new NotFoundException();
        } else return result;
    }

    /**
     * Finds airplanes by fuel tank capacity range.
     *
     * @param min the minimum fuel capacity
     * @param max the maximum fuel capacity
     * @return a list of airplanes within the specified fuel range
     * @throws MissingDataException if the values are out of range
     * @throws NotFoundException if no airplanes are found
     */
    public List<Airplane> findByFuel(int min, int max) {
        if (min < 50000 || max < 50000) {
            throw new MissingDataException("Not enough fuel");
        }

        List<Airplane> result = repository.findByTankBetween(min, max);

        if (result.isEmpty()) {
            throw new NotFoundException();
        } else return result;
    }

    /**
     * Finds airplanes by max distance capacity range.
     *
     * @param min the minimum distance capacity
     * @param max the maximum distance capacity
     * @return a list of airplanes within the specified distance
     * @throws NotFoundException if no airplanes are found
     */
    public List<Airplane> findByMaxDistance(int min, int max) {
        if (min < 50 || max > 10000) {
            throw new MissingDataException("Check the distance");
        }

        List<Airplane> result = repository.findByMaxKmDistanceBetween(min, max);

        if (result.isEmpty()) {
            throw new NotFoundException();
        } else return result;
    }

    /**
     * Updates an existing airplane.
     *
     * @param dto the updated AirplaneDTO
     * @param id the UUID of the airplane to update
     * @return the updated AirplaneDTO
     */
    @Transactional
    public AirplaneDTO updateAirplane(AirplaneDTO dto, UUID id) {
        Airplane result = this.findById(id);

        if (!(dto.getName() == null)) {
            result.setName(dto.getName());
        }
        if (!(dto.getAirlineName() == null)) {
            result.setAirlineName(dto.getAirlineName());
        }
        if (dto.getCapacity()>50) {
            result.setCapacity(dto.getCapacity());
        }
        if (dto.getTank()>50000) {
            result.setTank(dto.getTank());
        }
        if (dto.getMaxKmDistance()>50) {
            result.setMaxKmDistance(dto.getMaxKmDistance());
        }

        repository.save(result);
        return dto;
    }

    /**
     * Deletes an airplane by ID.
     *
     * @param id the UUID of the airplane to delete
     */
    @Transactional
    public void deleteAirplane(UUID id){
        Airplane result = this.findById(id);
        try {
            repository.deleteById(result.getId());
        }catch (Exception e){
            throw new InternalServiceException();
        }
    }
}
