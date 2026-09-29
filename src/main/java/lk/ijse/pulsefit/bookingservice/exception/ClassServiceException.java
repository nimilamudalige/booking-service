package lk.ijse.pulsefit.bookingservice.exception;

/** Thrown when class-service cannot be reached or rejects a lookup. */
public class ClassServiceException extends RuntimeException {
    public ClassServiceException(String message) {
        super(message);
    }
}
