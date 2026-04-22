package com.hongseonah.interlinkhub.domain.interfaceinfo.service;

import com.hongseonah.interlinkhub.common.response.PageResponse;
import com.hongseonah.interlinkhub.domain.interfaceinfo.dto.request.InterfaceCreateRequest;
import com.hongseonah.interlinkhub.domain.interfaceinfo.dto.request.InterfaceStatusUpdateRequest;
import com.hongseonah.interlinkhub.domain.interfaceinfo.dto.request.InterfaceUpdateRequest;
import com.hongseonah.interlinkhub.domain.interfaceinfo.dto.response.InterfaceResponse;
import com.hongseonah.interlinkhub.domain.interfaceinfo.entity.ManagedInterface;
import com.hongseonah.interlinkhub.domain.interfaceinfo.repository.InterfaceRepository;
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
public class InterfaceServiceImpl implements InterfaceService {

    private final InterfaceRepository interfaceRepository;
    private final SystemRepository systemRepository;

    @Override
    @Transactional
    public InterfaceResponse create(InterfaceCreateRequest request) {
        validateDuplicateCode(request.interfaceCode());

        ManagedSystem sourceSystem = systemRepository.findById(request.sourceSystemId())
                .orElseThrow(() -> new BusinessException(ErrorCode.SOURCE_SYSTEM_NOT_FOUND));
        ManagedSystem targetSystem = systemRepository.findById(request.targetSystemId())
                .orElseThrow(() -> new BusinessException(ErrorCode.TARGET_SYSTEM_NOT_FOUND));
        validateDifferentSystems(sourceSystem.getId(), targetSystem.getId());

        ManagedInterface managedInterface = new ManagedInterface();
        managedInterface.setInterfaceCode(request.interfaceCode());
        managedInterface.setInterfaceName(request.interfaceName());
        managedInterface.setDescription(request.description());
        managedInterface.setProtocolType(request.protocolType());
        managedInterface.setSourceSystem(sourceSystem);
        managedInterface.setTargetSystem(targetSystem);
        managedInterface.setOwnerName(request.ownerName());
        managedInterface.setStatus(request.status());

        return InterfaceResponse.from(interfaceRepository.save(managedInterface));
    }

    @Override
    public PageResponse<InterfaceResponse> findAll(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ManagedInterface> result = (keyword == null || keyword.isBlank())
                ? interfaceRepository.findAll(pageable)
                : interfaceRepository.findByInterfaceNameContainingIgnoreCaseOrInterfaceCodeContainingIgnoreCase(
                keyword,
                keyword,
                pageable
        );

        return PageResponse.<InterfaceResponse>builder()
                .content(result.getContent().stream().map(InterfaceResponse::from).toList())
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();
    }

    @Override
    public InterfaceResponse findById(Long id) {
        return InterfaceResponse.from(getInterface(id));
    }

    @Override
    @Transactional
    public InterfaceResponse update(Long id, InterfaceUpdateRequest request) {
        ManagedInterface managedInterface = getInterface(id);

        ManagedSystem sourceSystem = systemRepository.findById(request.sourceSystemId())
                .orElseThrow(() -> new BusinessException(ErrorCode.SOURCE_SYSTEM_NOT_FOUND));
        ManagedSystem targetSystem = systemRepository.findById(request.targetSystemId())
                .orElseThrow(() -> new BusinessException(ErrorCode.TARGET_SYSTEM_NOT_FOUND));
        validateDifferentSystems(sourceSystem.getId(), targetSystem.getId());

        managedInterface.setInterfaceName(request.interfaceName());
        managedInterface.setDescription(request.description());
        managedInterface.setProtocolType(request.protocolType());
        managedInterface.setSourceSystem(sourceSystem);
        managedInterface.setTargetSystem(targetSystem);
        managedInterface.setOwnerName(request.ownerName());
        managedInterface.setStatus(request.status());

        return InterfaceResponse.from(interfaceRepository.save(managedInterface));
    }

    @Override
    @Transactional
    public InterfaceResponse updateStatus(Long id, InterfaceStatusUpdateRequest request) {
        ManagedInterface managedInterface = getInterface(id);
        managedInterface.setStatus(request.status());
        return InterfaceResponse.from(interfaceRepository.save(managedInterface));
    }

    private ManagedInterface getInterface(Long id) {
        return interfaceRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.INTERFACE_NOT_FOUND));
    }

    private void validateDuplicateCode(String interfaceCode) {
        if (interfaceRepository.existsByInterfaceCode(interfaceCode)) {
            throw new BusinessException(ErrorCode.DUPLICATE_INTERFACE_CODE);
        }
    }

    private void validateDifferentSystems(Long sourceSystemId, Long targetSystemId) {
        if (sourceSystemId.equals(targetSystemId)) {
            throw new BusinessException(ErrorCode.SAME_SYSTEM_NOT_ALLOWED);
        }
    }
}