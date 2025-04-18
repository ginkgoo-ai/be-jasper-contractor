package com.jasper.core.contractor.service.geocoding.impl;

import com.jasper.core.contractor.dto.request.Address;
import com.jasper.core.contractor.dto.request.AddressValidationRequest;
import com.jasper.core.contractor.dto.response.*;
import com.jasper.core.contractor.service.geocoding.GeocodingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.flywaydb.core.internal.util.JsonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleMapGeocodingProvider implements GeocodingService {

    private static final String API_URL = "https://maps.googleapis.com/maps/api/geocode/json?";

    private static final String DEFAULT_REGION_CODE = "US";

    @Value("${GOOGLE_MAP_API_KEY}")
    public String apiKey;

    @Override
    public Optional<GeoLocation> geocode(String addressLine) {


        try (CloseableHttpClient client = HttpClients.createDefault()) {


            List<BasicNameValuePair> params=new ArrayList<>();
            params.add(new BasicNameValuePair("key", apiKey));
            params.add(new BasicNameValuePair("address", addressLine));
            String qureyString= URLEncodedUtils.format(params,"utf-8");
            HttpGet get = new HttpGet(API_URL + qureyString);

            CloseableHttpResponse httpResponse = client.execute(get);
            String responseTxt = EntityUtils.toString(httpResponse.getEntity());
            GoogleGeocodingResponse response = JsonUtils.parseJson(responseTxt, GoogleGeocodingResponse.class);
            List<GoogleGeocodingResult> resultList = response.getResults();
            if (!CollectionUtils.isEmpty(resultList)) {
                GoogleGeocodingResult result=resultList.getFirst();
                return Optional.ofNullable(result.getGeometry().getLocation());
            }else{
                log.warn("No address validation result:{}", responseTxt);
            }
        } catch (Exception e) {
            log.warn("Validate address failed", e);
        }

        return Optional.empty();
    }
}
