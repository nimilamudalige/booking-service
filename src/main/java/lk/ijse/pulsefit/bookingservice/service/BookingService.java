package lk.ijse.pulsefit.bookingservice.service;

import lk.ijse.pulsefit.bookingservice.dto.BookingDtos.BookingRequest;
import lk.ijse.pulsefit.bookingservice.dto.BookingDtos.BookingResponse;

import java.util.List;

public interface BookingService {
    BookingResponse create(BookingRequest request);
    List<BookingResponse> findAll();
    BookingResponse findById(String id);
    List<BookingResponse> findByMember(Long memberId);
    BookingResponse cancel(String id);
    void delete(String id);
}
