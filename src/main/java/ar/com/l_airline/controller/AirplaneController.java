package ar.com.l_airline.controller;

import ar.com.l_airline.domain.airplane.Airplane;
import ar.com.l_airline.domain.airplane.AirplaneDTO;
import ar.com.l_airline.services.AirplaneService;
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

    @PostMapping("/create")
    @Transactional
    public ResponseEntity<Airplane> createAirplane(@RequestBody AirplaneDTO dto){
        return ResponseEntity.ok(service.createAirplane(dto));
    }

    @GetMapping("/byId")
    public ResponseEntity<Airplane> findById(@RequestParam UUID id){
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/byAirline")
    public ResponseEntity<List<Airplane>> findByAirline(@RequestParam String airline){
        return ResponseEntity.ok(service.findByAirline(airline));
    }

    @GetMapping("/byCapacity")
    public ResponseEntity<List<Airplane>> findByCapacity(@RequestParam int min, @RequestParam int max){
        return ResponseEntity.ok(service.findByCapacity(min, max));
    }

    @GetMapping("/byMaxdistance")
    public ResponseEntity<List<Airplane>> findByDistance(@RequestParam int min, @RequestParam int max){
        return ResponseEntity.ok(service.findByMaxDistance(min, max));
    }

    @GetMapping("/byTank")
    public ResponseEntity<List<Airplane>> findByTank(@RequestParam int min, @RequestParam int max){
        return ResponseEntity.ok(service.findByFuel(min, max));
    }

    @GetMapping("/byName")
    public ResponseEntity<List<Airplane>> findByName(@RequestParam String name){
        return ResponseEntity.ok(service.findByName(name));
    }

    @PatchMapping("/update")
    public ResponseEntity<AirplaneDTO> updateAirplane(@RequestBody AirplaneDTO dto, @RequestParam UUID uuid){
        return ResponseEntity.ok(service.updateAirplane(dto, uuid));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAirplane(@RequestParam UUID id){
        return ResponseEntity.ok().body("Airplane deleted!");
    }
}
