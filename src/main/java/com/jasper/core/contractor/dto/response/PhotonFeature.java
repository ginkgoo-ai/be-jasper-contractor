package com.jasper.core.contractor.dto.response;

import lombok.Data;

@Data
public class PhotonFeature {
    private String type;
    private PhotonGeometry geometry;
    private PhotonProperty properties;
}
