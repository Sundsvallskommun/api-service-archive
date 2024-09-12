package se.sundsvall.archive.api.domain.byggr;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ByggRArchiveRequestTest {

	@Test
	void testGettersAndSetters() {
		final var request = ByggRArchiveRequest.builder()
			.withAttachment(new Attachment())
			.withMetadata("someMetadata")
			.build();

		assertThat(request.getAttachment()).isNotNull();
		assertThat(request.getMetadata()).isEqualTo("someMetadata");
	}
}
