package se.sundsvall.archive.configuration;

import static org.zalando.logbook.json.JsonPathBodyFilters.jsonPath;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.BodyFilter;

@Configuration
class LogbookBodyFiltersConfiguration {

	@Bean
	BodyFilter incomingRequestFilePropertyFilter() {
		return jsonPath("$..file").replace(s -> String.format("<file contents omitted (%d char(s))>", s.length()));
	}

	@Bean
	BodyFilter outgoingRequestDataFilter() {
		return jsonPath("$..Data").replace(s -> String.format("<file contents omitted (%d char(s))>", s.length()));
	}
}
