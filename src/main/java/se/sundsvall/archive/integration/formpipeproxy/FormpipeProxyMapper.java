package se.sundsvall.archive.integration.formpipeproxy;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import se.sundsvall.archive.integration.formpipeproxy.domain.ImportRequest;
import se.sundsvall.archive.integration.formpipeproxy.domain.ImportResponse;

/**
 * Formpipe Proxy Mapper, defining the contract for mapping different archive requests/responses
 * to/from Formpipe requests/responses.
 *
 * @param <I> the archive request type (Input)
 * @param <O> the archive response type (Output)
 */
public interface FormpipeProxyMapper<I, O> {

	ImportRequest map(I request);

	O map(ImportResponse response);

	default String toBase64(final String s) {
		return Base64.getEncoder().encodeToString(s.getBytes(StandardCharsets.UTF_8));
	}
}
