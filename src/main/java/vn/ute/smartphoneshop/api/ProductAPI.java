package vn.ute.smartphoneshop.api;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.ute.smartphoneshop.model.dto.ProductDTO;
import vn.ute.smartphoneshop.repository.ProductRepository;
import vn.ute.smartphoneshop.service.ProductService;

import java.util.List;
import java.util.Map;

@RestController
@Transactional
public class ProductAPI {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping(value = "/api/product")
    public List<ProductDTO> getProduct(@RequestParam Map<String, Object> params) {
    List<ProductDTO> products = productService.findAll(params);
    return products;
    }
}
