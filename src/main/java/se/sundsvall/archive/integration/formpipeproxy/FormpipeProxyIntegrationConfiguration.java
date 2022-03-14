package se.sundsvall.archive.integration.formpipeproxy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.zalando.logbook.Logbook;

import se.sundsvall.dept44.configuration.resttemplate.RestTemplateBuilder;

@Configuration
class FormpipeProxyIntegrationConfiguration {

    private final FormpipeProxyIntegrationProperties properties;
    private final Logbook logbook;

    FormpipeProxyIntegrationConfiguration(final FormpipeProxyIntegrationProperties properties,
            final Logbook logbook) {
        this.properties = properties;
        this.logbook = logbook;
    }

    @Bean("integration.formpipe-proxy.resttemplate")
    RestTemplate restTemplate() {
        return new RestTemplateBuilder()
            .withBaseUrl(properties.getBaseUrl())
            .withLogbook(logbook)
            .withConnectTimeout(properties.getConnectTimeout())
            .withReadTimeout(properties.getReadTimeout())
            .build();
    }
}
