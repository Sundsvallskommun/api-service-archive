package se.sundsvall.archive.api.domain.byggr;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Request för att arkivera bygglovshandlingar, med BASE64-encode:ad metadata")
public class ByggRArchiveRequest2 {

    @Valid
    @NotNull
    @Schema(description = "Bilaga")
    private Attachment attachment;

    @NotBlank
    @Schema(description = "Metadata XML")
    private String metadata;
}
