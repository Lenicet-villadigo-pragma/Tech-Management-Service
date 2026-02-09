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
import reactivechallenge.pragma.techmanagementservice.input.dto.TechnologyRequestDto;
import reactivechallenge.pragma.techmanagementservice.input.dto.TechnologyResponseDto;
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
        TechnologyRequestDto requestDto = new TechnologyRequestDto("Java", "Programming Language");
        TechnologyResponseDto responseDto = new TechnologyResponseDto("Java", "Programming Language");

        when(technologyHandler.createTechnology(any())).thenReturn(
            ServerResponse.created(java.net.URI.create("/create"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(responseDto), TechnologyResponseDto.class)
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
                .expectBody(TechnologyResponseDto.class)
                .isEqualTo(responseDto);
    }
}
