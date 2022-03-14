package se.sundsvall.archive.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import se.sundsvall.archive.api.domain.ArchiveResponse;
import se.sundsvall.archive.api.domain.byggr.ByggRArchiveRequest;
import se.sundsvall.archive.api.domain.byggr.ByggRFormpipeProxyMapper;
import se.sundsvall.archive.integration.formpipeproxy.FormpipeProxyIntegration;
import se.sundsvall.archive.integration.formpipeproxy.domain.ImportRequest;
import se.sundsvall.archive.integration.formpipeproxy.domain.ImportResponse;

@ExtendWith(MockitoExtension.class)
class ArchiveResourceTest {

    @Mock
    private FormpipeProxyIntegration mockFormpipeProxyIntegration;
    @Mock
    private ByggRFormpipeProxyMapper mockByggRFormpipeProxyMapper;

    private ArchiveResource archiveResource;

    @BeforeEach
    void setUp() {
        archiveResource = new ArchiveResource(mockFormpipeProxyIntegration,
            mockByggRFormpipeProxyMapper);
    }

    @Test
    void test_successful_byggR_shouldReturnResponse() {
        when(mockByggRFormpipeProxyMapper.map(any(ByggRArchiveRequest.class)))
            .thenReturn(new ImportRequest());
        when(mockFormpipeProxyIntegration.doImport(any(ImportRequest.class)))
            .thenReturn(new ImportResponse());
        when(mockByggRFormpipeProxyMapper.map(any(ImportResponse.class)))
            .thenReturn(new ArchiveResponse());

        var archiveResponse = archiveResource.byggR(new ByggRArchiveRequest());

        assertThat(archiveResponse).isNotNull();

        verify(mockByggRFormpipeProxyMapper, times(1)).map(any(ByggRArchiveRequest.class));
        verify(mockFormpipeProxyIntegration, times(1)).doImport(any(ImportRequest.class));
        verify(mockByggRFormpipeProxyMapper, times(1)).map(any(ImportResponse.class));
    }
}
