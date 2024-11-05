package vn.ute.smartphoneshop;

import org.springframework.boot.CommandLineRunner;
import jakarta.annotation.Resource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import vn.ute.smartphoneshop.service.IBrandService;

@SpringBootApplication
public class SmartPhoneShopApplication implements CommandLineRunner {
	@Resource
	IBrandService brandService;

	public static void main(String[] args) {
		SpringApplication.run(SmartPhoneShopApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		brandService.init();
	}
}
