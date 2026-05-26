package com.hospital.patient.security;

import com.hospital.patient.dto.AuthValidationResponse;
import com.hospital.patient.exception.AuthServiceUnavailableException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AuthValidationClient {

    private final RestTemplate restTemplate;

    @Value("${auth.service.url}")
    private String authServiceUrl;

    public AuthPrincipal validate(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);

            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<AuthValidationResponse> response = restTemplate.exchange(
                    authServiceUrl + "/api/auth/validate",
                    HttpMethod.GET,
                    requestEntity,
                    AuthValidationResponse.class
            );

            AuthValidationResponse body = response.getBody();

            if (body == null || !Boolean.TRUE.equals(body.valid())) {
                return null;
            }

            return new AuthPrincipal(body.username(), body.role());
        } catch (HttpClientErrorException.Unauthorized ex) {
            return null;
        } catch (RestClientException ex) {
            throw new AuthServiceUnavailableException("Auth service is unavailable");
        }
    }

}
