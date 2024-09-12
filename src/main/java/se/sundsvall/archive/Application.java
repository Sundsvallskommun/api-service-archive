package se.sundsvall.archive;

import static org.springframework.boot.SpringApplication.run;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import se.sundsvall.dept44.ServiceApplication;
import se.sundsvall.dept44.util.jacoco.ExcludeFromJacocoGeneratedCoverageReport;

@ServiceApplication
@ExcludeFromJacocoGeneratedCoverageReport
public class Application {

	private static final Logger LOG = LoggerFactory.getLogger("STARTUP");

	public static void main(String... args) {
		run(Application.class, args);
	}

	@Bean
	CommandLineRunner startupDebug(
		@Value("${integration.formpipe-proxy.base-url}") final String formpipeProxyIntegrationBaseUrl,
		@Value("${byggr.submission-agreement-id}") final String byggRSubmissionAgreementId) {
		return args -> {
			LOG.info("Formpipe Proxy base URL = '{}'", formpipeProxyIntegrationBaseUrl);
			LOG.info("ByggR submission agreement id = '{}'", byggRSubmissionAgreementId);
		};
	}
}
