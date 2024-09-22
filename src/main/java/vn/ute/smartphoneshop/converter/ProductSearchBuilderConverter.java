package vn.ute.smartphoneshop.converter;

import org.springframework.stereotype.Component;
import vn.ute.smartphoneshop.builder.ProductSearchBuilder;
import vn.ute.smartphoneshop.utils.MapUtil;

import java.util.Map;

@Component
public class ProductSearchBuilderConverter {

    public ProductSearchBuilder toProductSearchBuilder(Map<String, Object> params) {
        ProductSearchBuilder productSearchBuilder = new ProductSearchBuilder.Builder()
                .setName(MapUtil.getObject(params, "name", String.class))                      // Tên sản phẩm
                .setBrand(MapUtil.getObject(params, "brand", String.class))                    // Thương hiệu
                .setProcessor(MapUtil.getObject(params, "processor", String.class))            // Loại chip
                .setOperatingSystem(MapUtil.getObject(params, "operatingSystem", String.class))// Hệ điều hành
                .setSim(MapUtil.getObject(params, "sim", String.class))                        // Loại SIM
                .setConnectivity(MapUtil.getObject(params, "connectivity", String.class))      // Kết nối
                .setCamera(MapUtil.getObject(params, "camera", String.class))                  // Camera
                .setWarrantyPeriod(MapUtil.getObject(params, "warrantyPeriod", String.class))  // Thời gian bảo hành
                .setBatteryCapacity(MapUtil.getObject(params, "batteryCapacity", Integer.class)) // Dung lượng pin
                .setRating(MapUtil.getObject(params, "rating", Float.class))                   // Đánh giá
                .setCategoryId(MapUtil.getObject(params, "categoryId", Integer.class))         // ID danh mục
                .setMinPrice(MapUtil.getObject(params, "minPrice", Long.class))                // Giá tối thiểu
                .setMaxPrice(MapUtil.getObject(params, "maxPrice", Long.class))                // Giá tối đa
                .build();

        return productSearchBuilder;
    }
}