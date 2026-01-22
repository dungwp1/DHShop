package vn.dh_shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.dh_shop.dto.cart.checkout.CheckoutResponseDTO;
import vn.dh_shop.dto.common.ApiResponse;
import vn.dh_shop.service.CheckoutService;

@RestController
@RequestMapping("/api/checkout")
@RequiredArgsConstructor
public class CheckoutController {
    private final CheckoutService checkoutService;

    @PostMapping()
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<CheckoutResponseDTO>> checkoutPreview () {
        CheckoutResponseDTO response = checkoutService.checkoutPreview();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "CREATE_CHECKOUT_PREVIEW_SUCCESS", response));
    }

}
