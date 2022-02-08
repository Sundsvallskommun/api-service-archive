package se.sundsvall.archive.integration.formpipeproxy;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import se.sundsvall.archive.integration.formpipeproxy.domain.ImportRequest;
import se.sundsvall.archive.integration.formpipeproxy.domain.ImportResponse;
/**
 * Formpipe Proxy Mapper, defining the contract for mapping different archive requests/responses
 * to/from Formpipe requests/responses.
 *
 * @param <REQUEST> the archive request type
 * @param <RESPONSE> the archive response type
 */
public interface FormpipeProxyMapper<REQUEST, RESPONSE> {

    ImportRequest map(REQUEST request);

    RESPONSE map(ImportResponse response);

    default String toBase64(final String s) {
        return Base64.getEncoder().encodeToString(s.getBytes(StandardCharsets.UTF_8));
    }
}
