package com.hongseonah.interlinkhub.domain.interfaceinfo.entity;

import com.hongseonah.interlinkhub.domain.system.entity.ManagedSystem;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "managed_interfaces")
public class ManagedInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String interfaceCode;

    @Column(nullable = false, length = 100)
    private String interfaceName;

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProtocolType protocolType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_system_id", nullable = false)
    private ManagedSystem sourceSystem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_system_id", nullable = false)
    private ManagedSystem targetSystem;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private InterfaceStatus status = InterfaceStatus.ACTIVE;

    @Column(nullable = false, length = 100)
    private String ownerName;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @PrePersist
    void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
