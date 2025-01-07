package se.sundsvall.archive.integration.formpipeproxy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import se.sundsvall.archive.integration.formpipeproxy.domain.ImportRequest;
import se.sundsvall.archive.integration.formpipeproxy.domain.ImportResponse;

class FormpipeProxyMapperTest {

	private final FormpipeProxyMapper<String, String> mapper = new FormpipeProxyMapper<>() {

		@Override
		public ImportRequest map(final String s) {
			return null;
		}

		@Override
		public String map(final ImportResponse response) {
			return null;
		}
	};

	@Test
	void toBase64() {
		assertThat(mapper.toBase64("hello world")).isEqualTo("aGVsbG8gd29ybGQ=");
	}
}
