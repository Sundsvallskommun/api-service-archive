package se.sundsvall.archive.integration.formpipeproxy;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import org.springframework.cloud.openfeign.FeignBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import se.sundsvall.dept44.configuration.feign.FeignConfiguration;
import se.sundsvall.dept44.configuration.feign.FeignMultiCustomizer;

import feign.Request.Options;

@Import(FeignConfiguration.class)
class FormpipeProxyIntegrationConfiguration {

    private final FormpipeProxyIntegrationProperties properties;

    FormpipeProxyIntegrationConfiguration(final FormpipeProxyIntegrationProperties properties) {
        this.properties = properties;
    }

    @Bean
    FeignBuilderCustomizer feignCustomizer() {
        return FeignMultiCustomizer.create()
            .withRequestOptions(requestOptions())
            .composeCustomizersToOne();
    }

    private Options requestOptions() {
        return new Options(
            properties.getConnectTimeout().toMillis(), MILLISECONDS,
            properties.getReadTimeout().toMillis(), MILLISECONDS,
            true
        );
    }
}
