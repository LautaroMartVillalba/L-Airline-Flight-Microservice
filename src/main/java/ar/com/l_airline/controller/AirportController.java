package ar.com.l_airline.controller;

import ar.com.l_airline.domain.airport.Airport;
import ar.com.l_airline.domain.airport.AirportDTO;
import ar.com.l_airline.exceptionHandler.custom_exceptions.ExistingObjectException;
import ar.com.l_airline.exceptionHandler.custom_exceptions.InternalServiceException;
import ar.com.l_airline.exceptionHandler.custom_exceptions.MissingDataException;
import ar.com.l_airline.exceptionHandler.custom_exceptions.NotFoundException;
import ar.com.l_airline.services.AirportService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/airport")
public class AirportController {

    private final AirportService service;

    public AirportController(AirportService service) {
        this.service = service;
    }

    @CircuitBreaker(name = "flightBreaker", fallbackMethod = "fallback")
    @RateLimiter(name ="post-delete-patch")
    @PostMapping("/create")
    public ResponseEntity<Airport> createAirport(@RequestBody AirportDTO dto){
        return ResponseEntity.ok(service.createAirport(dto));
    }

    @CircuitBreaker(name = "flightBreaker", fallbackMethod = "fallback")
    @RateLimiter(name ="get")
    @GetMapping("/byId")
    public ResponseEntity<Airport> findById(@RequestParam UUID id){
        return ResponseEntity.ok(service.findById(id));
    }

    @CircuitBreaker(name = "flightBreaker", fallbackMethod = "fallback")
    @RateLimiter(name ="get")
    @GetMapping("/byCoordinates")
    public ResponseEntity<Airport> findByCoordinates(@RequestParam String latitude, @RequestParam String longitude){
        return ResponseEntity.ok(service.findByLatitudeAndLongitude(latitude, longitude));
    }

    @CircuitBreaker(name = "flightBreaker", fallbackMethod = "fallback")
    @RateLimiter(name ="get")
    @GetMapping("/byName")
    public ResponseEntity<List<Airport>> findByName(@RequestParam String name){
        return ResponseEntity.ok(service.findByName(name));
    }

    @CircuitBreaker(name = "flightBreaker", fallbackMethod = "fallback")
    @RateLimiter(name ="get")
    @GetMapping("/byCity")
    public ResponseEntity<List<Airport>> findByCity(@RequestParam String city){
        return ResponseEntity.ok(service.findByCity(city));
    }

    @CircuitBreaker(name = "flightBreaker", fallbackMethod = "fallback")
    @RateLimiter(name ="post-delete-patch")
    @PatchMapping("/update")
    public ResponseEntity<Airport> update(@RequestBody AirportDTO dto, @RequestParam UUID id){
        return ResponseEntity.ok(service.updateAirport(dto, id));
    }

    @CircuitBreaker(name = "flightBreaker", fallbackMethod = "fallback")
    @RateLimiter(name ="post-delete-patch")
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam UUID id){
        service.deleteAirport(id);
        return ResponseEntity.ok().body("Airport deleted");
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
