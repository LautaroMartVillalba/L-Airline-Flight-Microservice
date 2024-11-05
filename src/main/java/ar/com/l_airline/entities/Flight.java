package ar.com.l_airline.entities;

import ar.com.l_airline.enums.AirlineName;
import ar.com.l_airline.enums.City;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "entity_flight")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "air_line")
    @Enumerated(EnumType.STRING)
    private AirlineName airLine;
    @Enumerated(EnumType.STRING)
    private City origin;
    @Enumerated(EnumType.STRING)
    private City destiny;
    @Column(name = "flight_schedule")
    private LocalDateTime flightSchedule;
    private int layover;
    private double price;

}
