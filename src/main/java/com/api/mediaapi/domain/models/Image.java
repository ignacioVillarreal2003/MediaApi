package com.api.mediaapi.domain.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(
        name = "images",
        indexes = @Index(name = "idx_reference_order", columnList = "reference_id, order_index"),
        uniqueConstraints = @UniqueConstraint(name = "uk_reference_order", columnNames = {"reference_id", "order_index"})
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private UUID referenceId;

    @Column(nullable = false)
    private Integer orderIndex;
}
