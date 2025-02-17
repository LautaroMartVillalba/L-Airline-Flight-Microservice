package ar.com.l_airline.services;

import ar.com.l_airline.domain.City;
import ar.com.l_airline.domain.airplane.Airplane;
import ar.com.l_airline.domain.airport.Airport;
import ar.com.l_airline.domain.ticket.Ticket;
import ar.com.l_airline.domain.ticket.TicketDTO;
import ar.com.l_airline.exceptionHandler.custom_exceptions.MissingDataException;
import ar.com.l_airline.exceptionHandler.custom_exceptions.NotFoundException;
import ar.com.l_airline.repositories.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TicketService {

    private final TicketRepository ticketServ;
    private final AirportService airportServ;
    private final AirplaneService airplaneServ;

    public TicketService(TicketRepository ticketServ, AirportService airportServ, AirplaneService airplaneServ) {
        this.ticketServ = ticketServ;
        this.airportServ = airportServ;
        this.airplaneServ = airplaneServ;
    }

    void ticketValidation(TicketDTO dto){
        if (dto.getAirlineName().name().isEmpty()){
            throw new RuntimeException("Empty airlinename exception");
        }
        if(dto.getDestiny().name().isEmpty()){
            throw new RuntimeException("Invalid destiny city exception");
        }
        if (dto.getOrigin().name().isEmpty()){
            throw new RuntimeException("Invalid origin city exception");
        }
        if (dto.getSeat() < 0){
            throw new RuntimeException("Invalid seat numer exception");
        }
        if (dto.getPrice()<1){
            throw new RuntimeException("Invalid price exception");
        }
        if (dto.getSchedule().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Invalid flight schedule exception");
        }
        if (dto.getDestinyAirportID().toString().isEmpty()){
            throw new RuntimeException("Invalid destiny airport exception");
        }
        if (dto.getOriginAirportID().toString().isEmpty()){
            throw new RuntimeException("Invalid origin airport exception");
        }
        if (dto.getAirplaneID().toString().isEmpty()){
            throw new RuntimeException("Invalid airplane");
        }
    }

    @Transactional
    public TicketDTO create(TicketDTO dto) {
        this.ticketValidation(dto);

        Airplane airplane = airplaneServ.findById(dto.getAirplaneID());
        Airport origin = airportServ.findById(dto.getOriginAirportID());
        Airport destiny = airportServ.findById(dto.getDestinyAirportID());

        Ticket ticket  = Ticket.builder()
                .airlineName(dto.getAirlineName())
                .origin(dto.getOrigin())
                .destiny(dto.getDestiny())
                .seat(dto.getSeat())
                .price(dto.getPrice())
                .schedule(dto.getSchedule())
                .airplane(airplane)
                .airplaneID(dto.getAirplaneID())
                .originAirportID(dto.getOriginAirportID())
                .originAirport(origin)
                .destinyAirportID(dto.getDestinyAirportID())
                .destinyAirport(destiny).build();

        ticketServ.save(ticket);

        return TicketDTO.builder()
                .id(ticket.getId())
                .airlineName(dto.getAirlineName())
                .origin(dto.getOrigin())
                .destiny(dto.getDestiny())
                .seat(dto.getSeat())
                .price(dto.getPrice())
                .schedule(dto.getSchedule())
                .airplaneID(dto.getAirplaneID())
                .originAirportID(dto.getOriginAirportID())
                .destinyAirportID(dto.getDestinyAirportID()).build();
    }

    public Ticket findById(UUID id){
        if (id == null){
            throw new MissingDataException();
        }

        return ticketServ.findById(id).orElseThrow(NotFoundException::new);
    }


    public TicketDTO findByIdResponse(UUID id){
        if (id == null){
            throw new MissingDataException();
        }

        Ticket result = ticketServ.findById(id).orElseThrow(NotFoundException::new);
        return TicketDTO.builder()
                .airlineName(result.getAirlineName())
                .origin(result.getOrigin())
                .destiny(result.getDestiny())
                .seat(result.getSeat())
                .price(result.getPrice())
                .schedule(result.getSchedule())
                .airplaneID(result.getAirplaneID())
                .originAirportID(result.getOriginAirportID())
                .destinyAirportID(result.getDestinyAirportID()).build();
    }

    public List<TicketDTO> findByOriginContaining(String city) {
        if (city.isEmpty()){
            throw new MissingDataException();
        }

        List<TicketDTO> response = new ArrayList<>();
        List<Ticket> result = ticketServ.findByOriginContaining(city);
        if (result.isEmpty()){
            throw new NotFoundException();
        }

        result.forEach(ticket -> {
            TicketDTO dto = TicketDTO.builder()
                    .airlineName(ticket.getAirlineName())
                    .origin(ticket.getOrigin())
                    .destiny(ticket.getDestiny())
                    .seat(ticket.getSeat())
                    .price(ticket.getPrice())
                    .schedule(ticket.getSchedule())
                    .airplaneID(ticket.getAirplaneID())
                    .originAirportID(ticket.getOriginAirportID())
                    .destinyAirportID(ticket.getDestinyAirportID()).build();

            response.add(dto);
        });

        return response;
    }

    public List<TicketDTO> findByDestinyContaining(String city) {
        if (city.isEmpty()){
            throw new MissingDataException();
        }
        List<TicketDTO> response = new ArrayList<>();

        List<Ticket> result = ticketServ.findByDestinyContaining(city);
        if (result.isEmpty()){
            throw new NotFoundException();
        }

        result.forEach(ticket -> {
            TicketDTO dto = TicketDTO.builder()
                    .airlineName(ticket.getAirlineName())
                    .origin(ticket.getOrigin())
                    .destiny(ticket.getDestiny())
                    .seat(ticket.getSeat())
                    .price(ticket.getPrice())
                    .schedule(ticket.getSchedule())
                    .airplaneID(ticket.getAirplaneID())
                    .originAirportID(ticket.getOriginAirportID())
                    .destinyAirportID(ticket.getDestinyAirportID()).build();

            response.add(dto);
        });
        return response;
    }

    public List<TicketDTO> findByPriceBetween(double min, double max) {
        if (min < 0 || max < 0) {
            throw new MissingDataException();
        }
        List<TicketDTO> response = new ArrayList<>();
        List<Ticket> result = ticketServ.findByPriceBetween(min, max);

        if (result.isEmpty()) {
            throw new NotFoundException();
        }

        result.forEach(ticket -> {
            TicketDTO dto = TicketDTO.builder()
                    .airlineName(ticket.getAirlineName())
                    .origin(ticket.getOrigin())
                    .destiny(ticket.getDestiny())
                    .seat(ticket.getSeat())
                    .price(ticket.getPrice())
                    .schedule(ticket.getSchedule())
                    .airplaneID(ticket.getAirplaneID())
                    .originAirportID(ticket.getOriginAirportID())
                    .destinyAirportID(ticket.getDestinyAirportID()).build();

            response.add(dto);
        });
        return response;
    }

    public List<TicketDTO> findByScheduleBetween(String from, String to) {

        int fromYear = Integer.parseInt(from.substring(0, 4));
        int fromMonth = Integer.parseInt(from.substring(4, 6));
        int fromDay = Integer.parseInt(from.substring(6, 8));
        int toYear = Integer.parseInt(to.substring(0, 4));
        int toMonth = Integer.parseInt(to.substring(4, 6));
        int toDay = Integer.parseInt(to.substring(6, 8));

        LocalDateTime dateFrom = LocalDateTime.of(fromYear, fromMonth, fromDay,0, 0 ,0);
        LocalDateTime dateTo = LocalDateTime.of(toYear, toMonth, toDay, 23, 59, 59);

        List<TicketDTO> response = new ArrayList<>();
        List<Ticket> result = ticketServ.findByScheduleBetween(dateFrom,dateTo);

        if (result.isEmpty()){
            throw new NotFoundException();
        }
        result.forEach(ticket -> {
            TicketDTO dto = TicketDTO.builder()
                    .airlineName(ticket.getAirlineName())
                    .origin(ticket.getOrigin())
                    .destiny(ticket.getDestiny())
                    .seat(ticket.getSeat())
                    .price(ticket.getPrice())
                    .schedule(ticket.getSchedule())
                    .airplaneID(ticket.getAirplaneID())
                    .originAirportID(ticket.getOriginAirportID())
                    .destinyAirportID(ticket.getDestinyAirportID()).build();

            response.add(dto);
        });
        return response;
    }

    public List<TicketDTO> findByAirlineNameContaining(String airline) {
        if (airline.isEmpty()){
            throw new MissingDataException();
        }

        List<TicketDTO> response = new ArrayList<>();
        List<Ticket> result = ticketServ.findByAirlineNameContaining(airline);

        if (result.isEmpty()){
            throw new NotFoundException();
        }
        result.forEach(ticket -> {
            TicketDTO dto = TicketDTO.builder()
                    .airlineName(ticket.getAirlineName())
                    .origin(ticket.getOrigin())
                    .destiny(ticket.getDestiny())
                    .seat(ticket.getSeat())
                    .price(ticket.getPrice())
                    .schedule(ticket.getSchedule())
                    .airplaneID(ticket.getAirplaneID())
                    .originAirportID(ticket.getOriginAirportID())
                    .destinyAirportID(ticket.getDestinyAirportID()).build();

            response.add(dto);
        });
        return response;
    }

    public List<TicketDTO> findByAirplane(UUID airplaneID) {
        if (airplaneID == null){
            throw new MissingDataException();
        }

        List<TicketDTO> response = new ArrayList<>();
        List<Ticket> result = ticketServ.findByAirplane(airplaneID);

        if (result.isEmpty()){
            throw new NotFoundException();
        }
        result.forEach(ticket -> {
            TicketDTO dto = TicketDTO.builder()
                    .airlineName(ticket.getAirlineName())
                    .origin(ticket.getOrigin())
                    .destiny(ticket.getDestiny())
                    .seat(ticket.getSeat())
                    .price(ticket.getPrice())
                    .schedule(ticket.getSchedule())
                    .airplaneID(ticket.getAirplaneID())
                    .originAirportID(ticket.getOriginAirportID())
                    .destinyAirportID(ticket.getDestinyAirportID()).build();

            response.add(dto);
        });
        return response;
    }

    public List<TicketDTO> findByOriginAirport(UUID id) {
        if (id.toString().isEmpty() || id == null){
            throw new MissingDataException();
        }

        List<TicketDTO> response = new ArrayList<>();
        List<Ticket> result = ticketServ.findByOriginAirportID(id);

        if (result.isEmpty()){
            throw new NotFoundException();
        }
        result.forEach(ticket -> {
            TicketDTO dto = TicketDTO.builder()
                    .airlineName(ticket.getAirlineName())
                    .origin(ticket.getOrigin())
                    .destiny(ticket.getDestiny())
                    .seat(ticket.getSeat())
                    .price(ticket.getPrice())
                    .schedule(ticket.getSchedule())
                    .airplaneID(ticket.getAirplaneID())
                    .originAirportID(ticket.getOriginAirportID())
                    .destinyAirportID(ticket.getDestinyAirportID()).build();

            response.add(dto);
        });
        return response;
    }

    public TicketDTO findByAirplaneAndSeat(UUID airplaneId, int seat){
        if (airplaneId == null || seat < 0){
            throw new MissingDataException();
        }
        Ticket result = ticketServ.findByAirplaneIDAndSeat(airplaneId, seat).orElseThrow(NotFoundException::new);

        return TicketDTO.builder()
                .airlineName(result.getAirlineName())
                .origin(result.getOrigin())
                .destiny(result.getDestiny())
                .seat(result.getSeat())
                .price(result.getPrice())
                .schedule(result.getSchedule())
                .airplaneID(result.getAirplaneID())
                .originAirportID(result.getOriginAirportID())
                .destinyAirportID(result.getDestinyAirportID()).build();
    }

    public List<TicketDTO> findByDestinyAirport(UUID id) {
        if (id.toString().isEmpty() || id == null){
            throw new MissingDataException();
        }

        List<TicketDTO> response = new ArrayList<>();
        List<Ticket> result = ticketServ.findByDestinyAirportID(id);

        if (result.isEmpty()){
            throw new NotFoundException();
        }
        result.forEach(ticket -> {
            TicketDTO dto = TicketDTO.builder()
                    .airlineName(ticket.getAirlineName())
                    .origin(ticket.getOrigin())
                    .destiny(ticket.getDestiny())
                    .seat(ticket.getSeat())
                    .price(ticket.getPrice())
                    .schedule(ticket.getSchedule())
                    .airplaneID(ticket.getAirplaneID())
                    .originAirportID(ticket.getOriginAirportID())
                    .destinyAirportID(ticket.getDestinyAirportID()).build();

            response.add(dto);
        });
        return response;
    }

    @Transactional
    public TicketDTO update(TicketDTO dto, UUID id) {
        if (id == null) {
            throw new MissingDataException();
        }

        Ticket ticket = this.findById(id);

        if (dto.getAirlineName() != null){
            ticket.setAirlineName(dto.getAirlineName());
        }
        if(dto.getDestiny() != null){
            ticket.setDestiny(dto.getDestiny());
        }
        if (dto.getOrigin() != null){
            ticket.setOrigin(dto.getOrigin());
        }
        if (!(dto.getPrice()<1)){
            ticket.setPrice(dto.getPrice());
        }
        if (dto.getSchedule().isAfter(LocalDateTime.now())){
            ticket.setSchedule(dto.getSchedule());
        }
        if (!(dto.getSeat()<0)){
            ticket.setSeat(dto.getSeat());
        }
        if (dto.getDestinyAirportID() != null){
            Airport airport = this.airportServ.findById(dto.getDestinyAirportID());
            ticket.setDestinyAirportID(dto.getDestinyAirportID());
            ticket.setDestinyAirport(airport);
        }
        if (dto.getOriginAirportID() != null) {
            Airport airport = this.airportServ.findById(dto.getOriginAirportID());
            ticket.setOriginAirportID(dto.getOriginAirportID());
            ticket.setOriginAirport(airport);
        }
        if(dto.getAirplaneID() != null){
            Airplane airplane = this.airplaneServ.findById(dto.getAirplaneID());
            ticket.setAirplaneID(dto.getAirplaneID());
            ticket.setAirplane(airplane);
        }
        ticketServ.save(ticket);

        return TicketDTO.builder()
                .id(ticket.getId())
                .airlineName(ticket.getAirlineName())
                .origin(ticket.getOrigin())
                .destiny(ticket.getDestiny())
                .seat(ticket.getSeat())
                .price(ticket.getPrice())
                .schedule(ticket.getSchedule())
                .airplaneID(ticket.getAirplaneID())
                .originAirportID(ticket.getOriginAirportID())
                .destinyAirportID(ticket.getDestinyAirportID()).build();
    }

    @Transactional
    public void delete(UUID id) {
        this.findById(id);

        ticketServ.deleteById(id);
    }

}
