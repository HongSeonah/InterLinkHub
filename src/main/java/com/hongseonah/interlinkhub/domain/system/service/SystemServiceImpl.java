package com.hongseonah.interlinkhub.domain.system.service;

import com.hongseonah.interlinkhub.common.response.PageResponse;
import com.hongseonah.interlinkhub.domain.system.dto.request.SystemCreateRequest;
import com.hongseonah.interlinkhub.domain.system.dto.request.SystemUpdateRequest;
import com.hongseonah.interlinkhub.domain.system.dto.response.SystemResponse;
import com.hongseonah.interlinkhub.domain.system.entity.ManagedSystem;
import com.hongseonah.interlinkhub.domain.system.repository.SystemRepository;
import com.hongseonah.interlinkhub.global.exception.BusinessException;
import com.hongseonah.interlinkhub.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SystemServiceImpl implements SystemService {

    private final SystemRepository systemRepository;

    @Override
    @Transactional
    public SystemResponse create(SystemCreateRequest request) {
        if (systemRepository.existsBySystemCode(request.systemCode())) {
            throw new BusinessException(ErrorCode.DUPLICATE_SYSTEM_CODE);
        }

        ManagedSystem system = new ManagedSystem();
        system.setSystemCode(request.systemCode());
        system.setSystemName(request.systemName());
        system.setSystemType(request.systemType());
        system.setDescription(request.description());
        system.setStatus(request.status());

        return SystemResponse.from(systemRepository.save(system));
    }

    @Override
    public PageResponse<SystemResponse> findAll(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ManagedSystem> result = (keyword == null || keyword.isBlank())
                ? systemRepository.findAll(pageable)
                : systemRepository.findBySystemNameContainingIgnoreCaseOrSystemCodeContainingIgnoreCase(
                keyword,
                keyword,
                pageable
        );

        return PageResponse.<SystemResponse>builder()
                .content(result.getContent().stream()
                        .map(SystemResponse::from)
                        .toList())
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();
    }

    @Override
    public SystemResponse findById(Long id) {
        ManagedSystem system = systemRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.SYSTEM_NOT_FOUND));
        return SystemResponse.from(system);
    }

    @Override
    @Transactional
    public SystemResponse update(Long id, SystemUpdateRequest request) {
        ManagedSystem system = systemRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.SYSTEM_NOT_FOUND));

        system.setSystemName(request.systemName());
        system.setSystemType(request.systemType());
        system.setDescription(request.description());
        system.setStatus(request.status());

        return SystemResponse.from(systemRepository.save(system));
    }
}