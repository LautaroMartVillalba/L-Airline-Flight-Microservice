package ar.com.l_airline.services;

import ar.com.l_airline.domain.airplane.Airplane;
import ar.com.l_airline.domain.airplane.AirplaneDTO;
import ar.com.l_airline.exceptionHandler.custom_exceptions.NotFoundException;
import ar.com.l_airline.repositories.AirplaneRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class AirplaneService {

    private final AirplaneRepository repository;

    public AirplaneService(AirplaneRepository repository) {
        this.repository = repository;
    }

    public void validateAirplane(AirplaneDTO dto) {
        if (dto.getName().name().isEmpty()) {
            throw new RuntimeException("Empty name exception");
        }
        if (dto.getAirlineName().name().isEmpty()) {
            throw new RuntimeException("Empty AirlineName exception");
        }
        if (dto.getCapacity() < 70) {
            throw new RuntimeException("Empty capacity exception");
        }
        if (dto.getTank() < 50000) {
            throw new RuntimeException("Empty fuel tank exception");
        }
        if (dto.getMaxKmDistance() < 50) {
            throw new RuntimeException("Empty max distance exception");
        }
    }

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

    public Airplane findById(UUID id) {
        if (id == null) {
            throw new RuntimeException("Id not received");
        }
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Airplane not found"));
    }

    public List<Airplane> findByName(String name) {
        if (name.isEmpty()) {
            throw new RuntimeException("Empty name exception");
        }
        List<Airplane> result = repository.findByNameContaining(name);

        if (result.isEmpty()) {
            throw new NotFoundException();
        } else return result;
    }

    public List<Airplane> findByAirline(String airline) {
        if (airline.isEmpty()) {
            throw new RuntimeException("Empty airline exception");
        }
        List<Airplane> result = repository.findByAirlineNameContaining(airline);

        if (result.isEmpty()) {
            throw new NotFoundException();
        } else return result;
    }

    public List<Airplane> findByCapacity(int min, int max) {
        if (min < 30 || max < 800) {
            throw new RuntimeException("Check passengers number");
        }

        List<Airplane> result = repository.findByCapacityBetween(min, max);

        if (result.isEmpty()) {
            throw new NotFoundException();
        } else return result;
    }

    public List<Airplane> findByFuel(int min, int max) {
        if (min < 50000 || max < 50000) {
            throw new RuntimeException("Not enough fuel");
        }

        List<Airplane> result = repository.findByTankBetween(min, max);

        if (result.isEmpty()) {
            throw new NotFoundException();
        } else return result;
    }

    public List<Airplane> findByMaxDistance(int min, int max) {
        if (min < 50 || max > 7000) {
            throw new RuntimeException("Check the distance");
        }

        List<Airplane> result = repository.findByMaxKmDistanceBetween(min, max);

        if (result.isEmpty()) {
            throw new NotFoundException();
        } else return result;
    }

    @Transactional
    public AirplaneDTO updateAirplane(AirplaneDTO dto, UUID id) {
        Airplane result = this.findById(id);

        if (!dto.getName().name().isEmpty()) {
            result.setName(dto.getName());
        }
        if (!dto.getAirlineName().name().isEmpty()) {
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

    @Transactional
    public boolean deleteAirplane(UUID id){
        Airplane result = this.findById(id);

        repository.deleteById(result.getId());

        return true;
    }
}
