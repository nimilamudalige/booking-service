package lk.ijse.pulsefit.bookingservice.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lk.ijse.pulsefit.bookingservice.exception.ClassServiceException;
import org.springframework.stereotype.Component;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.time.LocalDateTime;

/**
 * Calls class-service by its Eureka logical name (CLASS-SERVICE) to
 * confirm a fitness class exists, and to snapshot its schedule, before a
 * booking is created.
 */
@Component
public class ClassServiceClient {

    private final RestClient restClient;

    public ClassServiceClient(@LoadBalanced RestClient.Builder loadBalancedRestClientBuilder) {
        this.restClient = loadBalancedRestClientBuilder.baseUrl("http://CLASS-SERVICE").build();
    }

    public ClassView getClass(Long classId) {
        try {
            ClassView fitnessClass = restClient.get()
                    .uri("/api/classes/{id}", classId)
                    .retrieve()
                    .body(ClassView.class);
            if (fitnessClass == null) {
                throw new ClassServiceException("class-service returned an empty response for class " + classId);
            }
            return fitnessClass;
        } catch (RestClientException ex) {
            throw new ClassServiceException(
                    "Could not verify class " + classId + " via class-service: " + ex.getMessage());
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ClassView(Long id, String className, LocalDateTime scheduleTime, Integer capacity) {
    }
}
