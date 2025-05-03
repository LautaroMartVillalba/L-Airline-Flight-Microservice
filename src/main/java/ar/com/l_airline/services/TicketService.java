package ar.com.l_airline.services;

import ar.com.l_airline.domain.airplane.Airplane;
import ar.com.l_airline.domain.airport.Airport;
import ar.com.l_airline.domain.ticket.Ticket;
import ar.com.l_airline.domain.ticket.TicketCreateDTO;
import ar.com.l_airline.domain.ticket.TicketRetrieveDTO;
import ar.com.l_airline.exceptionHandler.custom_exceptions.ExistingObjectException;
import ar.com.l_airline.exceptionHandler.custom_exceptions.MissingDataException;
import ar.com.l_airline.exceptionHandler.custom_exceptions.NotFoundException;
import ar.com.l_airline.repositories.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * Validates the ticket details.
     *
     * @param dto the ticket data transfer object (DTO) containing ticket details
     * @throws MissingDataException if any validation fails
     */
    void ticketValidation(TicketCreateDTO dto){
        if (dto.getSeat() <= 0){
            throw new MissingDataException("Invalid seat number.");
        }
        if (dto.getSchedule() == null || dto.getSchedule().isBefore(LocalDateTime.now())){
            throw new MissingDataException("Invalid flight schedule.");
        }
        if (dto.getDestinyAirportID() == null){
            throw new MissingDataException("Invalid airport id airport.");
        }
        if (dto.getOriginAirportID() == null){
            throw new MissingDataException("Invalid origin id airport.");
        }
        if (dto.getAirplaneID() == null){
            throw new MissingDataException("Invalid id airplane.");
        }
    }

    /**
     * Creates a new ticket.
     *
     * @param dto the ticket DTO containing ticket details
     * @return the created ticket DTO
     */
    @Transactional
    public TicketRetrieveDTO create(TicketCreateDTO dto) {
        this.ticketValidation(dto);

        Airplane airplane = airplaneServ.findById(dto.getAirplaneID());
        Airport origin = airportServ.findById(dto.getOriginAirportID());
        Airport destiny = airportServ.findById(dto.getDestinyAirportID());

        List<Ticket> ticketInDB = ticketServ.findByScheduleBetween(dto.getSchedule(), dto.getSchedule());

        ticketInDB.forEach(ticket -> {
            if (ticket.getAirplaneID().equals(dto.getAirplaneID()) && ticket.getSchedule().equals(dto.getSchedule()) && ticket.getSeat() == dto.getSeat()){
                throw new ExistingObjectException();
            }
        });

        double ticketPrice = PriceGenerator.calc(origin.getLatitude(), origin.getLongitude(), destiny.getLatitude(), destiny.getLongitude());

        Ticket ticket  = Ticket.builder()
                .airplane(airplane)
                .origin(origin.getCity())
                .originAirport(origin)
                .originAirportID(UUID.fromString(String.valueOf(dto.getOriginAirportID())))
                .destiny(destiny.getCity())
                .destinyAirport(destiny)
                .destinyAirportID(dto.getDestinyAirportID())
                .airplane(airplane)
                .airplaneID(dto.getAirplaneID())
                .schedule(dto.getSchedule())
                .seat(dto.getSeat())
                .price(ticketPrice).build();

        ticketServ.save(ticket);

        return TicketRetrieveDTO.builder()
                .code(ticket.getCode())
                .airlineName(airplane.getAirlineName())
                .origin(origin.getCity())
                .destiny(destiny.getCity())
                .seat(dto.getSeat())
                .price(ticketPrice)
                .schedule(dto.getSchedule())
                .airplaneID(dto.getAirplaneID())
                .originAirportID(dto.getOriginAirportID())
                .destinyAirportID(dto.getDestinyAirportID()).build();
    }

    /**
     * Finds a ticket by its ID.
     *
     * @param id the ticket ID
     * @return the found ticket
     * @throws MissingDataException if the ID is null
     * @throws NotFoundException if the ticket is not found
     */
    public Ticket findById(UUID id){
        if (id == null){
            throw new MissingDataException("Empty ID.");
        }

        return ticketServ.findById(id).orElseThrow(NotFoundException::new);
    }

    /**
     * Finds a ticket by its ID and returns it as a DTO.
     *
     * @param id the ticket ID
     * @return the ticket DTO
     * @throws MissingDataException if the ID is null
     * @throws NotFoundException if the ticket is not found
     */
    public TicketRetrieveDTO findByIdResponse(UUID id){
        if (id == null || id.toString().isEmpty()){
            throw new MissingDataException("Empty ID.");
        }

        Ticket result = ticketServ.findByID(id).orElseThrow(NotFoundException::new);
        return TicketRetrieveDTO.builder()
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

    /**
     * Finds tickets by origin city containing the specified string.
     *
     * @param city the string to search for in the origin city
     * @return a list of TicketDTO objects matching the search criteria
     * @throws MissingDataException if the provided city string is empty
     * @throws NotFoundException if no tickets are found matching the criteria
     */
    public List<TicketRetrieveDTO> findByOriginContaining(String city) {
        if (city.isEmpty()){
            throw new MissingDataException("No city name received.");
        }

        List<TicketRetrieveDTO> response = new ArrayList<>();
        List<Ticket> result = ticketServ.findByOriginContaining(city);
        if (result.isEmpty()){
            throw new NotFoundException();
        }

        result.forEach(ticket -> {
            TicketRetrieveDTO dto = TicketRetrieveDTO.builder()
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

    /**
     * Finds tickets by destination city containing the specified string.
     *
     * @param city the string to search for in the destination city
     * @return a list of TicketDTO objects matching the search criteria
     * @throws MissingDataException if the provided city string is empty
     * @throws NotFoundException if no tickets are found matching the criteria
     */
    public List<TicketRetrieveDTO> findByDestinyContaining(String city) {
        if (city.isEmpty()){
            throw new MissingDataException("No city name received.");
        }
        List<TicketRetrieveDTO> response = new ArrayList<>();

        List<Ticket> result = ticketServ.findByDestinyContaining(city);
        if (result.isEmpty()){
            throw new NotFoundException();
        }

        result.forEach(ticket -> {
            TicketRetrieveDTO dto = TicketRetrieveDTO.builder()
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

    /**
     * Finds tickets with prices between the specified minimum and maximum values.
     *
     * @param min the minimum price
     * @param max the maximum price
     * @return a list of TicketDTO objects matching the search criteria
     * @throws MissingDataException if either min or max is negative
     * @throws NotFoundException if no tickets are found matching the criteria
     */
    public List<TicketRetrieveDTO> findByPriceBetween(double min, double max) {
        if (min < 0 || max < 0) {
            throw new MissingDataException("No prices received.");
        }
        List<TicketRetrieveDTO> response = new ArrayList<>();
        List<Ticket> result = ticketServ.findByPriceBetween(min, max);

        result.forEach(ticket -> {
            TicketRetrieveDTO dto = TicketRetrieveDTO.builder()
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

    /**
     * Finds tickets with schedules between the specified start and end dates.
     *
     * @param from the start date in "yyyyMMdd" format
     * @param to the end date in "yyyyMMdd" format
     * @return a list of TicketDTO objects matching the search criteria
     * @throws NotFoundException if no tickets are found matching the criteria
     */
    public List<TicketRetrieveDTO> findByScheduleBetween(String from, String to) {

        int fromYear = Integer.parseInt(from.substring(0, 4));
        int fromMonth = Integer.parseInt(from.substring(4, 6));
        int fromDay = Integer.parseInt(from.substring(6, 8));
        int toYear = Integer.parseInt(to.substring(0, 4));
        int toMonth = Integer.parseInt(to.substring(4, 6));
        int toDay = Integer.parseInt(to.substring(6, 8));

        LocalDateTime dateFrom = LocalDateTime.of(fromYear, fromMonth, fromDay,0, 0 ,0);
        LocalDateTime dateTo = LocalDateTime.of(toYear, toMonth, toDay, 23, 59, 59);

        List<TicketRetrieveDTO> response = new ArrayList<>();
        List<Ticket> result = ticketServ.findByScheduleBetween(dateFrom,dateTo);

        if (result.isEmpty()){
            throw new NotFoundException();
        }
        result.forEach(ticket -> {
            TicketRetrieveDTO dto = TicketRetrieveDTO.builder()
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

    /**
     * Finds tickets by airline name containing the specified string.
     *
     * @param airline the string to search for in the airline name
     * @return a list of TicketDTO objects matching the search criteria
     * @throws MissingDataException if the provided airline string is empty
     * @throws NotFoundException if no tickets are found matching the criteria
     */
    public List<TicketRetrieveDTO> findByAirlineNameContaining(String airline) {
        if (airline.isEmpty()){
            throw new MissingDataException("No Airline name received.");
        }

        List<TicketRetrieveDTO> response = new ArrayList<>();
        List<Ticket> result = ticketServ.findByAirlineNameContaining(airline);

        if (result.isEmpty()){
            throw new NotFoundException();
        }
        result.forEach(ticket -> {
            TicketRetrieveDTO dto = TicketRetrieveDTO.builder()
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

    /**
     * Finds tickets by airplane ID.
     *
     * @param airplaneID the UUID of the airplane
     * @return a list of TicketDTO objects matching the search criteria
     * @throws MissingDataException if the provided airplane ID is null
     * @throws NotFoundException if no tickets are found matching the criteria
     */
    public List<TicketRetrieveDTO> findByAirplane(UUID airplaneID) {
        if (airplaneID == null || airplaneID.toString().isEmpty()){
            throw new MissingDataException("No Airplane ID received.");
        }

        List<TicketRetrieveDTO> response = new ArrayList<>();
        List<Ticket> result = ticketServ.findByAirplane(airplaneID);

        if (result.isEmpty()){
            throw new NotFoundException();
        }
        result.forEach(ticket -> {
            TicketRetrieveDTO dto = TicketRetrieveDTO.builder()
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

    /**
     * Finds tickets by origin airport ID.
     *
     * @param id the UUID of the origin airport
     * @return a list of TicketDTO objects matching the search criteria
     * @throws MissingDataException if the provided airport ID is null or empty
     * @throws NotFoundException if no tickets are found matching the criteria
     */
    public List<TicketRetrieveDTO> findByOriginAirport(UUID id) {
        if (id == null || id.toString().isEmpty()){
            throw new MissingDataException("No Airport ID received.");
        }

        List<TicketRetrieveDTO> response = new ArrayList<>();
        List<Ticket> result = ticketServ.findByOriginAirportID(id);

        if (result.isEmpty()){
            throw new NotFoundException();
        }
        result.forEach(ticket -> {
            TicketRetrieveDTO dto = TicketRetrieveDTO.builder()
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
   /**
     * Finds a ticket by airplane ID and seat number.
     *
     * @param airplaneId the UUID of the airplane
     * @param seat the seat number
     * @return the found TicketDTO object
     * @throws MissingDataException if the provided airplane ID is null or the seat number is negative
     * @throws NotFoundException if no ticket is found matching the criteria
     */
    public TicketRetrieveDTO findByAirplaneAndSeat(UUID airplaneId, int seat){
        if (airplaneId == null || airplaneId.toString().isEmpty() || seat < 0){
            throw new MissingDataException("Check Airplane ID or seat number.");
        }
        Ticket result = ticketServ.findByAirplaneIDAndSeat(airplaneId, seat).orElseThrow(NotFoundException::new);

        return TicketRetrieveDTO.builder()
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

    /**
     * Finds tickets by destination airport ID.
     *
     * @param id the UUID of the destination airport
     * @return a list of TicketDTO objects matching the search criteria
     * @throws MissingDataException if the provided airport ID is null or empty
     * @throws NotFoundException if no tickets are found matching the criteria
     */
    public List<TicketRetrieveDTO> findByDestinyAirport(UUID id) {
        if (id == null || id.toString().isEmpty()){
            throw new MissingDataException("No Airport ID received.");
        }

        List<TicketRetrieveDTO> response = new ArrayList<>();
        List<Ticket> result = ticketServ.findByDestinyAirportID(id);

        if (result.isEmpty()){
            throw new NotFoundException();
        }
        result.forEach(ticket -> {
            TicketRetrieveDTO dto = TicketRetrieveDTO.builder()
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

    /**
     * Updates an existing ticket with the provided TicketDTO object.
     *
     * @param dto the TicketDTO object containing updated ticket details
     * @param id the UUID of the ticket to update
     * @return the updated TicketDTO object
     * @throws MissingDataException if the provided ID is null
     */
    @Transactional
    public TicketRetrieveDTO update(TicketRetrieveDTO dto, UUID id) {
        if (id == null || id.toString().isEmpty()) {
            throw new MissingDataException("No ID received.");
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
        if (dto.getSchedule() != null && !dto.getSchedule().isAfter(LocalDateTime.now())){
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

        return TicketRetrieveDTO.builder()
                .code(ticket.getCode())
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

    /**
     * Deletes a ticket by its ID.
     *
     * @param id the ticket ID
     * @throws NotFoundException if the ticket does not exist
     */
    @Transactional
    public void delete(UUID id) {
        this.findById(id);

        ticketServ.deleteById(id);
    }

}
