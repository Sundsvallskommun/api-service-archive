package se.sundsvall.archive.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import se.sundsvall.archive.Application;
import se.sundsvall.archive.api.domain.ArchiveResponse;
import se.sundsvall.archive.api.domain.byggr.Attachment;
import se.sundsvall.archive.api.domain.byggr.ByggRArchiveRequest;
import se.sundsvall.archive.api.domain.byggr.ByggRFormpipeProxyMapper;
import se.sundsvall.archive.integration.formpipeproxy.FormpipeProxyIntegration;
import se.sundsvall.archive.integration.formpipeproxy.domain.ImportRequest;
import se.sundsvall.archive.integration.formpipeproxy.domain.ImportResponse;

@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
@ActiveProfiles("junit")
class ArchiveResourceTest {

	private static final String PATH_TEMPLATE = "/{municipalityId}/archive/byggr";
	private static final String MUNICIPALITY_ID = "2281";

	@MockBean
	private FormpipeProxyIntegration mockFormpipeProxyIntegration;

	@MockBean
	private ByggRFormpipeProxyMapper mockByggRFormpipeProxyMapper;

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void successful() {

		// Arrange.
		final var request = ByggRArchiveRequest.builder()
			.withAttachment(Attachment.builder()
				.withName("name")
				.withExtension(".txt")
				.withFile("content")
				.build())
			.withMetadata("metadata")
			.build();

		when(mockByggRFormpipeProxyMapper.map(any(ByggRArchiveRequest.class))).thenReturn(new ImportRequest());
		when(mockFormpipeProxyIntegration.doImport(any(ImportRequest.class))).thenReturn(new ImportResponse());
		when(mockByggRFormpipeProxyMapper.map(any(ImportResponse.class))).thenReturn(new ArchiveResponse());

		// Act
		final var archiveResponse = webTestClient.post()
			.uri(builder -> builder.path(PATH_TEMPLATE).build(Map.of("municipalityId", MUNICIPALITY_ID)))
			.contentType(APPLICATION_JSON)
			.bodyValue(request)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBody(ArchiveResponse.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(archiveResponse).isNotNull();

		verify(mockByggRFormpipeProxyMapper).map(any(ByggRArchiveRequest.class));
		verify(mockFormpipeProxyIntegration).doImport(any(ImportRequest.class));
		verify(mockByggRFormpipeProxyMapper).map(any(ImportResponse.class));
	}
}
