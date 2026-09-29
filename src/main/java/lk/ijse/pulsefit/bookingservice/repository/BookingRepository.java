package lk.ijse.pulsefit.bookingservice.repository;

import lk.ijse.pulsefit.bookingservice.document.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BookingRepository extends MongoRepository<Booking, String> {
    List<Booking> findByMemberId(Long memberId);
}
