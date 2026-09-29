package lk.ijse.pulsefit.bookingservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

public class BookingDtos {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookingRequest {

        @NotNull(message = "memberId is required")
        private Long memberId;

        @NotNull(message = "classId is required")
        private Long classId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookingResponse {
        private String id;
        private Long memberId;
        private String memberName;
        private Long classId;
        private String className;
        private LocalDateTime scheduleTime;
        private String status;
        private Instant bookedAt;
    }
}
