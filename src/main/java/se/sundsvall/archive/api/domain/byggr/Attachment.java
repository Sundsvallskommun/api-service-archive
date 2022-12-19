package se.sundsvall.archive.api.domain.byggr;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import se.sundsvall.archive.api.domain.validation.ValidBase64String;

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
    @ValidBase64String
    @Schema(description = "BASE64-encoded file contents (Max 100Mb)", example = "YXBhCg==", maxLength = 100000000)
    private String file;
}