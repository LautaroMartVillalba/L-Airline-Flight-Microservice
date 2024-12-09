package ar.com.l_airline.controller;

import ar.com.l_airline.domain.enums.AirlineName;
import ar.com.l_airline.domain.entities.Flight;
import ar.com.l_airline.domain.dto.FlightDTO;
import ar.com.l_airline.services.FlightService;
import ar.com.l_airline.domain.enums.City;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/flight")
public class FlightController {

    private final FlightService service;

    public FlightController(FlightService service) {
        this.service = service;
    }

    @GetMapping("/byId")
    public ResponseEntity<Flight> byId (@RequestParam Long id){
        return ResponseEntity.ok(service.findFlightById(id));
    }

    @GetMapping("/byAirline")
    public ResponseEntity<List<Flight>> getByAirLine(@RequestParam AirlineName airlineName){
        return ResponseEntity.ok(service.findByAirLine(airlineName));
    }

    @GetMapping("/byOrigin")
    public ResponseEntity<List<Flight>> byOrigin (@RequestParam City origin){
        return ResponseEntity.ok(service.findByOrigin(origin));
    }

    @GetMapping("/byDestiny")
    public ResponseEntity<List<Flight>> findByDestiny (@RequestParam City destiny){
        return ResponseEntity.ok(service.findByDestiny(destiny));
    }

    @GetMapping("/bySchedule")
    public ResponseEntity<List<Flight>> bySchedule (@RequestParam int year, @RequestParam int month,
                                                    @RequestParam int day, @RequestParam int hour,
                                                    @RequestParam int minute){
        return ResponseEntity.ok(service.findByFlightSchedule(year, month,day, hour, minute));
    }

    @GetMapping("/byPrice")
    public ResponseEntity<List<Flight>> byPrice (@RequestParam double min, @RequestParam double max){
        return ResponseEntity.ok(service.findByPriceBetween(min, max));
    }

    @PostMapping("/insert")
    public ResponseEntity<FlightDTO> createFlight (@RequestBody FlightDTO flight) {
        service.createFlight(flight);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteHotel(@RequestParam Long id){
        service.deleteFlight(id);
        return ResponseEntity.ok("¡Hotel removed!");
    }

    @PatchMapping("/update")
    public ResponseEntity<Flight> update(@RequestParam Long id, @RequestBody FlightDTO flight){
        return ResponseEntity.ok(service.updateFlight(id, flight));
    }

}
