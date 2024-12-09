package se.sundsvall.archive.integration.formpipeproxy.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImportResponse {

	@JsonProperty("ErrorDetails")
	private ErrorDetails errorDetails;

	@JsonProperty("ImportedFileSetId")
	private String importedFileSetId;

	@Getter
	@Setter
	public static class ErrorDetails {

		@JsonProperty("ErrorCode")
		private Integer errorCode;

		@JsonProperty("ErrorMessage")
		private String errorMessage;

		@JsonProperty("ServiceName")
		private String serviceName;
	}
}
