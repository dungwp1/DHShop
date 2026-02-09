package vn.dh_shop.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.dh_shop.config.VnpayConfig;
import vn.dh_shop.dto.common.ApiResponse;
import vn.dh_shop.dto.payment.PaymentRequestDTO;
import vn.dh_shop.dto.payment.PaymentResponseDTO;
import vn.dh_shop.service.PaymentService;
import vn.dh_shop.util.VnpayUtil;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static java.lang.System.out;

@RestController
@RequestMapping(value = "/api/payments")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PaymentController {
    private final PaymentService paymentService;
    private final VnpayConfig vnpayConfig;

    @PostMapping("/vnpay")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<PaymentResponseDTO>> payWithVnpay (@Valid @RequestBody PaymentRequestDTO request) {
        PaymentResponseDTO response = paymentService.createVNpayPaymentUrl(request);
        log.info("createVNpayPaymentUrl: {}", response);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(HttpStatus.CREATED.value(), "CREATE_VNPAY_PAYMENT_URL_SUCCESS", response));
    }

    @GetMapping("/vnpay/return")
    public void vnpayReturnUrl(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String redirectUrl = paymentService.createVNpayReturnUrl(request);
        // redirect browser về FE
        response.sendRedirect(redirectUrl);
    }


    @GetMapping("/vnpay/ipn")
    public Map<String, String> vnpayIpn(HttpServletRequest request) {
        Map<String,String> response = paymentService.handleIpnUrl(request);
        log.info(response.get("RspCode"));
        log.info(response.get("Message"));
        return response; // Spring sẽ tự convert Map này thành JSON cho VNPAY đọc
    }


}
