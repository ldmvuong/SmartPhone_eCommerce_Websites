package vn.ute.smartphoneshop.builder;

public class ProductSearchBuilder {
    private String name;
    private String processor;
    private String operatingSystem;
    private String sim;
    private String connectivity;
    private String camera;
    private String warrantyPeriod;
    private Integer batteryCapacity;
    private Float rating;
    private Long brandId;
    private Long minPrice;  // Giá tối thiểu
    private Long maxPrice;  // Giá tối đa


    private ProductSearchBuilder(Builder builder) {
        this.name = builder.name;
        this.processor = builder.processor;
        this.operatingSystem = builder.operatingSystem;
        this.sim = builder.sim;
        this.connectivity = builder.connectivity;
        this.camera = builder.camera;
        this.warrantyPeriod = builder.warrantyPeriod;
        this.batteryCapacity = builder.batteryCapacity;
        this.rating = builder.rating;
        this.brandId = builder.brandId;
        this.minPrice = builder.minPrice;
        this.maxPrice = builder.maxPrice;
    }

    public String getName() {
        return name;
    }


    public String getProcessor() {
        return processor;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public String getSim() {
        return sim;
    }

    public String getConnectivity() {
        return connectivity;
    }

    public String getCamera() {
        return camera;
    }

    public String getWarrantyPeriod() {
        return warrantyPeriod;
    }

    public Integer getBatteryCapacity() {
        return batteryCapacity;
    }

    public Float getRating() {
        return rating;
    }

    public Long getBrandId() {
        return brandId;
    }

    public Long getMinPrice() {
        return minPrice;
    }

    public Long getMaxPrice() {
        return maxPrice;
    }


    public static class Builder {
        private String name;
        private String processor;
        private String operatingSystem;
        private String sim;
        private String connectivity;
        private String camera;
        private String warrantyPeriod;
        private Integer batteryCapacity;
        private Float rating;
        private Long brandId;
        private Long minPrice;
        private Long maxPrice;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setProcessor(String processor) {
            this.processor = processor;
            return this;
        }

        public Builder setOperatingSystem(String operatingSystem) {
            this.operatingSystem = operatingSystem;
            return this;
        }

        public Builder setSim(String sim) {
            this.sim = sim;
            return this;
        }

        public Builder setConnectivity(String connectivity) {
            this.connectivity = connectivity;
            return this;
        }

        public Builder setCamera(String camera) {
            this.camera = camera;
            return this;
        }

        public Builder setWarrantyPeriod(String warrantyPeriod) {
            this.warrantyPeriod = warrantyPeriod;
            return this;
        }

        public Builder setBatteryCapacity(Integer batteryCapacity) {
            this.batteryCapacity = batteryCapacity;
            return this;
        }

        public Builder setRating(Float rating) {
            this.rating = rating;
            return this;
        }

        public Builder setBrandId(Long brandId) {
            this.brandId = brandId;
            return this;
        }

        public Builder setMinPrice(Long minPrice) {
            this.minPrice = minPrice;
            return this;
        }

        public Builder setMaxPrice(Long maxPrice) {
            this.maxPrice = maxPrice;
            return this;
        }


        public ProductSearchBuilder build() {
            return new ProductSearchBuilder(this);
        }
    }
}


