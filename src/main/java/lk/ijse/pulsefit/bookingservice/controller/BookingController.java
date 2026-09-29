package lk.ijse.pulsefit.bookingservice.controller;

import jakarta.validation.Valid;
import lk.ijse.pulsefit.bookingservice.dto.BookingDtos.BookingRequest;
import lk.ijse.pulsefit.bookingservice.dto.BookingDtos.BookingResponse;
import lk.ijse.pulsefit.bookingservice.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingResponse> create(@Valid @RequestBody BookingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.create(request));
    }

    @GetMapping
    public List<BookingResponse> findAll() {
        return bookingService.findAll();
    }

    @GetMapping("/{id}")
    public BookingResponse findById(@PathVariable String id) {
        return bookingService.findById(id);
    }

    @GetMapping("/member/{memberId}")
    public List<BookingResponse> findByMember(@PathVariable Long memberId) {
        return bookingService.findByMember(memberId);
    }

    @PutMapping("/{id}/cancel")
    public BookingResponse cancel(@PathVariable String id) {
        return bookingService.cancel(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        bookingService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
