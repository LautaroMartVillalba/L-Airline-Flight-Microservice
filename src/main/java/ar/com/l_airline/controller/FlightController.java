package ar.com.l_airline.controller;

import ar.com.l_airline.enums.AirlineName;
import ar.com.l_airline.entities.Flight;
import ar.com.l_airline.entities.FlightDTO;
import ar.com.l_airline.exceptionHandler.MissingDataException;
import ar.com.l_airline.services.FlightService;
import ar.com.l_airline.enums.City;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/flight")
public class FlightController {

    private final FlightService service;

    public FlightController(FlightService service) {
        this.service = service;
    }

    @GetMapping("/byId")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Optional<Flight>> byId (@RequestParam Long id){
        Optional<Flight> result = service.findFlightById(id);
        if (result.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/byAirline")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<Flight>> getByAirLine(@RequestParam AirlineName airlineName){
        List<Flight> result = service.findByAirLine(airlineName);
        if (result.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/byOrigin")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<Flight>> byOrigin (@RequestParam City origin){
        List<Flight> result = service.findByOrigin(origin);
        if (result.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/byDestiny")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<Flight>> findByDestiny (@RequestParam City destiny){
        List<Flight> result = service.findByDestiny(destiny);
        if (result.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/bySchedule")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<Flight>> bySchedule (@RequestParam int year, @RequestParam int month,
                                                    @RequestParam int day, @RequestParam int hour,
                                                    @RequestParam int minute){
        List<Flight> result = service.findByFlightSchedule(year, month,day, hour, minute);
        if (result.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/byPrice")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<Flight>> byPrice (@RequestParam double min, @RequestParam double max){
        List<Flight> result = service.findByPriceBetween(min, max);
        if (result.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/insert")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FlightDTO> createFlight (@RequestBody FlightDTO flight) throws MissingDataException {
        Flight result = service.createFlight(flight);
        if (result == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(flight);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteHotel(@RequestParam Long id){
        boolean result = service.deleteFlight(id);
        if (!result){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok("¡Hotel removed!");
    }

    @PatchMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Flight> update(@RequestParam Long id, @RequestBody FlightDTO flight){
        Flight result = service.updateFlight(id, flight);
        if (result == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(result);
    }

}
