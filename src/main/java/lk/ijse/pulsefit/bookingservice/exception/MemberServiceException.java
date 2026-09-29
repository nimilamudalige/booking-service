package lk.ijse.pulsefit.bookingservice.exception;

/** Thrown when member-service cannot be reached or rejects a lookup. */
public class MemberServiceException extends RuntimeException {
    public MemberServiceException(String message) {
        super(message);
    }
}
