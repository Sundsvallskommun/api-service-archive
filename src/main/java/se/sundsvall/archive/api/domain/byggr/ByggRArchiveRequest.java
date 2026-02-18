package se.sundsvall.archive.api.domain.byggr;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor(access = PRIVATE)
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
