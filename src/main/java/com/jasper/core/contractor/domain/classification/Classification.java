package com.jasper.core.contractor.domain.classification;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Details about a contractor's classification")
@Table(name = "classification")
public class Classification {

    @Id
    @Schema(description = "The classification code")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Schema(description = "The classification name")
    private String name;
}
