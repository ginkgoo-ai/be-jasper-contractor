package com.jasper.core.contractor.service.geocoding;

import com.jasper.core.contractor.dto.response.GeoLocation;

import java.util.Optional;

public interface GeocodingService {
    Optional<GeoLocation> geocode(String addressLine, String locality);
}
