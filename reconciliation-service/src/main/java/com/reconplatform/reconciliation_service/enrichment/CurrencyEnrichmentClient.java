package com.reconplatform.reconciliation_service.enrichment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.reconplatform.common.error.AppException;
import com.reconplatform.common.error.ErrorCode;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
public class CurrencyEnrichmentClient {

    private static final String TARGET = "INR";
    private static final long TTL_MS = Duration.ofHours(1).toMillis();

    private final ConcurrentHashMap<String, CachedRate> cache = new ConcurrentHashMap<>();
    private final RestClient restClient;

    public CurrencyEnrichmentClient(ObjectProvider<RestClient.Builder> builderProvider) {
        RestClient.Builder builder = builderProvider.getIfAvailable(RestClient::builder);
        if (builder == null) {
            builder = RestClient.builder();
        }

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(3));
        factory.setReadTimeout(Duration.ofSeconds(5));

        this.restClient = builder
                .baseUrl("https://api.frankfurter.dev")
                .requestFactory(factory)
                .build();
    }

    public BigDecimal convertToInr(BigDecimal amount, String currency) {
        if (TARGET.equals(currency)) {
            return amount.setScale(2, RoundingMode.HALF_UP);
        }
        return amount.multiply(getRate(currency)).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal getRate(String currency) {
        CachedRate cached = cache.get(currency);
        if (cached != null && !cached.isExpired()) {
            return cached.rate();
        }
        try {
            RateResponse response = restClient.get()
                    .uri("/v2/rate/{base}/{quote}", currency, TARGET)
                    .retrieve()
                    .body(RateResponse.class);

            if (response == null || response.rate() == null) {
                throw new AppException(ErrorCode.RECON_EXTERNAL_API_FAILURE);
            }
            cache.put(currency, new CachedRate(response.rate(), System.currentTimeMillis() + TTL_MS));
            return response.rate();
        } catch (RestClientException e) {
            throw new AppException(ErrorCode.RECON_EXTERNAL_API_FAILURE, e);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    record RateResponse(String date, String base, String quote, BigDecimal rate) {}

    record CachedRate(BigDecimal rate, long expiresAt) {
        boolean isExpired() {
            return System.currentTimeMillis() >= expiresAt;
        }
    }
}