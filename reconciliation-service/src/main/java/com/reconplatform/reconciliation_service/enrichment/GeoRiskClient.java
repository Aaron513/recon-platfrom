package com.reconplatform.reconciliation_service.enrichment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.reconplatform.common.error.AppException;
import com.reconplatform.common.error.ErrorCode;
import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
public class GeoRiskClient {

    private static final Logger log = LoggerFactory.getLogger(GeoRiskClient.class);
    private static final long MIN_INTERVAL_MS = 1100;
    private long lastCallAt = 0;

    private final RestClient restClient;

    public GeoRiskClient(ObjectProvider<RestClient.Builder> builderProvider) {
        RestClient.Builder builder = builderProvider.getIfAvailable(RestClient::builder);
        if (builder == null) {
            builder = RestClient.builder();
        }

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(3));
        factory.setReadTimeout(Duration.ofSeconds(5));

        this.restClient = builder
                .baseUrl("https://ipwho.is")
                .requestFactory(factory)
                .build();
    }

    public String lookupCountry(String ip) {
        if (ip == null || ip.isBlank()) {
            return null;
        }
        throttle();
        try {
            IpLookup response = restClient.get()
                    .uri("/{ip}", ip)
                    .retrieve()
                    .body(IpLookup.class);

            if (response == null) {
                throw new AppException(ErrorCode.RECON_EXTERNAL_API_FAILURE);
            }
            if (!Boolean.TRUE.equals(response.success())) {
                String message = response.message();
                if ("Reserved range".equals(message) || "Invalid IP address".equals(message)) {
                    return null;                  // bad input data, not a service failure: no risk signal
                }
                log.warn("Geo lookup for {} failed: {}", ip, message);
                throw new AppException(ErrorCode.RECON_EXTERNAL_API_FAILURE);
            }
            return response.countryCode();
        } catch (RestClientException e) {         // includes the 429
            throw new AppException(ErrorCode.RECON_EXTERNAL_API_FAILURE, e);
        }
    }

    private synchronized void throttle() {
        long wait = lastCallAt + MIN_INTERVAL_MS - System.currentTimeMillis();
        if (wait > 0) {
            try {
                Thread.sleep(wait);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        lastCallAt = System.currentTimeMillis();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    record IpLookup(Boolean success, String message, @JsonProperty("country_code") String countryCode) {}
}