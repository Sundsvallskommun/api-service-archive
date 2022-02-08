package se.sundsvall.archive.integration.formpipeproxy.domain;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImportRequest {

    @JsonProperty("SubmissionAgreementId")
    private String submissionAgreementId;

    @JsonProperty("Uuid")
    private String uuid;

    @JsonProperty("ConfidentialityLevel")
    private Integer confidentialityLevel;

    @JsonProperty("ConfidentialityDegradationDate")
    private LocalDateTime confidentialityDegradationDate;

    @JsonProperty("PersonalDataFlag")
    private Boolean personalDataFlag;

    @JsonProperty("MetadataXml")
    private String metadataXml;

    @JsonProperty("PreservationObject")
    private PreservationObject preservationObject;

    @Getter
    @Setter
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class PreservationObject {

        @JsonProperty("FileName")
        private String fileName;

        @JsonProperty("FileExtension")
        private String fileExtension;

        @JsonProperty("Data")
        private String data;
    }
}
