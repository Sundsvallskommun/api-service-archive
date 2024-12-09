package se.sundsvall.archive.integration.formpipeproxy;

import java.time.Duration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "integration.formpipe-proxy")
class FormpipeProxyIntegrationProperties {

	private String baseUrl;
	private Duration connectTimeout = Duration.ofSeconds(10);
	private Duration readTimeout = Duration.ofSeconds(30);
}
