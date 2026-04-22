package com.hongseonah.interlinkhub.domain.interfaceinfo.service;

import com.hongseonah.interlinkhub.domain.interfaceinfo.dto.request.ProtocolConfigCreateRequest;
import com.hongseonah.interlinkhub.domain.interfaceinfo.dto.request.ProtocolConfigUpdateRequest;
import com.hongseonah.interlinkhub.domain.interfaceinfo.dto.response.ProtocolConnectionTestResponse;
import com.hongseonah.interlinkhub.domain.interfaceinfo.dto.response.ProtocolConfigResponse;
import com.hongseonah.interlinkhub.domain.interfaceinfo.entity.InterfaceProtocolConfig;
import com.hongseonah.interlinkhub.domain.interfaceinfo.entity.AuthType;
import com.hongseonah.interlinkhub.domain.interfaceinfo.repository.InterfaceRepository;
import com.hongseonah.interlinkhub.domain.interfaceinfo.repository.ProtocolConfigRepository;
import com.hongseonah.interlinkhub.global.exception.BusinessException;
import com.hongseonah.interlinkhub.global.exception.ErrorCode;
import java.net.URI;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProtocolConfigServiceImpl implements ProtocolConfigService {

    private final ProtocolConfigRepository protocolConfigRepository;
    private final InterfaceRepository interfaceRepository;

    @Override
    @Transactional
    public ProtocolConfigResponse create(Long interfaceId, ProtocolConfigCreateRequest request) {
        if (protocolConfigRepository.existsByManagedInterfaceId(interfaceId)) {
            throw new BusinessException(ErrorCode.DUPLICATE_PROTOCOL_CONFIG);
        }

        InterfaceProtocolConfig config = new InterfaceProtocolConfig();
        config.setManagedInterface(interfaceRepository.findById(interfaceId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INTERFACE_NOT_FOUND)));

        verifyEndpoint(request.httpMethod(), request.endpointUrl(), request.authType(), request.authValue(),
                request.connectTimeoutMillis(), request.readTimeoutMillis());

        config.setHttpMethod(request.httpMethod());
        config.setEndpointUrl(request.endpointUrl());
        config.setAuthType(request.authType());
        config.setAuthValue(request.authValue());
        config.setConnectTimeoutMillis(request.connectTimeoutMillis());
        config.setReadTimeoutMillis(request.readTimeoutMillis());
        config.setRetryCount(request.retryCount());

        return ProtocolConfigResponse.from(protocolConfigRepository.save(config));
    }

    @Override
    public ProtocolConfigResponse findByInterfaceId(Long interfaceId) {
        return ProtocolConfigResponse.from(getConfig(interfaceId));
    }

    @Override
    @Transactional
    public ProtocolConfigResponse update(Long interfaceId, ProtocolConfigUpdateRequest request) {
        InterfaceProtocolConfig config = getConfig(interfaceId);
        verifyEndpoint(request.httpMethod(), request.endpointUrl(), request.authType(), request.authValue(),
                request.connectTimeoutMillis(), request.readTimeoutMillis());
        config.setHttpMethod(request.httpMethod());
        config.setEndpointUrl(request.endpointUrl());
        config.setAuthType(request.authType());
        config.setAuthValue(request.authValue());
        config.setConnectTimeoutMillis(request.connectTimeoutMillis());
        config.setReadTimeoutMillis(request.readTimeoutMillis());
        config.setRetryCount(request.retryCount());
        return ProtocolConfigResponse.from(protocolConfigRepository.save(config));
    }

    @Override
    public ProtocolConnectionTestResponse test(ProtocolConfigCreateRequest request) {
        ResponseEntity<String> response = callEndpoint(
                request.httpMethod(),
                request.endpointUrl(),
                request.authType(),
                request.authValue(),
                request.connectTimeoutMillis(),
                request.readTimeoutMillis()
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new BusinessException(ErrorCode.PROTOCOL_ENDPOINT_INVALID);
        }

        return new ProtocolConnectionTestResponse(
                true,
                response.getStatusCode().value(),
                "연결이 확인되었습니다.",
                response.getBody()
        );
    }

    private InterfaceProtocolConfig getConfig(Long interfaceId) {
        return protocolConfigRepository.findByManagedInterfaceId(interfaceId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROTOCOL_CONFIG_NOT_FOUND));
    }

    private void verifyEndpoint(
            String httpMethod,
            String endpointUrl,
            AuthType authType,
            String authValue,
            Integer connectTimeoutMillis,
            Integer readTimeoutMillis
    ) {
        ResponseEntity<String> response = callEndpoint(
                httpMethod,
                endpointUrl,
                authType,
                authValue,
                connectTimeoutMillis,
                readTimeoutMillis
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new BusinessException(ErrorCode.PROTOCOL_ENDPOINT_INVALID);
        }
    }

    private ResponseEntity<String> callEndpoint(
            String httpMethod,
            String endpointUrl,
            AuthType authType,
            String authValue,
            Integer connectTimeoutMillis,
            Integer readTimeoutMillis
    ) {
        try {
            HttpMethod method = HttpMethod.valueOf(httpMethod.trim().toUpperCase(Locale.ROOT));
            RestTemplate restTemplate = buildRestTemplate(connectTimeoutMillis, readTimeoutMillis);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            if (authType == AuthType.BEARER && StringUtils.hasText(authValue)) {
                headers.setBearerAuth(authValue);
            } else if (authType == AuthType.API_KEY && StringUtils.hasText(authValue)) {
                headers.add("X-API-Key", authValue);
            }

            HttpEntity<Object> entity = method == HttpMethod.GET || method == HttpMethod.HEAD
                    ? new HttpEntity<>(headers)
                    : new HttpEntity<>(java.util.Map.of("ping", "test"), headers);

            log.info("Testing endpoint: method={}, url={}", method, endpointUrl.trim());
            return restTemplate.exchange(
                    URI.create(endpointUrl.trim()),
                    method,
                    entity,
                    String.class
            );
        } catch (BusinessException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            log.warn("Invalid endpoint test request: {}", e.getMessage());
            throw new BusinessException(ErrorCode.INVALID_REQUEST);
        } catch (RestClientException e) {
            log.warn("Endpoint test failed: {}", e.getMessage());
            throw new BusinessException(ErrorCode.PROTOCOL_ENDPOINT_INVALID);
        }
    }

    private RestTemplate buildRestTemplate(Integer connectTimeoutMillis, Integer readTimeoutMillis) {
        org.springframework.http.client.SimpleClientHttpRequestFactory requestFactory =
                new org.springframework.http.client.SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(connectTimeoutMillis != null ? connectTimeoutMillis : 3_000);
        requestFactory.setReadTimeout(readTimeoutMillis != null ? readTimeoutMillis : 5_000);
        return new RestTemplate(requestFactory);
    }
}
