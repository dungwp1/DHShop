package vn.dh_shop.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class ItemRequestDTO implements Serializable {
    private Long categoryId;
    private Long brandId;
    private Long modelId;
    private Long ramId;
    private Long storageId;
    private Long colorId;
    private Long price;
    private String description;
    private List<MultipartFile> images;
}
