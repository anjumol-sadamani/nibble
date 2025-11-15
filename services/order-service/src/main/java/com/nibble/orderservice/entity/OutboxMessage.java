package com.nibble.orderservice.entity;

import com.nibble.orderservice.enums.OutboxMessageStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table
public class OutboxMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String aggregateType;
    private String aggregateId;
    private String type;
    @Column(columnDefinition = "TEXT")
    private String payload;
    @Enumerated(EnumType.STRING)
    private OutboxMessageStatus status;
    private OffsetDateTime occurredAt = OffsetDateTime.now();
    private OffsetDateTime claimedAt;
    private OffsetDateTime lastAttemptAt;
    private String claimedBy;
    private int attempts;
}
