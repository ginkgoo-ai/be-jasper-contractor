package com.jasper.core.contractor.dto.response;

import lombok.Data;

import java.util.List;
@Data
public class PhotonGeometry {
    private String type;
    private List<Double> coordinates;
}
