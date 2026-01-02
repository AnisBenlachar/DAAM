package com.ipd.energy.service;

import com.ipd.energy.entity.Product;
import com.ipd.energy.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product productDetails) {
        Product product = getProductById(id);

        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setCategory(productDetails.getCategory());
        product.setBasePrice(productDetails.getBasePrice());
        product.setUnit(productDetails.getUnit());

        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @PostConstruct
    public void initMockData() {
        if (productRepository.count() == 0) {
            Product solarPanel = new Product();
            solarPanel.setName("500W Solar Panel");
            solarPanel.setDescription("High-efficiency monocrystalline solar panel");
            solarPanel.setCategory("Solar Panels");
            solarPanel.setBasePrice(new BigDecimal("2499.00"));
            solarPanel.setUnit("panel");

            Product battery = new Product();
            battery.setName("10kWh Battery");
            battery.setDescription("Lithium-ion storage system");
            battery.setCategory("Batteries");
            battery.setBasePrice(new BigDecimal("5999.00"));
            battery.setUnit("unit");

            Product inverter = new Product();
            inverter.setName("Hybrid Inverter");
            inverter.setDescription("Smart grid-connected converter");
            inverter.setCategory("Inverters");
            inverter.setBasePrice(new BigDecimal("3499.00"));
            inverter.setUnit("unit");

            productRepository.saveAll(Arrays.asList(solarPanel, battery, inverter));
            System.out.println("Mock Product data initialized");
        }
    }
}
