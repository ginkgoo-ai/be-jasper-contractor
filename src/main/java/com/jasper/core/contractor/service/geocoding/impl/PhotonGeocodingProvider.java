package com.jasper.core.contractor.service.geocoding.impl;

import com.jasper.core.contractor.dto.response.*;
import com.jasper.core.contractor.service.geocoding.GeocodingService;
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
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PhotonGeocodingProvider implements GeocodingService {
    private static final String API_URL="https://photon.komoot.io/api/?";

    @Override
    public Optional<GeoLocation> geocode(String addressLine) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {


            List<BasicNameValuePair> params=new ArrayList<>();
            params.add(new BasicNameValuePair("q", addressLine));
            params.add(new BasicNameValuePair("limit", "1"));
            String qureyString=URLEncodedUtils.format(params,"utf-8");
            String url=API_URL+qureyString;
            HttpGet get = new HttpGet(url);

            CloseableHttpResponse httpResponse = client.execute(get);
            String responseTxt = EntityUtils.toString(httpResponse.getEntity());
            PhotonResponse response = JsonUtils.parseJson(responseTxt, PhotonResponse.class);
            List<PhotonFeature> result = response.getFeatures();
            if (!CollectionUtils.isEmpty(result)) {
                PhotonFeature feature = result.getFirst();
                if (feature.getGeometry() != null) {
                    PhotonGeometry geometry = feature.getGeometry();
                    GeoLocation geoLocation = new GeoLocation();
                    geoLocation.setLongitude(geometry.getCoordinates().get(0));
                    geoLocation.setLatitude(geometry.getCoordinates().get(1));
                    return Optional.of(geoLocation);
                }
            }
        } catch (Exception e) {
            log.warn("Validate address failed", e);
        }

        return Optional.empty();

    }
}
