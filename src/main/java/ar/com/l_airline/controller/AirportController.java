package ar.com.l_airline.controller;

import ar.com.l_airline.domain.airport.Airport;
import ar.com.l_airline.domain.airport.AirportDTO;
import ar.com.l_airline.services.AirportService;
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

    @PostMapping("/create")
    public ResponseEntity<Airport> createAirport(@RequestBody AirportDTO dto){
        return ResponseEntity.ok(service.createAirport(dto));
    }

    @GetMapping("/byId")
    public ResponseEntity<Airport> findById(@RequestParam UUID id){
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/byCoordinates")
    public ResponseEntity<Airport> findByCoordinates(@RequestParam String latitude, @RequestParam String longitude){
        return ResponseEntity.ok(service.findByLatitudeAndLongitude(latitude, longitude));
    }

    @GetMapping("/byName")
    public ResponseEntity<List<Airport>> findByName(@RequestParam String name){
        return ResponseEntity.ok(service.findByName(name));
    }

    @GetMapping("/byCity")
    public ResponseEntity<List<Airport>> findByCity(@RequestParam String city){
        return ResponseEntity.ok(service.findByCity(city));
    }

    @PatchMapping("/update")
    public ResponseEntity<Airport> update(@RequestBody AirportDTO dto, @RequestParam UUID id){
        return ResponseEntity.ok(service.updateAirport(dto, id));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam UUID id){
        service.deleteAirport(id);
        return ResponseEntity.ok().body("Airport deleted");
    }
}
