package ar.com.l_airline.controller;

import ar.com.l_airline.domain.airplane.Airplane;
import ar.com.l_airline.domain.airplane.AirplaneDTO;
import ar.com.l_airline.exceptionHandler.custom_exceptions.ExistingObjectException;
import ar.com.l_airline.exceptionHandler.custom_exceptions.InternalServiceException;
import ar.com.l_airline.exceptionHandler.custom_exceptions.MissingDataException;
import ar.com.l_airline.exceptionHandler.custom_exceptions.NotFoundException;
import ar.com.l_airline.services.AirplaneService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/airplane")
public class AirplaneController {

    private final AirplaneService service;

    public AirplaneController (AirplaneService service){
        this.service = service;
    }

    @CircuitBreaker(name = "flightBreaker", fallbackMethod = "fallback")
    @RateLimiter(name ="post-delete-patch")
    @PostMapping("/create")
    @Transactional
    public ResponseEntity<Airplane> createAirplane(@RequestBody AirplaneDTO dto){
        return ResponseEntity.ok(service.createAirplane(dto));
    }

    @CircuitBreaker(name = "flightBreaker", fallbackMethod = "fallback")
    @RateLimiter(name ="get")
    @GetMapping("/byId")
    public ResponseEntity<Airplane> findById(@RequestParam UUID id){
        return ResponseEntity.ok(service.findById(id));
    }

    @CircuitBreaker(name = "flightBreaker", fallbackMethod = "fallback")
    @RateLimiter(name ="get")
    @GetMapping("/byAirline")
    public ResponseEntity<List<Airplane>> findByAirline(@RequestParam String airline){
        return ResponseEntity.ok(service.findByAirline(airline));
    }

    @CircuitBreaker(name = "flightBreaker", fallbackMethod = "fallback")
    @RateLimiter(name ="get")
    @GetMapping("/byCapacity")
    public ResponseEntity<List<Airplane>> findByCapacity(@RequestParam int min, @RequestParam int max){
        return ResponseEntity.ok(service.findByCapacity(min, max));
    }

    @CircuitBreaker(name = "flightBreaker", fallbackMethod = "fallback")
    @RateLimiter(name ="get")
    @GetMapping("/byMaxdistance")
    public ResponseEntity<List<Airplane>> findByDistance(@RequestParam int min, @RequestParam int max){
        return ResponseEntity.ok(service.findByMaxDistance(min, max));
    }

    @CircuitBreaker(name = "flightBreaker", fallbackMethod = "fallback")
    @RateLimiter(name ="get")
    @GetMapping("/byTank")
    public ResponseEntity<List<Airplane>> findByTank(@RequestParam int min, @RequestParam int max){
        return ResponseEntity.ok(service.findByFuel(min, max));
    }

    @CircuitBreaker(name = "flightBreaker", fallbackMethod = "fallback")
    @RateLimiter(name ="get")
    @GetMapping("/byName")
    public ResponseEntity<List<Airplane>> findByName(@RequestParam String name){
        return ResponseEntity.ok(service.findByName(name));
    }

    @CircuitBreaker(name = "flightBreaker", fallbackMethod = "fallback")
    @RateLimiter(name ="post-delete-patch")
    @PatchMapping("/update")
    public ResponseEntity<AirplaneDTO> updateAirplane(@RequestBody AirplaneDTO dto, @RequestParam UUID id){
        return ResponseEntity.ok(service.updateAirplane(dto, id));
    }

    @CircuitBreaker(name = "flightBreaker", fallbackMethod = "fallback")
    @RateLimiter(name ="post-delete-patch")
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAirplane(@RequestParam UUID id){
        service.deleteAirplane(id);
        return ResponseEntity.ok().body("Airplane deleted!");
    }

    private ResponseEntity<String> fallback(Exception e){
        if (e instanceof NotFoundException) {
            throw new NotFoundException();
        }
        if (e instanceof InternalServiceException) {
            throw new InternalServiceException();
        }
        if (e instanceof ExistingObjectException) {
            throw new ExistingObjectException();
        }
        if (e instanceof MissingDataException) {
            throw new MissingDataException(e.getMessage());
        }

        return new ResponseEntity<>("An error has occurred in our services servers. Please, try again later.", HttpStatusCode.valueOf(503));
    }
}
