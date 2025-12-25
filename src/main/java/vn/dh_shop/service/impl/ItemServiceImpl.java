package vn.dh_shop.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.dh_shop.dto.request.ItemRequestDTO;
import vn.dh_shop.dto.response.ItemResponseDTO;
import vn.dh_shop.dto.response.PageItemsResponseDTO;
import vn.dh_shop.entity.*;
import vn.dh_shop.exception.BadRequestException;
import vn.dh_shop.repository.*;
import vn.dh_shop.service.ItemService;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ModelRepository modelRepository;
    private final ColorRepository colorRepository;
    private final RamRepository ramRepository;
    private final StorageRepository storageRepository;

    @Override
    public ItemResponseDTO addItem(ItemRequestDTO request) {
//        Log
        log.info("cateogryId -------- {}", request.getCategoryId());

//        Check category
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Category"));
        Brand brand = brandRepository.findById(request.getBrandId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Brand"));
        Model model = modelRepository.findById(request.getModelId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Model"));
        Ram ram = ramRepository.findById(request.getRamId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Ram"));
        Color color = colorRepository.findById(request.getColorId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Color"));
        Storage storage = storageRepository.findById(request.getStorageId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Storage"));
//      Tạo entity item
        Item item = new Item();
        item.setCategory(category);
        item.setBrand(brand);
        item.setModel(model);
        item.setRam(ram);
        item.setStorage(storage);
        item.setColor(color);
        item.setPrice(request.getPrice());
        item.setDescription(request.getDescription());
//        Lưu vào db item
//        Lấy file image
        List<MultipartFile> imageFiles = request.getImages();
        if (imageFiles.isEmpty()) throw new BadRequestException("Không có ảnh");

        imageFiles.forEach(file -> {
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uniqueFileName = UUID.randomUUID().toString() + extension;

            Path path = Paths.get("src/main/resources/static/assets/items/" + uniqueFileName);
            try {
                file.transferTo(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //        Lưu ItemImage
            ItemImage image = new ItemImage();
            image.setItem(item);
            image.setURL(uniqueFileName);

            item.getImages().add(image);

        });
        Item savedItem = itemRepository.save(item);
//        Map sang response

        ItemResponseDTO response = new ItemResponseDTO();
        response.setId(savedItem.getId());
        response.setCategoryId(savedItem.getCategory().getId());
        response.setCategoryName(savedItem.getCategory().getName());
        response.setBrandId(savedItem.getBrand().getId());
        response.setBrandName(savedItem.getBrand().getName());
        response.setModelId(savedItem.getModel().getId());
        response.setModelName(savedItem.getModel().getName());
        response.setRamId(savedItem.getRam().getId());
        response.setRamName(savedItem.getRam().getName());
        response.setStorageId(savedItem.getStorage().getId());
        response.setStorageName(savedItem.getStorage().getName());
        response.setColorId(savedItem.getColor().getId());
        response.setColorName(savedItem.getColor().getName());
        response.setHexColor(savedItem.getColor().getHexColor());
        response.setPrice(savedItem.getPrice());
        response.setDescription(savedItem.getDescription());
//        Map list url image
        List<String> listURL = new ArrayList<>();
        item.getImages().forEach(i -> {
            listURL.add(i.getURL());
        });
        response.setImageURLs(listURL);

        return response;
    }

    @Override
    public PageItemsResponseDTO getAllItem(int pageNo, int pageSize) {
//        Convert pageNo = 0
        if (pageNo == 0) throw new BadRequestException("Không có trang số 0");

        Pageable page = PageRequest.of(pageNo - 1, pageSize);
        Page<Item> pageItems = itemRepository.findAll(page);

        PageItemsResponseDTO pageResponse = new PageItemsResponseDTO();
        List<ItemResponseDTO> listItem = new ArrayList<>();
        pageItems.forEach(i -> {
            ItemResponseDTO response = new ItemResponseDTO();
            response.setId(i.getId());
            response.setCategoryId(i.getCategory().getId());
            response.setCategoryName(i.getCategory().getName());
            response.setBrandId(i.getBrand().getId());
            response.setBrandName(i.getBrand().getName());
            response.setModelId(i.getModel().getId());
            response.setModelName(i.getModel().getName());
            response.setRamId(i.getRam().getId());
            response.setRamName(i.getRam().getName());
            response.setStorageId(i.getStorage().getId());
            response.setStorageName(i.getStorage().getName());
            response.setColorId(i.getColor().getId());
            response.setColorName(i.getColor().getName());
            response.setHexColor(i.getColor().getHexColor());
            response.setPrice(i.getPrice());
            response.setDescription(i.getDescription());

            List<String> listURL = new ArrayList<>();
            i.getImages().forEach(ii -> {
                listURL.add(ii.getURL());
            });
            response.setImageURLs(listURL);
            listItem.add(response);
        });

        pageResponse.setItems(listItem);
        pageResponse.setCurrentPage(pageItems.getNumber() + 1);
        pageResponse.setTotalPages(pageItems.getTotalPages());
        pageResponse.setTotalItems(pageItems.getTotalElements());
        return pageResponse;
    }

    @Override
    public ItemResponseDTO getItemById(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy item có id: " + itemId));

        ItemResponseDTO response = new ItemResponseDTO();
        response.setId(item.getId());
        response.setCategoryId(item.getCategory().getId());
        response.setCategoryName(item.getCategory().getName());
        response.setBrandId(item.getBrand().getId());
        response.setBrandName(item.getBrand().getName());
        response.setModelId(item.getModel().getId());
        response.setModelName(item.getModel().getName());
        response.setRamId(item.getRam().getId());
        response.setRamName(item.getRam().getName());
        response.setStorageId(item.getStorage().getId());
        response.setStorageName(item.getStorage().getName());
        response.setColorId(item.getColor().getId());
        response.setColorName(item.getColor().getName());
        response.setHexColor(item.getColor().getHexColor());
        response.setPrice(item.getPrice());
        response.setDescription(item.getDescription());

        List<String> listURL = new ArrayList<>();
        item.getImages().forEach(i -> {
            listURL.add(i.getURL());
        });
        response.setImageURLs(listURL);


        return response;
    }

    @Override
    public PageItemsResponseDTO getItemByCategoryId(Long categoryId, int pageNo, int pageSize) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Category"));

        Pageable page = PageRequest.of(pageNo - 1, pageSize);
        Page<Item> pageItems = itemRepository.findAllByCategoryId(categoryId, page);
        PageItemsResponseDTO pageResponse = new PageItemsResponseDTO();
        List<ItemResponseDTO> listItem = new ArrayList<>();

        pageItems.forEach(i -> {
            ItemResponseDTO response = new ItemResponseDTO();
            response.setId(i.getId());
            response.setCategoryId(i.getCategory().getId());
            response.setCategoryName(i.getCategory().getName());
            response.setBrandId(i.getBrand().getId());
            response.setBrandName(i.getBrand().getName());
            response.setModelId(i.getModel().getId());
            response.setModelName(i.getModel().getName());
            response.setRamId(i.getRam().getId());
            response.setRamName(i.getRam().getName());
            response.setStorageId(i.getStorage().getId());
            response.setStorageName(i.getStorage().getName());
            response.setColorId(i.getColor().getId());
            response.setColorName(i.getColor().getName());
            response.setHexColor(i.getColor().getHexColor());
            response.setPrice(i.getPrice());
            response.setDescription(i.getDescription());

            List<String> listURL = new ArrayList<>();
            i.getImages().forEach(ii -> {
                listURL.add(ii.getURL());
            });
            response.setImageURLs(listURL);
            listItem.add(response);
        });

        pageResponse.setItems(listItem);
        pageResponse.setCurrentPage(pageItems.getNumber() + 1);
        pageResponse.setTotalPages(pageItems.getTotalPages());
        pageResponse.setTotalItems(pageItems.getTotalElements());

        return pageResponse;
    }

    @Override
    public PageItemsResponseDTO getItemByBrandId(Long categoryId, Long brandId, int pageNo, int pageSize) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Category"));
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy brand"));
        if (brand.getCategory().getId() != categoryId)
            throw new BadRequestException("Category và Brand không trùng nhau");

        Pageable page = PageRequest.of(pageNo - 1, pageSize);
        Page<Item> pageItems = itemRepository.findAllByBrandId(brandId, page);
        PageItemsResponseDTO pageResponse = new PageItemsResponseDTO();
        List<ItemResponseDTO> listItem = new ArrayList<>();

        pageItems.forEach(i -> {
            ItemResponseDTO response = new ItemResponseDTO();
            response.setId(i.getId());
            response.setCategoryId(i.getCategory().getId());
            response.setCategoryName(i.getCategory().getName());
            response.setBrandId(i.getBrand().getId());
            response.setBrandName(i.getBrand().getName());
            response.setModelId(i.getModel().getId());
            response.setModelName(i.getModel().getName());
            response.setRamId(i.getRam().getId());
            response.setRamName(i.getRam().getName());
            response.setStorageId(i.getStorage().getId());
            response.setStorageName(i.getStorage().getName());
            response.setColorId(i.getColor().getId());
            response.setColorName(i.getColor().getName());
            response.setHexColor(i.getColor().getHexColor());
            response.setPrice(i.getPrice());
            response.setDescription(i.getDescription());

            List<String> listURL = new ArrayList<>();
            i.getImages().forEach(ii -> {
                listURL.add(ii.getURL());
            });
            response.setImageURLs(listURL);
            listItem.add(response);
        });

        pageResponse.setItems(listItem);
        pageResponse.setCurrentPage(pageItems.getNumber() + 1);
        pageResponse.setTotalPages(pageItems.getTotalPages());
        pageResponse.setTotalItems(pageItems.getTotalElements());


        return pageResponse;
    }

    @Override
    public PageItemsResponseDTO getItemByModelId(Long categoryId) {
        return null;
    }

    @Override
    public void deleteItem(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item không tìm thấy"));

        itemRepository.delete(item);
    }
}
