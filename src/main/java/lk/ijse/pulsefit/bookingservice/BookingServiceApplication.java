package lk.ijse.pulsefit.bookingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * PulseFit - Booking Service.
 * Owns class bookings (MongoDB - the non-relational database requirement).
 * Registers with Eureka as BOOKING-SERVICE. Before saving a booking it
 * calls member-service and class-service directly (through a
 * @LoadBalanced RestClient resolved via Eureka) to confirm both exist,
 * then writes a lightweight audit event to Firestore.
 */
@SpringBootApplication
public class BookingServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BookingServiceApplication.class, args);
    }
}
