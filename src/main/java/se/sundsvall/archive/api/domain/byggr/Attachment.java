package se.sundsvall.archive.api.domain.byggr;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import se.sundsvall.dept44.common.validators.annotation.ValidBase64;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Attachment {

    @NotBlank
    private String name;

    @NotBlank
    @Pattern(regexp = "^\\.(bmp|gif|tif|tiff|jpeg|jpg|png|htm|html|pdf|rtf|doc|docx|txt|xls|xlsx|odt|ods|pptx|ppt|msg)$", message = "extension must be valid. Must match regex: {regexp}")
    @Schema(example = ".pdf")
    private String extension;

    @Size(max = 100000000)
    @ValidBase64
    @Schema(description = "BASE64-encoded file contents (Max 100Mb)", example = "YXBhCg==", maxLength = 100000000)
    private String file;
}