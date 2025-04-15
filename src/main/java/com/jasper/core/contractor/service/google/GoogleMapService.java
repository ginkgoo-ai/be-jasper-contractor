package com.jasper.core.contractor.service.google;

import com.jasper.core.contractor.dto.request.Address;
import com.jasper.core.contractor.dto.request.AddressValidationRequest;
import com.jasper.core.contractor.dto.response.AddressValidationResponse;
import com.jasper.core.contractor.dto.response.AddressValidationResult;
import com.jasper.core.contractor.dto.response.GeoLocation;
import com.jasper.core.contractor.dto.response.Verdict;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.flywaydb.core.internal.util.JsonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleMapService {

    private static final String API_URL = "https://addressvalidation.googleapis.com/v1:validateAddress?key=";

    private static final String DEFAULT_REGION_CODE = "US";

    @Value("${GOOGLE_MAP_API_KEY}")
    private String apiKey;

    public Optional<GeoLocation> validateAddress(String addressLine, String locality) {
        Address address = Address.builder()
                .regionCode(DEFAULT_REGION_CODE)
                .locality(locality)
                .addressLines(List.of(addressLine))
                .build();
        AddressValidationRequest request = AddressValidationRequest
                .builder()
                .address(address)
                .build();

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(API_URL + apiKey);

            StringEntity entity = new StringEntity(JsonUtils.toJson(request), ContentType.APPLICATION_JSON);

            post.setEntity(entity);
            CloseableHttpResponse httpResponse = client.execute(post);
            String responseTxt = EntityUtils.toString(httpResponse.getEntity());
            AddressValidationResponse response = JsonUtils.parseJson(responseTxt, AddressValidationResponse.class);
            AddressValidationResult result = response.getResult();
            if (result != null) {
                Verdict verdict = result.getVerdict();
                if (verdict != null && Boolean.TRUE.equals(verdict.getAddressComplete())) {
                    return Optional.ofNullable(result.getGeocode().getLocation());
                }
            }
        } catch (Exception e) {
            log.warn("Validate address failed", e);
        }

        return Optional.empty();
    }
}
