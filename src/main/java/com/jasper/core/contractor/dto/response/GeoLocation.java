package com.jasper.core.contractor.dto.response;

import lombok.Data;

@Data
public class GeoLocation {
    private double lng;
    private double lat;

    @Override
    public String toString() {
        return "GeoLocation{" +
                "lng=" + lng +
                ", lat=" + lat +
                '}';
    }
}
