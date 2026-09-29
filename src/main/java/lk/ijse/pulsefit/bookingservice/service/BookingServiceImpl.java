package lk.ijse.pulsefit.bookingservice.service;

import lk.ijse.pulsefit.bookingservice.client.ClassServiceClient;
import lk.ijse.pulsefit.bookingservice.client.ClassServiceClient.ClassView;
import lk.ijse.pulsefit.bookingservice.client.MemberServiceClient;
import lk.ijse.pulsefit.bookingservice.client.MemberServiceClient.MemberView;
import lk.ijse.pulsefit.bookingservice.document.Booking;
import lk.ijse.pulsefit.bookingservice.document.BookingStatus;
import lk.ijse.pulsefit.bookingservice.dto.BookingDtos.BookingRequest;
import lk.ijse.pulsefit.bookingservice.dto.BookingDtos.BookingResponse;
import lk.ijse.pulsefit.bookingservice.exception.BookingNotFoundException;
import lk.ijse.pulsefit.bookingservice.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final MemberServiceClient memberServiceClient;
    private final ClassServiceClient classServiceClient;
    private final FirestoreAuditService firestoreAuditService;

    public BookingServiceImpl(BookingRepository bookingRepository,
                               MemberServiceClient memberServiceClient,
                               ClassServiceClient classServiceClient,
                               FirestoreAuditService firestoreAuditService) {
        this.bookingRepository = bookingRepository;
        this.memberServiceClient = memberServiceClient;
        this.classServiceClient = classServiceClient;
        this.firestoreAuditService = firestoreAuditService;
    }

    @Override
    public BookingResponse create(BookingRequest request) {
        // Cross-service validation, resolved through Eureka - this is what
        // proves member-service and class-service are reachable through
        // the platform, not just booking-service in isolation.
        MemberView member = memberServiceClient.getMember(request.getMemberId());
        ClassView fitnessClass = classServiceClient.getClass(request.getClassId());

        Instant now = Instant.now();
        Booking booking = Booking.builder()
                .memberId(member.id())
                .memberName(member.fullName())
                .classId(fitnessClass.id())
                .className(fitnessClass.className())
                .scheduleTime(fitnessClass.scheduleTime())
                .status(BookingStatus.CONFIRMED)
                .bookedAt(now)
                .updatedAt(now)
                .build();

        Booking saved = bookingRepository.save(booking);
        firestoreAuditService.logBookingEvent(saved, "BOOKING_CREATED");
        return toResponse(saved);
    }

    @Override
    public List<BookingResponse> findAll() {
        return bookingRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public BookingResponse findById(String id) {
        return toResponse(getOrThrow(id));
    }

    @Override
    public List<BookingResponse> findByMember(Long memberId) {
        return bookingRepository.findByMemberId(memberId).stream().map(this::toResponse).toList();
    }

    @Override
    public BookingResponse cancel(String id) {
        Booking booking = getOrThrow(id);
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setUpdatedAt(Instant.now());
        Booking saved = bookingRepository.save(booking);
        firestoreAuditService.logBookingEvent(saved, "BOOKING_CANCELLED");
        return toResponse(saved);
    }

    @Override
    public void delete(String id) {
        if (!bookingRepository.existsById(id)) {
            throw new BookingNotFoundException(id);
        }
        bookingRepository.deleteById(id);
    }

    private Booking getOrThrow(String id) {
        return bookingRepository.findById(id).orElseThrow(() -> new BookingNotFoundException(id));
    }

    private BookingResponse toResponse(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getMemberId(),
                booking.getMemberName(),
                booking.getClassId(),
                booking.getClassName(),
                booking.getScheduleTime(),
                booking.getStatus() != null ? booking.getStatus().name() : null,
                booking.getBookedAt()
        );
    }
}
