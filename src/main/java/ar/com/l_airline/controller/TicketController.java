package ar.com.l_airline.controller;

import ar.com.l_airline.domain.City;
import ar.com.l_airline.domain.ticket.Ticket;
import ar.com.l_airline.domain.ticket.TicketDTO;
import ar.com.l_airline.services.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/ticket")
public class TicketController {

    private final TicketService service;

    public TicketController(TicketService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<TicketDTO> createTicket (@RequestBody TicketDTO dto){
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/byId")
    public ResponseEntity<TicketDTO> findById(@RequestParam UUID id){
        return ResponseEntity.ok(service.findByIdResponse(id));
    }

    @GetMapping("/byOrigin")
    public ResponseEntity<List<TicketDTO>> findByOrigin(@RequestParam String origin){
        return ResponseEntity.ok(service.findByOriginContaining(origin));
    }

    @GetMapping("/byDestiny")
    public ResponseEntity<List<TicketDTO>> findByDestiny(@RequestParam String destiny){
        return ResponseEntity.ok(service.findByDestinyContaining(destiny));
    }

    @GetMapping("/byPrice")
    public ResponseEntity<List<TicketDTO>> findByPrice(@RequestParam double min,@RequestParam double max){
        return ResponseEntity.ok(service.findByPriceBetween(min,max));
    }

    @GetMapping("/byAirline")
    public ResponseEntity<List<TicketDTO>> findByAirlineName(@RequestParam String airline){
        return ResponseEntity.ok(service.findByAirlineNameContaining(airline));
    }

    @GetMapping("/bySchedule")
    public ResponseEntity<List<TicketDTO>> findBySchedule(@RequestParam String from,@RequestParam String to){
        return ResponseEntity.ok(service.findByScheduleBetween(from,to));
    }

    @GetMapping("/byAirplane")
    public ResponseEntity<List<TicketDTO>> findByAirplane(@RequestParam UUID airplaneID){
        return ResponseEntity.ok(service.findByAirplane(airplaneID));
    }

    @GetMapping("/byOriginAirport")
    public ResponseEntity<List<TicketDTO>> findByOriginAirport(@RequestParam UUID id){
        return ResponseEntity.ok(service.findByOriginAirport(id));
    }

    @GetMapping("/byAirplaneSeat")
        public ResponseEntity<TicketDTO> findByAirplaneAndSeat(@RequestParam UUID airplaneId,@RequestParam int seat){
        return ResponseEntity.ok(service.findByAirplaneAndSeat(airplaneId, seat));
    }

    @GetMapping("/byDestinyAirport")
    public ResponseEntity<List<TicketDTO>> findByDestinyAirport(@RequestParam UUID id){
        return ResponseEntity.ok(service.findByDestinyAirport(id));
    }

    @PatchMapping("/update")
    public ResponseEntity<TicketDTO> update(@RequestBody TicketDTO dto,@RequestParam UUID id){
        return ResponseEntity.ok(service.update(dto,id));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam UUID id){
        service.delete(id);
        return ResponseEntity.ok("Ticket deleted!");
    }

}
