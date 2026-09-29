package lk.ijse.pulsefit.bookingservice.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDateTime;

@Document(collection = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    private String id;

    private Long memberId;
    private String memberName;

    private Long classId;
    private String className;
    private LocalDateTime scheduleTime;

    private BookingStatus status;

    private Instant bookedAt;
    private Instant updatedAt;
}
