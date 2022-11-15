package se.sundsvall.archive.configuration;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

import se.sundsvall.archive.Application;

@Configuration
@EnableFeignClients(basePackageClasses = Application.class)
class FeignConfiguration {

}
