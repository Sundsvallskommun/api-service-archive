package se.sundsvall.archive.api;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import se.sundsvall.archive.Application;
import se.sundsvall.archive.api.domain.byggr.Attachment;
import se.sundsvall.archive.api.domain.byggr.ByggRArchiveRequest;
import se.sundsvall.archive.api.domain.byggr.ByggRFormpipeProxyMapper;
import se.sundsvall.archive.integration.formpipeproxy.FormpipeProxyIntegration;
import se.sundsvall.dept44.problem.violations.ConstraintViolationProblem;
import se.sundsvall.dept44.problem.violations.Violation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;

@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
@ActiveProfiles("junit")
@AutoConfigureWebTestClient
class ArchiveResourceFailuresTest {

	private static final String PATH_TEMPLATE = "/{municipalityId}/archive/byggr";
	private static final String MUNICIPALITY_ID = "2281";

	@MockitoBean
	private FormpipeProxyIntegration mockFormpipeProxyIntegration;

	@MockitoBean
	private ByggRFormpipeProxyMapper mockByggRFormpipeProxyMapper;

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void emptyRequest() {

		// Arrange.
		final var request = new ByggRArchiveRequest();

		// Act
		final var response = webTestClient.post()
			.uri(builder -> builder.path(PATH_TEMPLATE).build(Map.of("municipalityId", MUNICIPALITY_ID)))
			.contentType(APPLICATION_JSON)
			.bodyValue(request)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody(ConstraintViolationProblem.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::field, Violation::message)
			.containsExactlyInAnyOrder(
				tuple("attachment", "must not be null"),
				tuple("metadata", "must not be blank"));

		verifyNoInteractions(mockByggRFormpipeProxyMapper, mockFormpipeProxyIntegration);
	}

	@Test
	void emptyAttachment() {

		// Arrange.
		final var request = ByggRArchiveRequest.builder()
			.withAttachment(new Attachment())
			.withMetadata("metadata")
			.build();

		// Act
		final var response = webTestClient.post()
			.uri(builder -> builder.path(PATH_TEMPLATE).build(Map.of("municipalityId", MUNICIPALITY_ID)))
			.contentType(APPLICATION_JSON)
			.bodyValue(request)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody(ConstraintViolationProblem.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::field, Violation::message)
			.containsExactlyInAnyOrder(
				tuple("attachment.extension", "must not be blank"),
				tuple("attachment.file", "not a valid BASE64-encoded string"),
				tuple("attachment.name", "must not be blank"));

		verifyNoInteractions(mockByggRFormpipeProxyMapper, mockFormpipeProxyIntegration);
	}

	@Test
	void invalidMunicipalityId() {

		// Arrange.
		final var municipalityId = "invalid";
		final var request = ByggRArchiveRequest.builder()
			.withAttachment(Attachment.builder()
				.withName("name")
				.withExtension(".txt")
				.withFile("content")
				.build())
			.withMetadata("metadata")
			.build();

		// Act
		final var response = webTestClient.post()
			.uri(builder -> builder.path(PATH_TEMPLATE).build(Map.of("municipalityId", municipalityId)))
			.contentType(APPLICATION_JSON)
			.bodyValue(request)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody(ConstraintViolationProblem.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::field, Violation::message)
			.containsExactlyInAnyOrder(tuple("byggR.municipalityId", "not a valid municipality ID"));

		verifyNoInteractions(mockByggRFormpipeProxyMapper, mockFormpipeProxyIntegration);
	}
}
