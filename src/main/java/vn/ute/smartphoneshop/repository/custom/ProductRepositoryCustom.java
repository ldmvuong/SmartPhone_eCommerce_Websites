package vn.ute.smartphoneshop.repository.custom;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import vn.ute.smartphoneshop.builder.ProductSearchBuilder;
import vn.ute.smartphoneshop.entity.ProductEntity;

import java.util.ArrayList;
import java.util.List;

public class ProductRepositoryCustom {
    public static Specification<ProductEntity> search(ProductSearchBuilder builder) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (builder.getName() != null && !builder.getName().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + builder.getName() + "%"));
            }
            if (builder.getProcessor() != null && !builder.getProcessor().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("processor"), builder.getProcessor()));
            }
            if (builder.getOperatingSystem() != null && !builder.getOperatingSystem().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("operatingSystem"), builder.getOperatingSystem()));
            }
            if (builder.getSim() != null && !builder.getSim().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("sim"), builder.getSim()));
            }
            if (builder.getConnectivity() != null && !builder.getConnectivity().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("connectivity"), builder.getConnectivity()));
            }
            if (builder.getCamera() != null && !builder.getCamera().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("camera"), builder.getCamera()));
            }
            if (builder.getWarrantyPeriod() != null && !builder.getWarrantyPeriod().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("warrantyPeriod"), builder.getWarrantyPeriod()));
            }
            if (builder.getBatteryCapacity() != null) {
                predicates.add(criteriaBuilder.equal(root.get("batteryCapacity"), builder.getBatteryCapacity()));
            }
            if (builder.getRating() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("rating"), builder.getRating()));
            }
            if (builder.getBrandId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("brandId"), builder.getBrandId()));
            }
            if (builder.getMinPrice() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), builder.getMinPrice()));
            }
            if (builder.getMaxPrice() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), builder.getMaxPrice()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
