package com.jasper.core.contractor.dto.response;

import lombok.Data;

@Data
public class GeoLocation {
    private double longitude;
    private double latitude;

    @Override
    public String toString() {
        return "GeoLocation{" +
                "lng=" + longitude +
                ", lat=" + latitude +
                '}';
    }
}
