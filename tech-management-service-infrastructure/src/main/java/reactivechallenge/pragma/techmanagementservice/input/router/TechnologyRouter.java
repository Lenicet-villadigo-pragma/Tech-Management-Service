package reactivechallenge.pragma.techmanagementservice.input.router;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactivechallenge.pragma.techmanagementservice.input.dto.CreateTechnologyRequestDto;
import reactivechallenge.pragma.techmanagementservice.input.dto.CreateTechnologyResponseDto;
import reactivechallenge.pragma.techmanagementservice.input.handler.TechnologyHandler;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

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
                            summary = "Crear nueva tecnología",
                            description = "Crea un nuevo registro de tecnología en el sistema con la información proporcionada.",
                            tags = {"Gestión de Tecnologías"},
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Technology created",
                                            content = @Content(schema = @Schema(implementation = CreateTechnologyResponseDto.class))
                                    )
                            },
                            requestBody = @RequestBody(
                                    content = @Content(schema = @Schema(implementation = CreateTechnologyRequestDto.class))
                            )
                    )
            ),
            @RouterOperation(
                    path = "/exists",
                    produces = {
                            MediaType.APPLICATION_JSON_VALUE
                    },
                    method = RequestMethod.GET,
                    beanClass = TechnologyHandler.class,
                    beanMethod = "verifyIfTechnologyExist",
                    operation = @Operation(
                            operationId = "verifyIfTechnologiesExists",
                            summary = "Verificar existencia de una o más tecnologías",
                            description = "Consulta si las tecnologías enviadas existen en la base de datos.",
                            tags = {"Gestión de Tecnologías"},
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Technology existence verified",
                                            content = @Content(schema = @Schema(implementation = Boolean.class))
                                    )
                            },
                            parameters = {
                                    @Parameter(in = ParameterIn.QUERY, name = "techIds", description = "list of Technology IDs")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> technologyRoutes(TechnologyHandler technologyHandler) {
        return RouterFunctions.route(
                POST("/create").and(accept(MediaType.APPLICATION_JSON)),
                        technologyHandler::createTechnology)
                .andRoute(GET("/exists"),
                        technologyHandler::verifyIfTechnologyExist);
    }
}
