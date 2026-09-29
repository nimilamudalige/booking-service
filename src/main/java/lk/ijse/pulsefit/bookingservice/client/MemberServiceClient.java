package lk.ijse.pulsefit.bookingservice.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lk.ijse.pulsefit.bookingservice.exception.MemberServiceException;
import org.springframework.stereotype.Component;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

/**
 * Calls member-service by its Eureka logical name (MEMBER-SERVICE) to
 * confirm a member exists before a booking is created.
 */
@Component
public class MemberServiceClient {

    private final RestClient restClient;

    public MemberServiceClient(@LoadBalanced RestClient.Builder loadBalancedRestClientBuilder) {
        this.restClient = loadBalancedRestClientBuilder.baseUrl("http://MEMBER-SERVICE").build();
    }

    public MemberView getMember(Long memberId) {
        try {
            MemberView member = restClient.get()
                    .uri("/api/members/{id}", memberId)
                    .retrieve()
                    .body(MemberView.class);
            if (member == null) {
                throw new MemberServiceException("member-service returned an empty response for member " + memberId);
            }
            return member;
        } catch (RestClientException ex) {
            throw new MemberServiceException(
                    "Could not verify member " + memberId + " via member-service: " + ex.getMessage());
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record MemberView(Long id, String fullName, String email, boolean active) {
    }
}
