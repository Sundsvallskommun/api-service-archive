package se.sundsvall.archive.api.domain.byggr;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ByggRArchiveRequestTest {

    @Test
    void testGettersAndSetters() {
        var request = new ByggRArchiveRequest();
        request.setAttachment(new Attachment());
        request.setMetadata("someMetadata");

        assertThat(request.getAttachment()).isNotNull();
        assertThat(request.getMetadata()).isEqualTo("someMetadata");
    }
}
