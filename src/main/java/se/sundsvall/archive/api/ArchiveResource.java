package se.sundsvall.archive.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zalando.problem.Problem;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import se.sundsvall.archive.api.domain.ArchiveResponse;
import se.sundsvall.archive.api.domain.byggr.ByggRArchiveRequest;
import se.sundsvall.archive.api.domain.byggr.ByggRFormpipeProxyMapper;
import se.sundsvall.archive.integration.formpipeproxy.FormpipeProxyIntegration;

@RestController
@RequestMapping("/archive")
@Tag(name = "Archive resources")
class ArchiveResource {

	private final FormpipeProxyIntegration formpipeProxyIntegration;
	private final ByggRFormpipeProxyMapper byggRFormpipeProxyMapper;

	ArchiveResource(final FormpipeProxyIntegration formpipeProxyIntegration,
		final ByggRFormpipeProxyMapper byggRFormpipeProxyMapper) {
		this.formpipeProxyIntegration = formpipeProxyIntegration;
		this.byggRFormpipeProxyMapper = byggRFormpipeProxyMapper;
	}

	@Operation(summary = "Submit a ByggR archive request")
	@ApiResponse(responseCode = "200", description = "Successful operation", useReturnTypeSchema = true)
	@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = Problem.class)))
	@PostMapping("/byggr")
	ArchiveResponse byggR(@Valid @RequestBody final ByggRArchiveRequest request) {
		return byggRFormpipeProxyMapper.map(
			formpipeProxyIntegration.doImport(
				byggRFormpipeProxyMapper.map(request)));
	}
}
