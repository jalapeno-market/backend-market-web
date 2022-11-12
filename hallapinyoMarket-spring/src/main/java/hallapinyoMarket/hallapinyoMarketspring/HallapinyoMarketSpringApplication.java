package hallapinyoMarket.hallapinyoMarketspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class HallapinyoMarketSpringApplication {

	public static final String APPLICATION_LOCATIONS = "spring.config.location="
			+ "classpath:application.yml,"
			+ "classpath:aws.yml";

	public static void main(String[] args) {
		new SpringApplicationBuilder(HallapinyoMarketSpringApplication.class)
				.properties(APPLICATION_LOCATIONS)
				.run(args);
	}

}
