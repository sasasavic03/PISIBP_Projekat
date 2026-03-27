package org.instagram.apigateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "gateway")
public class PublicEndpointsProperties {

    private static final Logger logger = LoggerFactory.getLogger(PublicEndpointsProperties.class);

    private List<String> publicEndpoints = new ArrayList<>();

    public PublicEndpointsProperties() {
    }

    public List<String> getPublicEndpoints() {
        if (publicEndpoints == null) {
            publicEndpoints = new ArrayList<>();
        }
        return publicEndpoints;
    }

    public void setPublicEndpoints(List<String> publicEndpoints) {
        this.publicEndpoints = publicEndpoints;
        logger.debug("Set public endpoints: {}", publicEndpoints);
    }

    @Override
    public String toString() {
        return "PublicEndpointsProperties{" +
                "publicEndpoints=" + publicEndpoints +
                '}';
    }
}

