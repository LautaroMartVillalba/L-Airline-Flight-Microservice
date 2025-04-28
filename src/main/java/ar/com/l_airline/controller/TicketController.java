package ar.com.l_airline.controller;

import ar.com.l_airline.domain.ticket.TicketCreateDTO;
import ar.com.l_airline.domain.ticket.TicketRetrieveDTO;
import ar.com.l_airline.exceptionHandler.custom_exceptions.ExistingObjectException;
import ar.com.l_airline.exceptionHandler.custom_exceptions.InternalServiceException;
import ar.com.l_airline.exceptionHandler.custom_exceptions.MissingDataException;
import ar.com.l_airline.exceptionHandler.custom_exceptions.NotFoundException;
import ar.com.l_airline.services.TicketService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    private final TicketService service;

    public TicketController(TicketService service) {
        this.service = service;
    }

    @CircuitBreaker(name = "flightBreaker", fallbackMethod = "fallback")
    @RateLimiter(name ="ticket-create")
    @PostMapping("/create")
    public ResponseEntity<TicketRetrieveDTO> createTicket (@RequestBody TicketCreateDTO dto){
        return ResponseEntity.ok(service.create(dto));
    }

    @CircuitBreaker(name = "flightBreaker", fallbackMethod = "fallback")
    @RateLimiter(name ="get")
    @GetMapping("/byId")
    public ResponseEntity<TicketRetrieveDTO> findById(@RequestParam UUID id){
        return ResponseEntity.ok(service.findByIdResponse(id));
    }

    @CircuitBreaker(name = "flightBreaker", fallbackMethod = "fallback")
    @RateLimiter(name ="get")
    @GetMapping("/byOrigin")
    public ResponseEntity<List<TicketRetrieveDTO>> findByOrigin(@RequestParam String origin){
        return ResponseEntity.ok(service.findByOriginContaining(origin));
    }

    @CircuitBreaker(name = "flightBreaker", fallbackMethod = "fallback")
    @RateLimiter(name ="get")
    @GetMapping("/byDestiny")
    public ResponseEntity<List<TicketRetrieveDTO>> findByDestiny(@RequestParam String destiny){
        return ResponseEntity.ok(service.findByDestinyContaining(destiny));
    }

    @CircuitBreaker(name = "flightBreaker", fallbackMethod = "fallback")
    @RateLimiter(name ="get")
    @GetMapping("/byPrice")
    public ResponseEntity<List<TicketRetrieveDTO>> findByPrice(@RequestParam double min, @RequestParam double max){
        return ResponseEntity.ok(service.findByPriceBetween(min,max));
    }

    @CircuitBreaker(name = "flightBreaker", fallbackMethod = "fallback")
    @RateLimiter(name ="get")
    @GetMapping("/byAirline")
    public ResponseEntity<List<TicketRetrieveDTO>> findByAirlineName(@RequestParam String airline){
        return ResponseEntity.ok(service.findByAirlineNameContaining(airline));
    }

    @CircuitBreaker(name = "flightBreaker", fallbackMethod = "fallback")
    @RateLimiter(name ="get")
    @GetMapping("/bySchedule")
    public ResponseEntity<List<TicketRetrieveDTO>> findBySchedule(@RequestParam String from, @RequestParam String to){
        return ResponseEntity.ok(service.findByScheduleBetween(from,to));
    }

    @CircuitBreaker(name = "flightBreaker", fallbackMethod = "fallback")
    @RateLimiter(name ="get")
    @GetMapping("/byAirplane")
    public ResponseEntity<List<TicketRetrieveDTO>> findByAirplane(@RequestParam UUID airplaneID){
        return ResponseEntity.ok(service.findByAirplane(airplaneID));
    }

    @CircuitBreaker(name = "flightBreaker", fallbackMethod = "fallback")
    @RateLimiter(name ="get")
    @GetMapping("/byOriginAirport")
    public ResponseEntity<List<TicketRetrieveDTO>> findByOriginAirport(@RequestParam UUID id){
        return ResponseEntity.ok(service.findByOriginAirport(id));
    }

    @CircuitBreaker(name = "flightBreaker", fallbackMethod = "fallback")
    @RateLimiter(name ="get")
    @GetMapping("/byAirplaneSeat")
        public ResponseEntity<TicketRetrieveDTO> findByAirplaneAndSeat(@RequestParam UUID airplaneId, @RequestParam int seat){
        return ResponseEntity.ok(service.findByAirplaneAndSeat(airplaneId, seat));
    }

    @CircuitBreaker(name = "flightBreaker", fallbackMethod = "fallback")
    @RateLimiter(name ="get")
    @GetMapping("/byDestinyAirport")
    public ResponseEntity<List<TicketRetrieveDTO>> findByDestinyAirport(@RequestParam UUID id){
        return ResponseEntity.ok(service.findByDestinyAirport(id));
    }

    @CircuitBreaker(name = "flightBreaker", fallbackMethod = "fallback")
    @RateLimiter(name ="post-delete-patch")
    @PatchMapping("/update")
    public ResponseEntity<TicketRetrieveDTO> update(@RequestBody TicketRetrieveDTO dto, @RequestParam UUID id){
        return ResponseEntity.ok(service.update(dto,id));
    }

    @CircuitBreaker(name = "flightBreaker", fallbackMethod = "fallback")
    @RateLimiter(name ="post-delete-patch")
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam UUID id){
        service.delete(id);
        return ResponseEntity.ok("Ticket deleted!");
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
