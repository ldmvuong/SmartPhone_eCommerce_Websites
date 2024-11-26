package vn.ute.smartphoneshop.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSearchRequest {
    private String name;
    private String brandName;
    private String processor;
    private String operatingSystem;
    private String sim;
    private String connectivity;
    private String camera;
    private String warrantyPeriod;
    private Integer batteryCapacity;
    private Float rating;
    private Long minPrice;
    private Long maxPrice;
}

