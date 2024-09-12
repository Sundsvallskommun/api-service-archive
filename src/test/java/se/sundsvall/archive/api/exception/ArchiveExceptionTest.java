package se.sundsvall.archive.api.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ArchiveExceptionTest {

	@Test
	void exceptionWithMessage() {
		final var message = "This is the ArchiveException";

		assertThat(new ArchiveException(message)).hasMessage(message);
	}

	@Test
	void exceptionWithMessageAndThrowable() {
		final var message = "This is the ArchiveException";
		final var exception = new RuntimeException("This is an RunTimeException");

		assertThat(new ArchiveException(message, exception))
			.hasMessage(message)
			.hasCause(exception);
	}
}
