package com.hongseonah.interlinkhub.domain.interfaceinfo.repository;

import com.hongseonah.interlinkhub.domain.interfaceinfo.entity.InterfaceProtocolConfig;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProtocolConfigRepository extends JpaRepository<InterfaceProtocolConfig, Long> {

    Optional<InterfaceProtocolConfig> findByManagedInterfaceId(Long interfaceId);

    boolean existsByManagedInterfaceId(Long interfaceId);
}