package reactivechallenge.pragma.techmanagementservice.input.router;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactivechallenge.pragma.techmanagementservice.input.dto.TechnologyRequestDto;
import reactivechallenge.pragma.techmanagementservice.input.dto.TechnologyResponseDto;
import reactivechallenge.pragma.techmanagementservice.input.handler.TechnologyHandler;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class TechnologyRouter {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/create",
                    produces = {
                            MediaType.APPLICATION_JSON_VALUE
                    },
                    method = RequestMethod.POST,
                    beanClass = TechnologyHandler.class,
                    beanMethod = "createTechnology",
                    operation = @Operation(
                            operationId = "createTechnology",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Technology created",
                                            content = @Content(schema = @Schema(implementation = TechnologyResponseDto.class))
                                    )
                            },
                            requestBody = @RequestBody(
                                    content = @Content(schema = @Schema(implementation = TechnologyRequestDto.class))
                            )
                    )
            )
    })
    public RouterFunction<ServerResponse> technologyRoutes(TechnologyHandler technologyHandler) {
        return RouterFunctions.route(
                POST("/create").and(accept(MediaType.APPLICATION_JSON)),
                technologyHandler::createTechnology
        );
    }
}
