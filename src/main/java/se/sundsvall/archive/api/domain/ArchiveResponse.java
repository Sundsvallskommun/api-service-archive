package se.sundsvall.archive.api.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(setterPrefix = "with")
@Schema(description = "Archive response")
public class ArchiveResponse {

    @Schema(description = "Archive ID", nullable = true)
    private String archiveId;

    @Schema(description = "Error details", nullable = true)
    private ErrorDetails errorDetails;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(setterPrefix = "with")
    public static class ErrorDetails {

        @Schema(description = "Formpipe error code")
        private Integer errorCode;

        @Schema(description = "Formpipe error message")
        private String errorMessage;

        @Schema(description = "Formpipe service name", nullable = true)
        private String serviceName;
    }
}
