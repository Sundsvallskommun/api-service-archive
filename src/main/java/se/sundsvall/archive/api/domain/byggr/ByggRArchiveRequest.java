package se.sundsvall.archive.api.domain.byggr;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Request för att arkivera bygglovshandlingar")
public class ByggRArchiveRequest {

    @Valid
    @NotNull
    @Schema(description = "Bilaga")
    private Attachment attachment;

    @NotBlank
    @Schema(description = "Metadata XML")
    private String metadata;
}
