package lk.ijse.pulsefit.bookingservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestClient;

/**
 * Two RestClient.Builder beans on purpose.
 *
 * "restClientBuilder" is a PLAIN builder, marked @Primary. Eureka's own
 * internal HTTP client (the one used to talk to the Eureka SERVER, not to
 * our services) asks Spring for a RestClient.Builder with no qualifier, so
 * it gets this plain one by default.
 *
 * "loadBalancedRestClientBuilder" is the @LoadBalanced one: Spring Cloud
 * LoadBalancer intercepts each request made through it and resolves the
 * host segment of the URL (e.g. "MEMBER-SERVICE") against the Eureka
 * registry, so calls automatically spread across however many instances
 * that service's Managed Instance Group is currently running.
 *
 * If there were only ONE RestClient.Builder bean and it was the
 * @LoadBalanced one, Spring would wire that SAME bean into Eureka's own
 * internal client too - every registry fetch would then be routed through
 * the load-balancer interceptor, which tries to "resolve" the Eureka
 * server's own hostname as if it were a discoverable service. That lookup
 * itself needs the eurekaClient bean, which is the exact bean still being
 * constructed: a circular BeanCurrentlyInCreationException that repeats
 * forever and silently stops this service from ever registering with
 * Eureka. Two beans - one plain and @Primary, one @LoadBalanced - avoids
 * this entirely.
 */
@Configuration
public class RestClientConfig {

    @Bean
    @Primary
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }

    @Bean
    @LoadBalanced
    public RestClient.Builder loadBalancedRestClientBuilder() {
        return RestClient.builder();
    }
}
