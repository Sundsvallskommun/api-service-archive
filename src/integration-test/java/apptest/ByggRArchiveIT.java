package apptest;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import se.sundsvall.archive.Application;
import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;

@WireMockAppTestSuite(files = "classpath:/ByggRArchiveIT/", classes = Application.class)
class ByggRArchiveIT extends AbstractAppTest {

    private static final String SERVICE_PATH = "/archive/byggr";

    @Test
    void test1_successful() throws Exception {
        setupCall()
            .withServicePath(SERVICE_PATH)
            .withHttpMethod(HttpMethod.POST)
            .withRequest("byggr.request")
            .withExpectedResponseStatus(HttpStatus.OK)
            .withExpectedResponse("byggr.expected-response")
            .sendRequestAndVerifyResponse();
    }

    // TODO: add "regular" failure test case

}
