package se.sundsvall.archive.api.domain.byggr;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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
