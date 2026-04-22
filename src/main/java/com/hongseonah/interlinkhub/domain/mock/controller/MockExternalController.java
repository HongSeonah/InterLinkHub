package com.hongseonah.interlinkhub.domain.mock.controller;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mock")
public class MockExternalController {

    @PostMapping("/partners/insurance-applications")
    public ResponseEntity<?> insuranceApplications(
            @RequestHeader(value = "X-Mock-Scenario", required = false) String scenario,
            @RequestBody(required = false) Map<String, Object> body
    ) {
        if ("FAIL".equalsIgnoreCase(scenario) || isForceFail(body)) {
            return ResponseEntity.status(500).body(Map.of(
                    "resultCode", "9999",
                    "resultMessage", "제휴사 처리 실패"
            ));
        }

        return ResponseEntity.ok(Map.of(
                "resultCode", "0000",
                "resultMessage", "제휴사 처리 성공"
        ));
    }

    @PostMapping("/agencies/underwriting-results")
    public ResponseEntity<?> underwritingResults(
            @RequestHeader(value = "X-Mock-Scenario", required = false) String scenario,
            @RequestBody(required = false) Map<String, Object> body
    ) {
        if ("FAIL".equalsIgnoreCase(scenario) || isForceFail(body)) {
            return ResponseEntity.status(500).body(Map.of(
                    "resultCode", "9999",
                    "resultMessage", "심사 결과 처리 실패"
            ));
        }

        return ResponseEntity.ok(Map.of(
                "resultCode", "0000",
                "resultMessage", "심사 결과 처리 성공"
        ));
    }

    @PostMapping("/regulator/report")
    public ResponseEntity<?> regulatorReport(
            @RequestHeader(value = "X-Mock-Scenario", required = false) String scenario,
            @RequestBody(required = false) Map<String, Object> body
    ) {
        if ("FAIL".equalsIgnoreCase(scenario) || isForceFail(body)) {
            return ResponseEntity.status(500).body(Map.of(
                    "resultCode", "9999",
                    "resultMessage", "보고 실패"
            ));
        }

        return ResponseEntity.ok(Map.of(
                "resultCode", "0000",
                "resultMessage", "보고 성공"
        ));
    }

    private boolean isForceFail(Map<String, Object> body) {
        if (body == null) {
            return false;
        }
        Object forceFail = body.get("forceFail");
        return forceFail instanceof Boolean bool ? bool : "true".equalsIgnoreCase(String.valueOf(forceFail));
    }
}