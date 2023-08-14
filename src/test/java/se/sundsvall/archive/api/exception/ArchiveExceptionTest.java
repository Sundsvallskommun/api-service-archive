package se.sundsvall.archive.api.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ArchiveExceptionTest {

	@Test
	void testExceptionWithMessage() {
		final var message = "This is the ArchiveException";

		assertThat(new ArchiveException(message)).hasMessage(message);
	}

	@Test
	void testExceptionWithMessageAndThrowable() {
		final var message = "This is the ArchiveException";
		final var exception = new RuntimeException("This is an RunTimeException");

		assertThat(new ArchiveException(message, exception))
			.hasMessage(message)
			.hasCause(exception);
	}
}
