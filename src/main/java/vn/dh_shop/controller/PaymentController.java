package vn.dh_shop.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.dh_shop.dto.common.ApiResponse;
import vn.dh_shop.dto.payment.PaymentRequestDTO;
import vn.dh_shop.dto.payment.PaymentResponseDTO;
import vn.dh_shop.service.PaymentService;
import java.util.*;

@RestController
@RequestMapping(value = "/api/payments")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PaymentController {
    private final PaymentService paymentService;
    @PostMapping("/vnpay")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<PaymentResponseDTO>> payWithVnpay (@Valid @RequestBody PaymentRequestDTO request) {
        PaymentResponseDTO response = paymentService.payWithVnpay(request);
        log.info("paywithVnpay: {}", response);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(HttpStatus.CREATED.value(), "PAYMENT_SUCCESS", response));
    }

}
