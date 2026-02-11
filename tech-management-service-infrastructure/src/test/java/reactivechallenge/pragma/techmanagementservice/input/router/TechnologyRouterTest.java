package reactivechallenge.pragma.techmanagementservice.input.router;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactivechallenge.pragma.techmanagementservice.input.dto.CreateTechnologyRequestDto;
import reactivechallenge.pragma.techmanagementservice.input.dto.CreateTechnologyResponseDto;
import reactivechallenge.pragma.techmanagementservice.input.handler.TechnologyHandler;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TechnologyRouterTest {

    @Mock
    private TechnologyHandler technologyHandler;

    @InjectMocks
    private TechnologyRouter technologyRouter;

    @Test
    @DisplayName("Router routes POST /create to handler")
    void createTechnologyRouteTest() {
        // Arrange
        CreateTechnologyRequestDto requestDto = new CreateTechnologyRequestDto("Java", "Programming Language");
        CreateTechnologyResponseDto responseDto = new CreateTechnologyResponseDto("Java", "Programming Language");

        when(technologyHandler.createTechnology(any())).thenReturn(
            ServerResponse.created(java.net.URI.create("/create"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(responseDto), CreateTechnologyResponseDto.class)
        );

        WebTestClient webTestClient = WebTestClient
                .bindToRouterFunction(technologyRouter.technologyRoutes(technologyHandler))
                .build();

        // Act & Assert
        webTestClient.post()
                .uri("/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(CreateTechnologyResponseDto.class)
                .isEqualTo(responseDto);
    }

    @Test
    @DisplayName("Router routes GET /verify to handler and returns true when exists")
    void verifyTechnologyExistsRouteReturnsTrue() {
        // Arrange
        Long techId = 1L;

        when(technologyHandler.verifyIfTechnologyExist(any())).thenReturn(
                ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(true)
        );

        WebTestClient webTestClient = WebTestClient
                .bindToRouterFunction(technologyRouter.technologyRoutes(technologyHandler))
                .build();

        // Act & Assert
        webTestClient.get()
                .uri("/exists", techId)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Boolean.class)
                .isEqualTo(true);
    }

    @Test
    @DisplayName("Router routes GET /verify to handler and returns false when not exists")
    void verifyTechnologyExistsRouteReturnsFalse() {
        // Arrange
        Long techId = 999L;

        when(technologyHandler.verifyIfTechnologyExist(any())).thenReturn(
                ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(false)
        );

        WebTestClient webTestClient = WebTestClient
                .bindToRouterFunction(technologyRouter.technologyRoutes(technologyHandler))
                .build();

        // Act & Assert
        webTestClient.get()
                .uri("/exists", techId)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Boolean.class)
                .isEqualTo(false);
    }

}
