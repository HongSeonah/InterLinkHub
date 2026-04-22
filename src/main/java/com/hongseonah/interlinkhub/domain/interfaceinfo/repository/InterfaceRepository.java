package com.hongseonah.interlinkhub.domain.interfaceinfo.repository;

import com.hongseonah.interlinkhub.domain.interfaceinfo.entity.ManagedInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterfaceRepository extends JpaRepository<ManagedInterface, Long> {

    boolean existsByInterfaceCode(String interfaceCode);

    Page<ManagedInterface> findByDeletedAtIsNullAndInterfaceNameContainingIgnoreCaseOrDeletedAtIsNullAndInterfaceCodeContainingIgnoreCase(
            String interfaceName,
            String interfaceCode,
            Pageable pageable
    );

    Page<ManagedInterface> findByDeletedAtIsNull(Pageable pageable);

    boolean existsByIdAndDeletedAtIsNull(Long id);

    java.util.Optional<ManagedInterface> findByIdAndDeletedAtIsNull(Long id);
}
