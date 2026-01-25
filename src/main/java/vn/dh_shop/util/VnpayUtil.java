package vn.dh_shop.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vn.dh_shop.config.VnpayConfig;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class VnpayUtil {

    private final VnpayConfig config;

    public String buildPaymentUrl(Map<String, String> params) {
        try {
            // 1. Sắp xếp tham số theo Alphabet (Dùng TreeMap là chuẩn rồi)
            Map<String, String> sorted = new TreeMap<>(params);

            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();

            for (Map.Entry<String, String> entry : sorted.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                if (value != null && !value.isEmpty()) {
                    // 2. Encode cả Key và Value
                    String encodedKey = URLEncoder.encode(key, StandardCharsets.UTF_8.toString());
                    String encodedValue = URLEncoder.encode(value, StandardCharsets.UTF_8.toString());

                    // 3. QUAN TRỌNG: Chuyển đổi dấu "+" thành "%20" cho đúng chuẩn VNPAY
                    encodedKey = encodedKey.replace("+", "%20");
                    encodedValue = encodedValue.replace("+", "%20");

                    // 4. Build đồng thời cả HashData và QueryString
                    hashData.append(encodedKey).append("=").append(encodedValue);
                    query.append(encodedKey).append("=").append(encodedValue);

                    hashData.append("&");
                    query.append("&");
                }
            }

            // Xóa dấu & thừa ở cuối
            hashData.deleteCharAt(hashData.length() - 1);
            query.deleteCharAt(query.length() - 1);

            // 5. Tính toán mã băm từ chuỗi đã được chuẩn hóa (đã có %20)
            String secureHash = hmacSHA512(config.getHashSecret(), hashData.toString());

            log.info("VNPAY HASH DATA (Fixed): {}", hashData);

            // 6. Trả về URL (Bỏ SecureHashType)
            return config.getPayUrl() + "?" + query.toString() + "&vnp_SecureHash=" + secureHash;

        } catch (Exception e) {
            log.error("Lỗi build URL VNPAY", e);
            throw new RuntimeException(e);
        }
    }


    public static String hmacSHA512(final String key, final String data) {
        try {
            if (key == null || data == null) throw new NullPointerException();
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes();
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Exception ex) {
            return "";
        }
    }


}
