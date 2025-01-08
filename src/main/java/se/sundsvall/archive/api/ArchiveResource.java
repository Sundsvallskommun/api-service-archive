package se.sundsvall.archive.api;

import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zalando.problem.Problem;
import org.zalando.problem.violations.ConstraintViolationProblem;
import se.sundsvall.archive.api.domain.ArchiveResponse;
import se.sundsvall.archive.api.domain.byggr.ByggRArchiveRequest;
import se.sundsvall.archive.api.domain.byggr.ByggRFormpipeProxyMapper;
import se.sundsvall.archive.integration.formpipeproxy.FormpipeProxyIntegration;
import se.sundsvall.dept44.common.validators.annotation.ValidMunicipalityId;

@RestController
@RequestMapping("/{municipalityId}/archive")
@Tag(name = "Archive resources")
@Validated
class ArchiveResource {

	private final FormpipeProxyIntegration formpipeProxyIntegration;
	private final ByggRFormpipeProxyMapper byggRFormpipeProxyMapper;

	ArchiveResource(final FormpipeProxyIntegration formpipeProxyIntegration,
		final ByggRFormpipeProxyMapper byggRFormpipeProxyMapper) {
		this.formpipeProxyIntegration = formpipeProxyIntegration;
		this.byggRFormpipeProxyMapper = byggRFormpipeProxyMapper;
	}

	@PostMapping("/byggr")
	@Operation(summary = "Submit a ByggR archive request", responses = {
		@ApiResponse(responseCode = "200", description = "Successful operation", useReturnTypeSchema = true),
		@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(oneOf = {
			Problem.class, ConstraintViolationProblem.class
		}))),
		@ApiResponse(responseCode = "500", description = "Internal Server error", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class))),
		@ApiResponse(responseCode = "502", description = "Bad Gateway", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	})
	ArchiveResponse byggR(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@Valid @RequestBody final ByggRArchiveRequest request) {

		return byggRFormpipeProxyMapper.map(formpipeProxyIntegration.doImport(byggRFormpipeProxyMapper.map(request)));
	}
}
