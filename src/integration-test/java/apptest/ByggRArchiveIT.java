package apptest;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.OK;

import org.junit.jupiter.api.Test;

import se.sundsvall.archive.Application;
import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;

@WireMockAppTestSuite(files = "classpath:/ByggRArchiveIT/", classes = Application.class)
class ByggRArchiveIT extends AbstractAppTest {

	private static final String SERVICE_PATH = "/2281/archive/byggr";

	@Test
	void test1_successfulRequest() {
		setupCall()
			.withServicePath(SERVICE_PATH)
			.withHttpMethod(POST)
			.withRequest("request.json")
			.withExpectedResponseStatus(OK)
			.withExpectedResponse("response.json")
			.sendRequestAndVerifyResponse();
	}
}
