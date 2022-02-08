package se.sundsvall.archive.integration.formpipeproxy;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "integration.formpipe-proxy")
class FormpipeProxyIntegrationProperties {

    private String baseUrl;
}
