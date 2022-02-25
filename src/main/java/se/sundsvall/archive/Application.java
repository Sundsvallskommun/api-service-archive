package se.sundsvall.archive;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;

import se.sundsvall.dept44.ServiceApplication;

@ServiceApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	CommandLineRunner startupDebug(
			@Value("${integration.formpipe-proxy.base-url}") final String formpipeProxyIntegrationBaseUrl) {
		return args -> {
			LoggerFactory.getLogger("STARTUP").info("Formpipe Proxy base URL = '{}'", formpipeProxyIntegrationBaseUrl);
		};
	}
}
