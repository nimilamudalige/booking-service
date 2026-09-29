package lk.ijse.pulsefit.bookingservice.service;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import lk.ijse.pulsefit.bookingservice.document.Booking;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Writes a lightweight audit/event record to Firestore whenever a booking
 * is created or cancelled. MongoDB remains the source of truth for booking
 * data; Firestore is used here specifically to demonstrate the Firestore
 * resource required by the module's cloud infrastructure checklist.
 *
 * The client is created lazily and every call is wrapped in try/catch so a
 * missing GCP credential during local development never blocks creating a
 * booking - it just skips the audit write and logs a warning. Set
 * gcp.firestore.enabled=true (FIRESTORE_ENABLED env var) once deployed to
 * GCP with a real project ID.
 */
@Service
public class FirestoreAuditService {

    @Value("${gcp.firestore.enabled:false}")
    private boolean enabled;

    @Value("${gcp.firestore.collection:booking_audit_logs}")
    private String collectionName;

    private volatile Firestore firestore;

    private Firestore getFirestore() {
        if (firestore == null) {
            synchronized (this) {
                if (firestore == null) {
                    firestore = FirestoreOptions.getDefaultInstance().getService();
                }
            }
        }
        return firestore;
    }

    public void logBookingEvent(Booking booking, String eventType) {
        if (!enabled) {
            return;
        }
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("bookingId", booking.getId());
            event.put("memberId", booking.getMemberId());
            event.put("memberName", booking.getMemberName());
            event.put("classId", booking.getClassId());
            event.put("className", booking.getClassName());
            event.put("status", booking.getStatus() != null ? booking.getStatus().name() : null);
            event.put("eventType", eventType);
            event.put("timestamp", System.currentTimeMillis());
            getFirestore().collection(collectionName).document().set(event);
        } catch (Exception e) {
            System.err.println("Firestore audit log skipped (this is fine locally without GCP credentials): "
                    + e.getMessage());
        }
    }
}
